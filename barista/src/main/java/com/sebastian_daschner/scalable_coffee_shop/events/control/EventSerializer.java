package com.sebastian_daschner.scalable_coffee_shop.events.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.CoffeeEvent;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;
import java.util.logging.Logger;

public class EventSerializer implements Serializer<CoffeeEvent> {

	private static final Logger logger = Logger.getLogger(EventSerializer.class.getName());

	private final ObjectMapper objectMapper = new ObjectMapper()
			.registerModule(new SimpleModule()
					.addSerializer(new EventJsonSerializer()));

	@Override
	public void configure(final Map<String, ?> configs, final boolean isKey) {
		// nothing to configure
	}

	@Override
	public byte[] serialize(final String topic, final CoffeeEvent event) {

		try {
			if (event == null) {
				return null;
			}

			return objectMapper.writeValueAsBytes(event);
		} catch (Exception e) {
			logger.severe("Could not serialize event: " + e.getMessage());
			throw new SerializationException("Could not serialize event", e);
		}
	}

	@Override
	public void close() {
		// nothing to do
	}

}
