package com.sebastian_daschner.scalable_coffee_shop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableScheduling
public class BaristaApplication implements CommandLineRunner {

	public static void main(String[] args) throws Exception {

		new SpringApplication(BaristaApplication.class).run(args);
	}

	@Override
	public void run(String... arg0) throws Exception {

		if (arg0.length > 0 && arg0[0].equals("exitcode")) {
			throw new BaristaApplication.ExitException();
		}
	}

	@Bean
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {

		final ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

		threadPoolTaskExecutor.setCorePoolSize(5);
		threadPoolTaskExecutor.setMaxPoolSize(50);
		threadPoolTaskExecutor.setQueueCapacity(100);

		return threadPoolTaskExecutor;
	}

	class ExitException extends RuntimeException implements ExitCodeGenerator {

		private static final long serialVersionUID = 1L;

		@Override
		public int getExitCode() {

			return 10;
		}

	}

}
