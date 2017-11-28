package com.sebastian_daschner.scalable_coffee_shop.orders.boundary;

import com.sebastian_daschner.scalable_coffee_shop.events.control.EventConsumer;
import com.sebastian_daschner.scalable_coffee_shop.events.control.KafkaConfigurator;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.BeansStored;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.CoffeeBrewFinished;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.CoffeeBrewStarted;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.CoffeeDelivered;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.OrderBeansValidated;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.OrderFailedBeansNotAvailable;
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
public class OrderEventHandler {

	private EventConsumer externalEventConsumer;

	// TODO
	@Autowired
	private ThreadPoolTaskExecutor mes;

	@Autowired
	private KafkaConfigurator kafkaConfigurator;

	@Autowired
	private ApplicationEventPublisher internalEventPublisher;

	@Autowired
	OrderCommandService orderService;

	private static final Logger logger = Logger.getLogger(OrderEventHandler.class.getName());

	@EventListener
	public void onApplicationEvent(final OrderBeansValidated event) {

		orderService.acceptOrder(event.getOrderId());
	}

	@EventListener
	public void onApplicationEvent(final OrderFailedBeansNotAvailable event) {

		orderService.cancelOrder(event.getOrderId(), "No beans of the origin were available");
	}

	@EventListener
	public void onApplicationEvent(final CoffeeBrewStarted event) {

		orderService.startOrder(event.getOrderInfo().getOrderId());
	}

	@EventListener
	public void onApplicationEvent(final CoffeeBrewFinished event) {

		orderService.finishOrder(event.getOrderId());
	}

	@EventListener
	public void onApplicationEvent(final CoffeeDelivered event) {

		orderService.deliverOrder(event.getOrderId());
	}

	@PostConstruct
	private void initConsumer() {

		kafkaProperties().put("group.id", "order-handler");
		String barista = kafkaProperties().getProperty("barista.topic");
		String beans = kafkaProperties().getProperty("beans.topic");

		externalEventConsumer = new EventConsumer(kafkaProperties(), coffeeEvent -> {
			logger.info("firing internally: " + coffeeEvent);
			internalEventPublisher.publishEvent(coffeeEvent);
		}, barista, beans);

		mes.execute(externalEventConsumer);
	}

	@EventListener
	public void onApplicationEvent(final BeansStored beansStored) {

		logger.info(beansStored.toString());
	}


	@PreDestroy
	public void closeConsumer() {

		externalEventConsumer.stop();
	}

	private Properties kafkaProperties() {

		return kafkaConfigurator.getProperties();
	}

}
