package com.sebastian_daschner.scalable_coffee_shop.events.entity;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;

public class CoffeeBrewStarted extends CoffeeEvent {

	private final OrderInfo orderInfo;

	public CoffeeBrewStarted(OrderInfo orderInfo) {

		this.orderInfo = orderInfo;
	}

	public CoffeeBrewStarted(OrderInfo orderInfo, Instant instant) {

		super(instant);
		this.orderInfo = orderInfo;
	}

	public CoffeeBrewStarted(final JsonNode jsonObject) {

		this(new OrderInfo(jsonObject.get("orderInfo")), Instant.parse(jsonObject.get("instant").asText()));
	}

	public OrderInfo getOrderInfo() {

		return orderInfo;
	}

}
