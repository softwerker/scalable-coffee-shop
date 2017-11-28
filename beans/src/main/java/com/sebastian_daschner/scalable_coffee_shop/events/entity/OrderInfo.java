package com.sebastian_daschner.scalable_coffee_shop.events.entity;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

public class OrderInfo {

	private final UUID orderId;

	private final CoffeeType type;

	private final String beanOrigin;

	public OrderInfo(final UUID orderId, final CoffeeType type, final String beanOrigin) {

		this.orderId = orderId;
		this.type = type;
		this.beanOrigin = beanOrigin;
	}

	public OrderInfo(final JsonNode jsonObject) {

		this(UUID.fromString(jsonObject.get("orderId").asText()),
				CoffeeType.fromString(jsonObject.get("type").asText()),
				jsonObject.get("beanOrigin").asText());
	}

	public UUID getOrderId() {

		return orderId;
	}

	public CoffeeType getType() {

		return type;
	}

	public String getBeanOrigin() {

		return beanOrigin;
	}

}
