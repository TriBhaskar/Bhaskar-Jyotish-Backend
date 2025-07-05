package com.anterka.bjyotish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BjyotishApplication {

	public static void main(String[] args) {
		SpringApplication.run(BjyotishApplication.class, args);
	}

}
