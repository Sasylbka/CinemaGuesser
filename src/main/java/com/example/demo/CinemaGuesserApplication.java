package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

@ImportResource("classpath*:config.xml")
@SpringBootApplication
public class CinemaGuesserApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(CinemaGuesserApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(CinemaGuesserApplication.class);
	}
}
