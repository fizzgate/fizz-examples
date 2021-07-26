package com.fizzgate.example.plugin;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import we.config.RedisReactiveConfig;
import we.config.RedisReactiveProperties;

@Configuration
public class RedisConfig {

    public static final String REACTIVE_REDIS_PROPERTIES = "reactiveRedisProperties";
    public static final String REACTIVE_STRING_REDIS_TEMPLATE = "reactiveStringRedisTemplate";
    public static final String REACTIVE_REDIS_CONNECTION_FACTORY = "reactiveRedisConnectionFactory";

    @ConfigurationProperties("redis")
    @Configuration(REACTIVE_REDIS_PROPERTIES)
    public static class ReactiveRedisProperties extends RedisReactiveProperties {
    }

    @Configuration
    public static class ReactiveRedisConfig extends RedisReactiveConfig {

        public ReactiveRedisConfig(@Qualifier(REACTIVE_REDIS_PROPERTIES) RedisReactiveProperties properties) {
            super(properties);
        }

        @Bean(REACTIVE_REDIS_CONNECTION_FACTORY)
        public ReactiveRedisConnectionFactory lettuceConnectionFactory() {
            return super.lettuceConnectionFactory();
        }

        @Bean(REACTIVE_STRING_REDIS_TEMPLATE)
        public ReactiveStringRedisTemplate reactiveStringRedisTemplate(@Qualifier(REACTIVE_REDIS_CONNECTION_FACTORY) ReactiveRedisConnectionFactory fact) {
            return super.reactiveStringRedisTemplate(fact);
        }
    }
}
