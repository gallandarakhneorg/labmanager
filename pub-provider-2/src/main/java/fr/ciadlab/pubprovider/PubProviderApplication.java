/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.pubprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;


@SpringBootApplication
@Configuration
@EnableSpringConfigured
public class PubProviderApplication extends SpringBootServletInitializer {

    //For localhost use:
    public static final String PROXYURL="proxy.utbm.fr";
    public static final int PROXYPORT=3128;
    public static final String DownloadablesPath="Downloadables/";
    //Also go rename the logback file to "something logback.xml something.txt"

    //For when deployed:
//    public static final String PROXYURL = "";
//    public static final int PROXYPORT = 0;
    //public static final String DownloadablesPath = "/var/www/ciad-lab.fr/Downloadables/";
    //Also go rename the logback file to logback.xml

    public static void main(String[] args) {
        SpringApplication.run(PubProviderApplication.class, args);
    }

}
