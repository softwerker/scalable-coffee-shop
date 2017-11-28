package com.sebastian_daschner.scalable_coffee_shop.beans.boundary;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("beans")
public class BeansResource {

	private static final Logger logger = LoggerFactory.getLogger(BeansResource.class);

	@Autowired
	BeanCommandService commandService;

	@Autowired
	BeanQueryService queryService;

	@GetMapping
	public ResponseEntity<JsonNode> getBeans() {

		final ObjectNode jsonObject = JsonNodeFactory.instance.objectNode();

		queryService.getStoredBeans()
				.forEach((beanName, stock) -> {
					jsonObject.put(beanName, stock);
				});

		return ResponseEntity.ok(jsonObject);
	}

	@PostMapping
	public ResponseEntity<Object> storeBeans(final JsonNode jsonObject) {

		logger.info("received: {}", jsonObject);

		final String beanOrigin = jsonObject.get("beanOrigin").asText();
		final int amount = jsonObject.get("amount").asInt();

		if (beanOrigin == null || amount == 0) {
			return ResponseEntity.badRequest().build();
		}

		commandService.storeBeans(beanOrigin, amount);

		return ResponseEntity.accepted().build();
	}

}
