package com.sebastian_daschner.scalable_coffee_shop.beans.boundary;

import com.sebastian_daschner.scalable_coffee_shop.events.control.EventConsumer;
import com.sebastian_daschner.scalable_coffee_shop.events.control.KafkaConfigurator;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.CoffeeBrewStarted;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.OrderPlaced;
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
public class BeanEventHandler {

	private EventConsumer externalEventConsumer;

	// TODO
	@Autowired
	private ThreadPoolTaskExecutor mes;

	@Autowired
	private KafkaConfigurator kafkaConfigurator;

	@Autowired
	private ApplicationEventPublisher internalEventPublisher;

	@Autowired
	BeanCommandService beanService;

	private static final Logger logger = Logger.getLogger(BeanEventHandler.class.getName());

	@EventListener
	public void onApplicationEvent(final OrderPlaced event) {

		beanService.validateBeans(event.getOrderInfo().getBeanOrigin(), event.getOrderInfo().getOrderId());
	}

	@EventListener
	public void onApplicationEvent(final CoffeeBrewStarted event) {

		beanService.fetchBeans(event.getOrderInfo().getBeanOrigin());
	}

	@PostConstruct
	private void initConsumer() {

		kafkaProperties().put("group.id", "beans-handler");
		String orders = kafkaProperties().getProperty("orders.topic");
		String barista = kafkaProperties().getProperty("barista.topic");

		externalEventConsumer = new EventConsumer(kafkaProperties(), coffeeEvent -> {
			logger.info("firing internally: " + coffeeEvent);
			internalEventPublisher.publishEvent(coffeeEvent);
		}, orders, barista);

		mes.execute(externalEventConsumer);
	}

	@PreDestroy
	public void closeConsumer() {

		externalEventConsumer.stop();
	}

	private Properties kafkaProperties() {

		return kafkaConfigurator.getProperties();
	}

}
