package com.sebastian_daschner.scalable_coffee_shop.barista.boundary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BaristaTimer {

	@Autowired
	BaristaCommandService baristaService;

	//@Scheduled(cron = "7/7 * * * * *")
	@Scheduled(initialDelay = 7000, fixedDelay = 7000)
	void checkCoffee() {

		baristaService.checkCoffee();
	}

	//@Scheduled(cron = "8/8 * * * * *")
	@Scheduled(initialDelay = 8000, fixedDelay = 8000)
	void checkCustomerDelivery() {

		baristaService.checkCustomerDelivery();
	}

}
