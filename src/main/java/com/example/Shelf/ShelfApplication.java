package com.example.Shelf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShelfApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShelfApplication.class, args);
	}

}
