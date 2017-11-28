package com.sebastian_daschner.scalable_coffee_shop.orders.boundary;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.CoffeeType;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.OrderInfo;
import com.sebastian_daschner.scalable_coffee_shop.orders.entity.CoffeeOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("orders")
public class OrdersResource {

	private static final Logger logger = LoggerFactory.getLogger(OrdersResource.class);

	@Autowired
	OrderCommandService commandService;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	OrderQueryService queryService;

	@PostMapping
	public ResponseEntity<Object> orderCoffee(@RequestBody JsonNode order, HttpServletRequest request, HttpServletResponse response) {

		logger.info("received: {}", order);

		final String beanOrigin = order.get("beanOrigin").asText();
		final CoffeeType coffeeType = CoffeeType.fromString(order.get("coffeeType").asText());

		if (beanOrigin == null || coffeeType == null) {
			return ResponseEntity.badRequest().build();
		}

		final UUID orderId = UUID.randomUUID();
		commandService.placeOrder(new OrderInfo(orderId, coffeeType, beanOrigin));

		final URI location = UriComponentsBuilder.fromHttpUrl(request.getRequestURL() + "/" + orderId).build().toUri();
		return ResponseEntity.accepted().location(location).build();
	}

	@GetMapping(path = "{id}")
	public ResponseEntity<JsonNode> getOrder(@PathVariable("id") UUID orderId) {

		final CoffeeOrder order = queryService.getOrder(orderId);

		if (order == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(JsonNodeFactory.instance.objectNode()
				.put("status", order.getState().name().toLowerCase())
				.put("type", order.getOrderInfo().getType().name().toLowerCase())
				.put("beanOrigin", order.getOrderInfo().getBeanOrigin())
		);
	}

}
