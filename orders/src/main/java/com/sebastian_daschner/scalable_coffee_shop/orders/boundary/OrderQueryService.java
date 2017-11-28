package com.sebastian_daschner.scalable_coffee_shop.orders.boundary;

import com.sebastian_daschner.scalable_coffee_shop.orders.control.CoffeeOrders;
import com.sebastian_daschner.scalable_coffee_shop.orders.entity.CoffeeOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderQueryService {

	@Autowired
	CoffeeOrders coffeeOrders;

	public CoffeeOrder getOrder(final UUID orderId) {

		return coffeeOrders.get(orderId);
	}

}
