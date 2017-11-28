package com.sebastian_daschner.scalable_coffee_shop.beans.control;

import com.sebastian_daschner.scalable_coffee_shop.events.control.EventConsumer;
import com.sebastian_daschner.scalable_coffee_shop.events.control.KafkaConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

@Component
public class BeanUpdateConsumer {

	private EventConsumer externalEventConsumer;

	// TODO
	@Autowired
	private ThreadPoolTaskExecutor mes;

	@Autowired
	private KafkaConfigurator kafkaConfigurator;

	@Autowired
	private ApplicationEventPublisher internalEventPublisher;

	private static final Logger logger = Logger.getLogger(BeanUpdateConsumer.class.getName());

	@PostConstruct
	private void initConsumer() {

		kafkaProperties().put("group.id", "beans-consumer-" + UUID.randomUUID());
		String beans = kafkaProperties().getProperty("beans.topic");

		externalEventConsumer = new EventConsumer(kafkaProperties(), coffeeEvent -> {
			logger.info("firing internally: " + coffeeEvent);
			internalEventPublisher.publishEvent(coffeeEvent);
		}, beans);

		mes.execute(externalEventConsumer);
	}

	@PreDestroy
	public void close() {

		externalEventConsumer.stop();
	}

	private Properties kafkaProperties() {

		return kafkaConfigurator.getProperties();
	}

}
