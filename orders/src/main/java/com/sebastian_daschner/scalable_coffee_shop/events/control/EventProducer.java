package com.sebastian_daschner.scalable_coffee_shop.events.control;

import com.sebastian_daschner.scalable_coffee_shop.events.entity.CoffeeEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

@Component
public class EventProducer {

	private Producer<String, CoffeeEvent> kafkaProducer;

	private String topic;

	@Autowired
	private KafkaConfigurator kafkaConfigurator;

	private static final Logger logger = Logger.getLogger(EventProducer.class.getName());

	@PostConstruct
	private void initProducer() {

		kafkaProperties().put("transactional.id", UUID.randomUUID().toString());
		kafkaProducer = new KafkaProducer<>(kafkaProperties());
		topic = kafkaProperties().getProperty("orders.topic");
		kafkaProducer.initTransactions();
	}

	public void publish(CoffeeEvent event) {

		final ProducerRecord<String, CoffeeEvent> record = new ProducerRecord<>(topic, event);
		try {
			kafkaProducer.beginTransaction();
			logger.info("publishing = " + record);
			kafkaProducer.send(record);
			kafkaProducer.commitTransaction();
		} catch (ProducerFencedException e) {
			kafkaProducer.close();
		} catch (KafkaException e) {
			kafkaProducer.abortTransaction();
		}
	}

	@PreDestroy
	public void close() {

		kafkaProducer.close();
	}

	private Properties kafkaProperties() {

		return kafkaConfigurator.getProperties();
	}
}

