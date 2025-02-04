package com.example.aquariux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AquariuxApplication {

	public static void main(String[] args) {
		SpringApplication.run(AquariuxApplication.class, args);
	}

}
