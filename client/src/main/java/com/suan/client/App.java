package com.suan.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClientException;

@SpringBootApplication
public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Bean
	public ApplicationRunner appRunner(ServerApiClient serverApiClient) {
        return args -> {
            try {
                var healthCheckResponse = serverApiClient.healthCheck();
                LOGGER.info("Server responded: {}", healthCheckResponse);
            } catch (WebClientException ex) {
                LOGGER.error("Error checking server health", ex);
                System.exit(-1);
            }
        };
    }

}
