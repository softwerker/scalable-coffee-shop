package com.sebastian_daschner.scalable_coffee_shop.events.entity;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;

public class BeansFetched extends CoffeeEvent {

	private final String beanOrigin;

	public BeansFetched(final String beanOrigin) {

		this.beanOrigin = beanOrigin;
	}

	public BeansFetched(final String beanOrigin, final Instant instant) {

		super(instant);
		this.beanOrigin = beanOrigin;
	}

	public BeansFetched(final JsonNode jsonObject) {

		this(jsonObject.get("beanOrigin").asText(), Instant.parse(jsonObject.get("instant").asText()));

	}

	public String getBeanOrigin() {

		return beanOrigin;
	}

}
