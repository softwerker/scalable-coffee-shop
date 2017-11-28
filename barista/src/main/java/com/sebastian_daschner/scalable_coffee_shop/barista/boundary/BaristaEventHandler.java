package com.sebastian_daschner.scalable_coffee_shop.barista.boundary;

import com.sebastian_daschner.scalable_coffee_shop.events.control.EventConsumer;
import com.sebastian_daschner.scalable_coffee_shop.events.control.KafkaConfigurator;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.OrderAccepted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Properties;
import java.util.logging.Logger;

@Component
public class BaristaEventHandler {

	private EventConsumer externalEventConsumer;

	// TODO
	@Autowired
	private ThreadPoolTaskExecutor mes;

	@Autowired
	private KafkaConfigurator kafkaConfigurator;

	@Autowired
	private ApplicationEventPublisher internalEventPublisher;

	@Autowired
	BaristaCommandService baristaService;

	private static final Logger logger = Logger.getLogger(BaristaEventHandler.class.getName());

	@EventListener
	public void onApplicationEvent(final OrderAccepted event) {

		baristaService.makeCoffee(event.getOrderInfo());
	}

	@PostConstruct
	private void initConsumer() {

		kafkaProperties().put("group.id", "barista-handler");
		String orders = kafkaProperties().getProperty("orders.topic");

		externalEventConsumer = new EventConsumer(kafkaProperties(), coffeeEvent -> {
			logger.info("firing = " + coffeeEvent);
			internalEventPublisher.publishEvent(coffeeEvent);
		}, orders);

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
