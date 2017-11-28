package com.sebastian_daschner.scalable_coffee_shop.beans.boundary;

import com.sebastian_daschner.scalable_coffee_shop.beans.control.BeanStorage;
import com.sebastian_daschner.scalable_coffee_shop.events.control.EventProducer;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.BeansFetched;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.BeansStored;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.OrderBeansValidated;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.OrderFailedBeansNotAvailable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BeanCommandService {

	@Autowired
	EventProducer externalEventProducer;

	@Autowired
	BeanStorage beanStorage;

	public void storeBeans(final String beanOrigin, final int amount) {

		externalEventProducer.publish(new BeansStored(beanOrigin, amount));
	}

	void validateBeans(final String beanOrigin, final UUID orderId) {

		if (beanStorage.getRemainingAmount(beanOrigin) > 0) {
			externalEventProducer.publish(new OrderBeansValidated(orderId));
		} else {
			externalEventProducer.publish(new OrderFailedBeansNotAvailable(orderId));
		}
	}

	void fetchBeans(final String beanOrigin) {

		externalEventProducer.publish(new BeansFetched(beanOrigin));
	}

}
