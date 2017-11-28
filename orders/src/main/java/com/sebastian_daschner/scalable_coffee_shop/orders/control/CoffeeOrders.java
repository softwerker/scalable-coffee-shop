package com.sebastian_daschner.scalable_coffee_shop.orders.control;

import com.sebastian_daschner.scalable_coffee_shop.events.entity.OrderAccepted;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.OrderCancelled;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.OrderDelivered;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.OrderFinished;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.OrderPlaced;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.OrderStarted;
import com.sebastian_daschner.scalable_coffee_shop.orders.entity.CoffeeOrder;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Component
public class CoffeeOrders {

	private Map<UUID, CoffeeOrder> coffeeOrders = new ConcurrentHashMap<>();

	public CoffeeOrder get(final UUID orderId) {

		return coffeeOrders.get(orderId);
	}

	@EventListener
	public void onApplicationEvent(final OrderPlaced event) {

		coffeeOrders.putIfAbsent(event.getOrderInfo().getOrderId(), new CoffeeOrder());
		applyFor(event.getOrderInfo().getOrderId(), o -> o.place(event.getOrderInfo()));
	}

	@EventListener
	public void onApplicationEvent(final OrderCancelled event) {

		applyFor(event.getOrderId(), CoffeeOrder::cancel);
	}

	@EventListener
	public void onApplicationEvent(final OrderAccepted event) {

		applyFor(event.getOrderInfo().getOrderId(), CoffeeOrder::accept);
	}

	@EventListener
	public void onApplicationEvent(final OrderStarted event) {

		applyFor(event.getOrderId(), CoffeeOrder::start);
	}

	@EventListener
	public void onApplicationEvent(final OrderFinished event) {

		applyFor(event.getOrderId(), CoffeeOrder::finish);
	}

	@EventListener
	public void onApplicationEvent(final OrderDelivered event) {

		applyFor(event.getOrderId(), CoffeeOrder::deliver);
	}

	private void applyFor(final UUID orderId, final Consumer<CoffeeOrder> consumer) {

		final CoffeeOrder coffeeOrder = coffeeOrders.get(orderId);
		if (coffeeOrder != null) {
			consumer.accept(coffeeOrder);
		}
	}

}
