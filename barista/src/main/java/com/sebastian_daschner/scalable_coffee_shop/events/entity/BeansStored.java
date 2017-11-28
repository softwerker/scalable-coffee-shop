package com.sebastian_daschner.scalable_coffee_shop.events.entity;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;

public class BeansStored extends CoffeeEvent {

	private final String beanOrigin;

	private final int amount;

	public BeansStored(final String beanOrigin, final int amount) {

		this.beanOrigin = beanOrigin;
		this.amount = amount;
	}

	public BeansStored(final String beanOrigin, final int amount, final Instant instant) {

		super(instant);
		this.beanOrigin = beanOrigin;
		this.amount = amount;
	}

	public BeansStored(final JsonNode jsonObject) {

		this(jsonObject.get("beanOrigin").asText(), jsonObject.get("amount").asInt(), Instant.parse(jsonObject.get("instant").asText()));
	}

	public String getBeanOrigin() {

		return beanOrigin;
	}

	public int getAmount() {

		return amount;
	}

}
