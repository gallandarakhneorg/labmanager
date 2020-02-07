package com.spring.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
public class PubProviderApplication extends SpringBootServletInitializer {

	//For localhost use:
	public static final String PROXYURL="proxy.utbm.fr";
	public static final int PROXYPORT=3128;
	public static final String DownloadablesPath="Downloadables/";
	
	//For when deployed:
	//public static final String PROXYURL="";
	//public static final int PROXYPORT=0;
	//public static final String DownloadablesPath="/var/www/ciad-lab.fr/Downloadables/";

	public static void main(String[] args) {
		SpringApplication.run(PubProviderApplication.class, args);
	}

}
