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

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

/**
 * Example
 * 
 * @author Francis Dong
 *
 */
@RestController
public class WeatherController {

	/**
	 * 手机归属地
	 * 
	 * @param exchange
	 * @return
	 */
	@GetMapping(value = "/weather/getMobileCodeInfo")
	public Object findUserList(@RequestParam("mobile") String mobile, ServerWebExchange exchange) {
		Map<String, Object> result = new HashMap<>();
		result.put("code", 0);
		result.put("message", "success");
		Long n = Long.valueOf(mobile) % 2;
		Map<String, Object> m = new HashMap<>();
		if (n == 0) {
			m.put("city", "广州");
		} else {
			m.put("city", "西安");
		}
		result.put("data", m);
		return result;
	}

	/**
	 * 根据城市查询天气
	 * 
	 * @param exchange
	 * @return
	 */
	@GetMapping(value = "/weather/getWeatherbyCityName")
	public Object getWeatherbyCityName(@RequestParam("city") String city, ServerWebExchange exchange) {
		Map<String, Object> result = new HashMap<>();
		result.put("code", 0);
		result.put("message", "success");
		Map<String, Object> m = new HashMap<>();
		m.put("city", city);
		if (city.equals("广州")) {
			m.put("temp", "27度");
			m.put("desc", "有雨");
		} else {
			m.put("temp", "31度");
			m.put("desc", "晴");
		}
		result.put("data", m);

		return result;
	}

}
