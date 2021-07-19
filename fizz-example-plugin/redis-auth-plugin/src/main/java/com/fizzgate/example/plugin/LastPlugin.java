package com.fizzgate.example.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import we.plugin.FizzPluginFilter;
import we.util.WebUtils;

import java.util.Map;

@Component(LastPlugin.LAST_PLUGIN)
public class LastPlugin implements FizzPluginFilter {

    private static final Logger log = LoggerFactory.getLogger(LastPlugin.class);

    public static final String LAST_PLUGIN = "lastPlugin";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, Map<String, Object> config) {
        log.info("last plugin work ...");
        return WebUtils.buildDirectResponse(exchange, HttpStatus.OK, null, "response done");
    }
}
