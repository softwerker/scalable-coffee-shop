package com.sebastian_daschner.scalable_coffee_shop.events.control;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.CoffeeEvent;

import java.io.IOException;

public class EventJsonSerializer extends StdSerializer<CoffeeEvent> {

	private final ObjectMapper objectMapper;

	protected EventJsonSerializer() {

		super(CoffeeEvent.class);

		objectMapper = new ObjectMapper()
				.registerModule(new JavaTimeModule())
				.enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@Override
	public void serialize(final CoffeeEvent event, final JsonGenerator generator, final SerializerProvider provider) throws IOException {

		generator.writeStartObject();
		generator.writeStringField("class", event.getClass().getCanonicalName());
		generator.writeFieldName("data");
		// TODO
		generator.writeRawValue(objectMapper.writeValueAsString(event));
		generator.writeEndObject();
	}
}