package com.sebastian_daschner.scalable_coffee_shop.orders.boundary;

import com.sebastian_daschner.scalable_coffee_shop.events.control.EventProducer;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.OrderAccepted;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.OrderCancelled;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.OrderDelivered;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.OrderFinished;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.OrderInfo;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.OrderPlaced;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.OrderStarted;
import com.sebastian_daschner.scalable_coffee_shop.orders.control.CoffeeOrders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderCommandService {

	@Autowired
	EventProducer externalEventProducer;

	@Autowired
	CoffeeOrders coffeeOrders;

	public void placeOrder(final OrderInfo orderInfo) {

		externalEventProducer.publish(new OrderPlaced(orderInfo));
	}

	void acceptOrder(final UUID orderId) {

		final OrderInfo orderInfo = coffeeOrders.get(orderId).getOrderInfo();
		externalEventProducer.publish(new OrderAccepted(orderInfo));
	}

	void cancelOrder(final UUID orderId, final String reason) {

		externalEventProducer.publish(new OrderCancelled(orderId, reason));
	}

	void startOrder(final UUID orderId) {

		externalEventProducer.publish(new OrderStarted(orderId));
	}

	void finishOrder(final UUID orderId) {

		externalEventProducer.publish(new OrderFinished(orderId));
	}

	void deliverOrder(final UUID orderId) {

		externalEventProducer.publish(new OrderDelivered(orderId));
	}

}
