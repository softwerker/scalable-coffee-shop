package com.sebastian_daschner.scalable_coffee_shop.events.entity;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

public class OrderDelivered extends CoffeeEvent {

	private final UUID orderId;

	public OrderDelivered(final UUID orderId) {

		this.orderId = orderId;
	}

	public OrderDelivered(final UUID orderId, Instant instant) {

		super(instant);
		this.orderId = orderId;
	}

	public OrderDelivered(final JsonNode jsonObject) {

		this(UUID.fromString(jsonObject.get("orderId").asText()), Instant.parse(jsonObject.get("instant").asText()));
	}

	public UUID getOrderId() {

		return orderId;
	}

}
