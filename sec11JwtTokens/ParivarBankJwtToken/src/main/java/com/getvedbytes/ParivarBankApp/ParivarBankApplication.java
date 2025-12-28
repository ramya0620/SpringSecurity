package com.getvedbytes.ParivarBankApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class ParivarBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParivarBankApplication.class, args);
	}

}

