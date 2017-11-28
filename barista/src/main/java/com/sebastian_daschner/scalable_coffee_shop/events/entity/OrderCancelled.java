package com.sebastian_daschner.scalable_coffee_shop.events.entity;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

public class OrderCancelled extends CoffeeEvent {

	private final UUID orderId;

	private final String reason;

	public OrderCancelled(final UUID orderId, final String reason) {

		this.orderId = orderId;
		this.reason = reason;
	}

	public OrderCancelled(final UUID orderId, final String reason, Instant instant) {

		super(instant);
		this.orderId = orderId;
		this.reason = reason;
	}

	public OrderCancelled(final JsonNode jsonObject) {

		this(UUID.fromString(jsonObject.get("orderId").asText()), jsonObject.get("reason").asText(), Instant.parse(jsonObject.get("instant").asText()));
	}

	public UUID getOrderId() {

		return orderId;
	}

	public String getReason() {

		return reason;
	}

}
