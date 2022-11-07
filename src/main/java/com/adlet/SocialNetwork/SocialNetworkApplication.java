package com.adlet.SocialNetwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SocialNetworkApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SocialNetworkApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder createSpringApplicationBuilder() {

		return super.createSpringApplicationBuilder();
	}

	@Bean
	public SpringApplicationContext springApplicationContext() {

		return new SpringApplicationContext();
	}

}
