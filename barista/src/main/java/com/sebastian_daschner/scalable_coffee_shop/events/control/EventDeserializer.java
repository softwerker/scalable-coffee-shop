package com.sebastian_daschner.scalable_coffee_shop.events.control;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.CoffeeEvent;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.logging.Logger;

public class EventDeserializer implements Deserializer<CoffeeEvent> {

	private static final Logger logger = Logger.getLogger(EventDeserializer.class.getName());

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void configure(final Map<String, ?> configs, final boolean isKey) {
		// nothing to configure
	}

	@Override
	public CoffeeEvent deserialize(final String topic, final byte[] data) {

		try (ByteArrayInputStream input = new ByteArrayInputStream(data)) {
			final JsonNode jsonObject = objectMapper.readTree(input);
			final Class<? extends CoffeeEvent> eventClass = (Class<? extends CoffeeEvent>) Class.forName(jsonObject.get("class").asText());
			return eventClass.getConstructor(JsonNode.class).newInstance(jsonObject.get("data"));
		} catch (Exception e) {
			logger.severe("Could not deserialize event: " + e.getMessage());
			throw new SerializationException("Could not deserialize event", e);
		}
	}

	@Override
	public void close() {
		// nothing to do
	}
}