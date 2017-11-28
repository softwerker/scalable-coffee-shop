package com.sebastian_daschner.scalable_coffee_shop.barista.control;

import com.sebastian_daschner.scalable_coffee_shop.events.entity.CoffeeBrewFinished;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.CoffeeBrewStarted;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.CoffeeDelivered;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;

import static java.util.Collections.unmodifiableCollection;

@Component
//@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class CoffeeBrews {

	private final Set<UUID> unfinishedBrews = new ConcurrentSkipListSet<>();

	private final Set<UUID> undeliveredOrders = new ConcurrentSkipListSet<>();

	public Collection<UUID> getUnfinishedBrews() {

		return unmodifiableCollection(unfinishedBrews);
	}

	public Collection<UUID> getUndeliveredOrders() {

		return unmodifiableCollection(undeliveredOrders);
	}

	@EventListener
	public void onApplicationEvent(final CoffeeBrewStarted event) {

		unfinishedBrews.add(event.getOrderInfo().getOrderId());
	}

	@EventListener
	public void onApplicationEvent(final CoffeeBrewFinished event) {

		final Iterator<UUID> iterator = unfinishedBrews.iterator();
		while (iterator.hasNext()) {
			final UUID orderId = iterator.next();
			if (orderId.equals(event.getOrderId())) {
				iterator.remove();
				undeliveredOrders.add(orderId);
			}
		}
	}

	@EventListener
	public void onApplicationEvent(final CoffeeDelivered event) {

		undeliveredOrders.removeIf(i -> i.equals(event.getOrderId()));
	}

}
