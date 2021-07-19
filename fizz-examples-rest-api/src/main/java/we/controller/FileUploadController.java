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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Example
 * 
 * @author Francis Dong
 *
 */
@RestController
public class FileUploadController {

	/**
	 * 
	 * File upload
	 * 
	 * @param exchange
	 * @return
	 */
	@PostMapping(value = "/post/fileUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Object formData(ServerWebExchange exchange) {
		Mono<MultiValueMap<String, Part>> m = exchange.getMultipartData();
		return m.flatMap(md -> {
			// extract non-file form data
			Map<String, Object> params = extractFormData(md);
			System.out.println(params);

			List<FilePart> list = new ArrayList<>();
			for (Entry<String, List<Part>> entry : md.entrySet()) {
				List<Part> val = entry.getValue();
				if (val != null && val.size() > 0) {
					val.stream().forEach(part -> {
						if (part instanceof FilePart) {
							FilePart fp = (FilePart) part;
							list.add(fp);
						}
					});
				}
			}

			if (list != null && list.size() > 0) {
				Flux<FilePart> fileParts = Flux.fromIterable(list);
				return fileParts.flatMap(fp -> {
					String tmpPath = System.getProperty("user.dir") + "/tmp/";
					File tmpFolder = new File(tmpPath);
					if (!tmpFolder.exists()) {
						tmpFolder.mkdirs();
					}
					String path = tmpPath + fp.filename();
					System.out.println(path);

					// Return file path
					return fp.transferTo(new File(path)).then(Mono.just("/tmp/" + fp.filename()));
				}).collectList().flatMap(urls -> {
					Map<String, Object> result = new HashMap<>();
					result.put("urls", urls);
					return Mono.just(result);
				});

			} else {
				return Mono.just(new HashMap<>());
			}
		});
	}

	/**
	 * Save profile
	 * 
	 * @param exchange
	 * @return
	 */
	@PostMapping(value = "/post/saveProfile", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Object saveProfile(ServerWebExchange exchange) {
		Mono<MultiValueMap<String, String>> m = exchange.getFormData();
		return m.flatMap(md -> {
			// save profile info ...
			System.out.println(md);

			Map<String, Object> result = new HashMap<>();
			result.put("code", 0);
			result.put("message", 0);
			Map<String, Object> user = new HashMap<>();
			user.put("userId", md.getFirst("userId"));
			user.put("name", md.getFirst("name"));
			user.put("age", md.getFirst("age"));
			user.put("avatarUrl", md.getFirst("avatarUrl"));
			result.put("user", user);
			return Mono.just(result);
		});
	}

	public static Map<String, Object> extractFormData(MultiValueMap<String, Part> params) {
		HashMap<String, Object> m = new HashMap<>();

		if (params == null || params.isEmpty()) {
			return m;
		}

		for (Entry<String, List<Part>> entry : params.entrySet()) {
			List<Part> val = entry.getValue();
			if (val != null && val.size() > 0) {
				if (val.size() > 1) {
					List<String> formFieldValues = new ArrayList<>();
					val.stream().forEach(part -> {
						if (part instanceof FormFieldPart) {
							FormFieldPart p = (FormFieldPart) part;
							formFieldValues.add(p.value());
						} else if (part instanceof FilePart) {
							FilePart fp = (FilePart) part;
							formFieldValues.add(fp.filename());
						}
					});
					if (formFieldValues.size() > 0) {
						m.put(entry.getKey(), formFieldValues);
					}
				} else {
					if (val.get(0) instanceof FormFieldPart) {
						FormFieldPart p = (FormFieldPart) val.get(0);
						m.put(entry.getKey(), p.value());
					} else if (val.get(0) instanceof FilePart) {
						FilePart fp = (FilePart) val.get(0);
						m.put(entry.getKey(), fp.filename());
					}
				}
			}
		}
		return m;
	}
}
