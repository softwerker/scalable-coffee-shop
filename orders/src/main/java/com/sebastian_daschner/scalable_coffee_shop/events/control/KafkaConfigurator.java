package com.sebastian_daschner.scalable_coffee_shop.events.control;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

@Component
public class KafkaConfigurator {

	private final Properties properties;

	public KafkaConfigurator() throws IOException {

		properties = PropertiesLoaderUtils.loadProperties(
				new ClassPathResource("/kafka.properties"));
	}

	public Properties getProperties() {

		return properties;
	}
}