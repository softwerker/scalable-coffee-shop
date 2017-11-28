package com.sebastian_daschner.scalable_coffee_shop.barista.boundary;

import com.sebastian_daschner.scalable_coffee_shop.barista.control.CoffeeBrews;
import com.sebastian_daschner.scalable_coffee_shop.events.control.EventProducer;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.CoffeeBrewFinished;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.CoffeeBrewStarted;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.CoffeeDelivered;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

@Component
public class BaristaCommandService {

	@Autowired
	EventProducer externalEventProducer;

	@Autowired
	CoffeeBrews coffeeBrews;

	private static final Logger logger = Logger.getLogger(BaristaCommandService.class.getName());

	void makeCoffee(final OrderInfo orderInfo) {

		externalEventProducer.publish(new CoffeeBrewStarted(orderInfo));
	}

	void checkCoffee() {

		final Collection<UUID> unfinishedBrews = coffeeBrews.getUnfinishedBrews();
		logger.info("checking " + unfinishedBrews.size() + " unfinished brews");
		unfinishedBrews.forEach(i -> {
			if (new Random().nextBoolean()) {
				externalEventProducer.publish(new CoffeeBrewFinished(i));
			}
		});
	}

	void checkCustomerDelivery() {

		final Collection<UUID> undeliveredOrder = coffeeBrews.getUndeliveredOrders();
		logger.info("checking " + undeliveredOrder.size() + " un-served orders");
		undeliveredOrder.forEach(i -> {
			if (new Random().nextBoolean()) {
				externalEventProducer.publish(new CoffeeDelivered(i));
			}
		});
	}

}
