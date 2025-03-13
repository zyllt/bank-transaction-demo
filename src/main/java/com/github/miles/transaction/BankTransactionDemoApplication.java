package com.github.miles.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main application class for the Bank Transaction Demo
 */
@SpringBootApplication
@EnableCaching
public class BankTransactionDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankTransactionDemoApplication.class, args);
	}

}
