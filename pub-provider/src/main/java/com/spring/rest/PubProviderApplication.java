package com.spring.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class PubProviderApplication {

	public static final String PROXYURL="proxy.utbm.fr";
	public static final int PROXYPORT=3128;

	public static void main(String[] args) {
		SpringApplication.run(PubProviderApplication.class, args);
	}

}
