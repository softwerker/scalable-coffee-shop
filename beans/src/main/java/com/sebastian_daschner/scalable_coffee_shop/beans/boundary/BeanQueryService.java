package com.sebastian_daschner.scalable_coffee_shop.beans.boundary;

import com.sebastian_daschner.scalable_coffee_shop.beans.control.BeanStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BeanQueryService {

	@Autowired
	BeanStorage beanStorage;

	public Map<String, Integer> getStoredBeans() {

		return beanStorage.getStoredBeans();
	}

}
