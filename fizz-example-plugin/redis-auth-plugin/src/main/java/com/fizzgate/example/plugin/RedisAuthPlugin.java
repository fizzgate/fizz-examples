package com.fizzgate.example.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import we.plugin.FizzPluginFilter;
import we.plugin.FizzPluginFilterChain;
import we.plugin.PluginConfig;
import we.plugin.auth.ApiConfig;
import we.proxy.FizzWebClient;
import we.util.Constants;
import we.util.ReactorUtils;
import we.util.WebUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component(RedisAuthPlugin.REDIS_AUTH_PLUGIN)
public class RedisAuthPlugin implements FizzPluginFilter {

    private static final Logger log = LoggerFactory.getLogger(RedisAuthPlugin.class);

    public static final String REDIS_AUTH_PLUGIN = "redisAuthPlugin";

    @Resource(name = RedisConfig.REACTIVE_STRING_REDIS_TEMPLATE)
    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    @Resource
    private FizzWebClient fizzWebClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, Map<String, Object> config) {
        String customConfig = (String) config.get(PluginConfig.CUSTOM_CONFIG); // 获取自定义的插件配置
        log.info("custom plugin config: " + customConfig);
        doSomething();

        DataBuffer requestBody = WebUtils.getRequestBody(exchange); // 获取请求体
        if (requestBody != null) {
            String requestBodyStr = requestBody.toString(StandardCharsets.UTF_8);
            log.info("request body: " + requestBodyStr);
//            ApiConfig apiConfig = WebUtils.getApiConfig(exchange);
//            apiConfig.type = ApiConfig.Type.REVERSE_PROXY;
//            apiConfig.httpHostPorts = Stream.of("http://127.0.0.1:9094").collect(Collectors.toList());

              // 若需调整转发到后端接口的请求体，可像下面这样设置新的体和类型，即可
//            String newRequestBody = "client " + requestBodyStr;
//            WebUtils.setConvertedRequestBody(exchange, newRequestBody); newRequestBody 也可以是 DataBuffer
//            String newRequestBodyType = MediaType.APPLICATION_ATOM_XML_VALUE;
//            WebUtils.appendHeader(exchange, HttpHeaders.CONTENT_TYPE, newRequestBodyType);

//            WebFilterChain chain = exchange.getAttribute(FizzPluginFilterChain.WEB_FILTER_CHAIN);
//            return chain.filter(exchange);
        }

        String tk = exchange.getRequest().getQueryParams().getFirst("token");
        return
                reactiveStringRedisTemplate.opsForValue().get(tk).defaultIfEmpty(Constants.Symbol.EMPTY)
                        .flatMap(
                                user -> {
                                    if (user == Constants.Symbol.EMPTY) {
                                        return WebUtils.buildDirectResponse(exchange, HttpStatus.OK, null, "不存在 token " + tk); // 拒绝当前请求
                                    } else {
                                        exchange.getAttributes().put("11", "22"); // 往后续插件或逻辑传递参数
                                        Mono next = FizzPluginFilterChain.next(exchange); // 执行下一个插件或后续逻辑
                                        return next.defaultIfEmpty(ReactorUtils.NULL).flatMap(
                                                nil -> {
                                                    doAfterNext(); // 当 next 完成时执行一些逻辑
                                                    return Mono.empty();
                                                }
                                        );
                                    }
                                }
                        );
    }

    public void doSomething() {
    }

    public void doAfterNext() {
        log.info("do after next plugin done");
    }
}
