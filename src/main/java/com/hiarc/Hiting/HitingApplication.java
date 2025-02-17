package com.hiarc.Hiting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class HitingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HitingApplication.class, args);
	}

}
