package com.sebastian_daschner.scalable_coffee_shop.events.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.context.ApplicationEvent;

import java.time.Instant;
import java.util.Objects;

@JsonIgnoreProperties({"source", "timestamp"})
public abstract class CoffeeEvent extends ApplicationEvent {

	private final Instant instant;

	protected CoffeeEvent() {

		super("dummy");

		instant = Instant.now();
	}

	protected CoffeeEvent(final Instant instant) {

		super("dummy");

		Objects.requireNonNull(instant);
		this.instant = instant;
	}

	public Instant getInstant() {

		return instant;
	}

	@Override
	public boolean equals(final Object o) {

		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final CoffeeEvent that = (CoffeeEvent) o;

		return instant.equals(that.instant);
	}

	@Override
	public int hashCode() {

		return instant.hashCode();
	}

}
