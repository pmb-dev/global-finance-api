package com.github.pmbdev.global_finance_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching // Cache support
public class GlobalFinanceApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(GlobalFinanceApiApplication.class, args);
	}
}
