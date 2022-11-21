/*
 *  Copyright (C) 2021 the original author or authors.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package we.controller;

import java.time.Duration;
import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * Example
 * 
 * @author Francis Dong
 *
 */
@RestController
public class EchoController {

	@GetMapping("/echo")
	public Mono<String> echo(@RequestParam(value = "latency", required = false) Integer latency,
			@RequestParam(value = "echo", required = false) String echo, ServerWebExchange exchange,
			ServerHttpRequest request) {
		// System.out.println("receive: " + echo);
		if (latency != null && latency > 0 && latency < 60 * 1000) {
			return Mono.just(echo != null ? echo : "ok").delayElement(Duration.ofMillis(latency));
		} else {
			return Mono.just(echo != null ? echo : "ok");
		}
	}

	@GetMapping("/echoheaders")
	public Mono<String> echoheaders(ServerWebExchange exchange,
			ServerHttpRequest request) {
		HttpHeaders hds = request.getHeaders();
		return Mono.just(hds.toString());
	}
}
