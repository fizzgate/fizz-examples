package com.fizzgate.example.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import we.config.AggregateRedisConfig;
import we.plugin.FizzPluginFilter;
import we.plugin.FizzPluginFilterChain;
import we.plugin.PluginConfig;
import we.util.Constants;
import we.util.ReactorUtils;
import we.util.WebUtils;

import javax.annotation.Resource;
import java.util.Map;

@Component(RedisAuthPlugin.REDIS_AUTH_PLUGIN)
public class RedisAuthPlugin implements FizzPluginFilter {

    private static final Logger log = LoggerFactory.getLogger(RedisAuthPlugin.class);

    public static final String REDIS_AUTH_PLUGIN = "redisAuthPlugin";

    @Resource(name = AggregateRedisConfig.AGGREGATE_REACTIVE_REDIS_TEMPLATE)
    private ReactiveStringRedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, Map<String, Object> config) {
        String customConfig = (String) config.get(PluginConfig.CUSTOM_CONFIG); // 获取自定义的插件配置
        log.info("custom plugin config: " + customConfig);
        doSomething();

        String token = exchange.getRequest().getQueryParams().getFirst("token");
        return
                redisTemplate.opsForValue().get(token).defaultIfEmpty(Constants.Symbol.EMPTY)
                        .flatMap(
                                user -> {
                                    if (user == Constants.Symbol.EMPTY) {
                                        return WebUtils.buildDirectResponse(exchange, HttpStatus.OK, null, "不存在 token " + token);
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
