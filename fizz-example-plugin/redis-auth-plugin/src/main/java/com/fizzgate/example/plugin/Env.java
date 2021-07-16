package com.fizzgate.example.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import we.config.AggregateRedisConfig;
import we.plugin.auth.*;
import we.util.JacksonUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class Env {

    private static final Logger log = LoggerFactory.getLogger(Env.class);

    @Resource
    private GatewayGroupService gatewayGroupService;

    @Resource
    private ApiConfigService apiConfigService;

    @Resource(name = AggregateRedisConfig.AGGREGATE_REACTIVE_REDIS_TEMPLATE)
    private ReactiveStringRedisTemplate redisTemplate;

    @PostConstruct
    public void init() {
        gatewayGroupService.currentGatewayGroupSet = Stream.of(GatewayGroup.DEFAULT).collect(Collectors.toSet());
        String json = "{\"id\":666,\"isDeleted\":0,\"service\":\"xservice\",\"backendService\":\"service0\",\"fizzMethod\":\"AM\",\"path\":\"/ypath\",\"exactMatch\":true,\"backendPath\":\"/ypath\",\"access\":\"a\",\"pluginConfigs\":[{\"plugin\":\"redisAuthPlugin\", \"fixedConfig\":\"fc0\"},{\"plugin\":\"lastPlugin\"}],\"checkApp\":false,\"timeout\":0,\"proxyMode\":2}";
        ApiConfig ac = JacksonUtils.readValue(json, ApiConfig.class);
        ServiceConfig sc = new ServiceConfig(ac.service);
        apiConfigService.serviceConfigMap.put(ac.service, sc);
        sc.add(ac);
        redisTemplate.opsForValue().set("token", "user0").block();
        log.info("example env ready");
    }
}
