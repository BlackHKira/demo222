package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@SpringBootApplication
@RestController
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/hello")
	public Map<String, Object> hello() throws InterruptedException {
		Thread.sleep(80);
		return Map.of("message", "hello from spring boot java 21");
	}

	@GetMapping("/slow")
	public Map<String, Object> slow() throws InterruptedException {
		Thread.sleep(500);
		return Map.of("message", "slow endpoint");
	}

	@GetMapping("/health")
	public Map<String, Object> health() {
		return Map.of(
				"status", "UP",
				"time", LocalDateTime.now()
		);
	}
}