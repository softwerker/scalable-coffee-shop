package com.sebastian_daschner.scalable_coffee_shop.events.control;

import com.google.common.collect.ImmutableMap;
import com.sebastian_daschner.scalable_coffee_shop.events.entity.CoffeeEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;

public class EventConsumer implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

	private final KafkaConsumer<String, CoffeeEvent> kafkaConsumer;

	private final Consumer<CoffeeEvent> coffeeEventConsumer;

	private final AtomicBoolean closed = new AtomicBoolean();

	private final List<String> topics;

	public EventConsumer(Properties kafkaProperties, Consumer<CoffeeEvent> coffeeEventConsumer, String... topics) {

		this.coffeeEventConsumer = coffeeEventConsumer;
		kafkaConsumer = new KafkaConsumer<>(kafkaProperties);
		this.topics = asList(topics);
		kafkaConsumer.subscribe(this.topics);
	}

	@Override
	public void run() {

		try {
			while (!closed.get()) {
				consume();
			}
		} catch (WakeupException e) {
			// will wakeup for closing
		} finally {
			kafkaConsumer.close();
		}
	}

	private void consume() {

		ConsumerRecords<String, CoffeeEvent> records = kafkaConsumer.poll(Long.MAX_VALUE);
		logger.info("received records in one of topics {}: {}", topics, readable(records));
		for (ConsumerRecord<String, CoffeeEvent> record : records) {
			coffeeEventConsumer.accept(record.value());
		}
		kafkaConsumer.commitSync();
	}

	public void stop() {

		closed.set(true);
		kafkaConsumer.wakeup();
	}

	private Object readable(final ConsumerRecords<String, CoffeeEvent> records) {

		return topics.stream()
				.flatMap(topic -> StreamSupport.stream(records.records(topic).spliterator(), false))
				.map(record -> ImmutableMap.of(
						"partition", record.partition(),
						"topic", record.topic(),
						"coffeeEvent", record.value().getClass().getSimpleName()
				))
				.collect(Collectors.toList());
	}

}
