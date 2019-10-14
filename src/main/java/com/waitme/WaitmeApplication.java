package com.waitme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class WaitmeApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(WaitmeApplication.class, args);
	}

}
