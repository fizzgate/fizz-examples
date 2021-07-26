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
public class OrderController {

	/**
	 * Order list by page
	 * 
	 * @param exchange
	 * @return
	 */
	@GetMapping(value = "/order/list")
	public Object findUserList(@RequestParam("page") Integer page, ServerWebExchange exchange) {
		Map<String, Object> result = new HashMap<>();
		result.put("code", 0);
		result.put("message", "success");
		List<Map<String, Object>> list = new ArrayList<>();
		result.put("data", list);

		if (page == null || page > 5 || page < 1) {
			return result;
		}

		int pagesize = 5;
		int offset = (page - 1) * pagesize;

		for (int i = offset; i < offset + pagesize; i++) {
			int n = i + 1;
			Map<String, Object> order = new HashMap<>();
			order.put("orderNo", n);
			order.put("userId", n);
			order.put("product", "大西瓜");
			order.put("price", 9.90);
			order.put("quantity", 2);
			
			Map<String, Object> address = new HashMap<>();
			address.put("province", "广东");
			address.put("city", "广州");
			address.put("district", "海珠");
			address.put("detailInfo", "xx路xx号");
			
			order.put("address", address);
			order.put("remark", "abcddddddd");
			list.add(order);
		}

		return result;
	}

	/**
	 * Order detail
	 * 
	 * @param exchange
	 * @return
	 */
	@GetMapping(value = "/order/detail")
	public Object getOrderDetail(@RequestParam("userId") Integer userId, @RequestParam("orderNo") Integer orderNo, ServerWebExchange exchange) {
		Map<String, Object> result = new HashMap<>();
		result.put("code", 0);
		result.put("message", "success");

		if (userId == null || userId > 25 || userId < 1) {
			return null;
		}

		Map<String, Object> order = new HashMap<>();
		order.put("orderNo", orderNo);
		order.put("userId", userId);
		order.put("product", "大西瓜");
		order.put("price", 9.90);
		order.put("quantity", 2);
		
		Map<String, Object> address = new HashMap<>();
		address.put("province", "广东");
		address.put("city", "广州");
		address.put("district", "海珠");
		address.put("detailInfo", "xx路xx号");
		
		order.put("address", address);
		order.put("remark", "abcddddddd");
		
		result.put("data", order);

		return result;
	}

}
