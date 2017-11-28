package com.sebastian_daschner.scalable_coffee_shop.beans.control;

import com.sebastian_daschner.scalable_coffee_shop.events.entity.BeansFetched;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.BeansStored;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
//@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class BeanStorage {

	private Map<String, Integer> beanOrigins = new ConcurrentHashMap<>();

	public Map<String, Integer> getStoredBeans() {

		return Collections.unmodifiableMap(beanOrigins);
	}

	public int getRemainingAmount(final String beanOrigin) {

		return beanOrigins.getOrDefault(beanOrigin, 0);
	}

	@EventListener
	public void onApplicationEvent(final BeansStored beansStored) {

		beanOrigins.merge(beansStored.getBeanOrigin(), beansStored.getAmount(), Math::addExact);
	}

	@EventListener
	public void onApplicationEvent(final BeansFetched beansFetched) {

		beanOrigins.merge(beansFetched.getBeanOrigin(), 0, (i1, i2) -> i1 - 1);
	}

}
