package com.github.miles.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 银行交易示例应用
 * 
 * @author miles.zeng
 * @since 2025-03-12
 */
@SpringBootApplication
@EnableCaching
public class BankTransactionDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankTransactionDemoApplication.class, args);
	}

}
