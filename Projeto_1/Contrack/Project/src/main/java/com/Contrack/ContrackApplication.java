package com.Contrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ContrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContrackApplication.class, args);
	}

}
