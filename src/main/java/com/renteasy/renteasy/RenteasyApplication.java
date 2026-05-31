package com.renteasy.renteasy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RenteasyApplication {

	public static void main(String[] args) {
		SpringApplication.run(RenteasyApplication.class, args);
	}

}
