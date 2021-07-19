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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

/**
 * Example
 * 
 * @author Francis Dong
 *
 */
@RestController
public class TestDataController {

	/**
	 * Test Data
	 * 
	 * @param exchange
	 * @return
	 */
	@GetMapping(value = "/test/data")
	public Object findPostList(ServerWebExchange exchange) {
		Map<String, Object> result = new HashMap<>();
		result.put("code", 0);
		result.put("message", "success");
		result.put("name", "ABCD");
		List<Map<String, Object>> list = new ArrayList<>();
		result.put("data", list);

		for (int i = 0; i < 5; i++) {
			int n = i + 1;
			Map<String, Object> record = new HashMap<>();
			record.put("id", n);
			record.put("userId", n);
			record.put("title", "title " + n);
			record.put("hits", Double.valueOf(Math.random() * 100000).intValue() % 10000);
			list.add(record);
		}
		return result;
	}

}
