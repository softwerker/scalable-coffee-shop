package com.sebastian_daschner.scalable_coffee_shop.events.entity;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

public class OrderBeansValidated extends CoffeeEvent {

	private final UUID orderId;

	public OrderBeansValidated(final UUID orderId) {

		this.orderId = orderId;
	}

	public OrderBeansValidated(final UUID orderId, final Instant instant) {

		super(instant);
		this.orderId = orderId;
	}

	public OrderBeansValidated(final JsonNode jsonObject) {

		this(UUID.fromString(jsonObject.get("orderId").asText()), Instant.parse(jsonObject.get("instant").asText()));
	}

	public UUID getOrderId() {

		return orderId;
	}

}
