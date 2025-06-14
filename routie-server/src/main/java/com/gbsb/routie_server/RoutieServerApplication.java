package com.gbsb.routie_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RoutieServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoutieServerApplication.class, args);
	}

}
