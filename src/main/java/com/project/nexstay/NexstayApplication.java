package com.project.nexstay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class NexstayApplication {
	public static void main(String[] args) {
		SpringApplication.run(NexstayApplication.class, args);
	}
}
