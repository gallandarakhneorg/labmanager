/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the CIAD laboratory and the Université de Technologie
 * de Belfort-Montbéliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD-UTBM.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

/** Tests for {@link AbstractCredentialController}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class AbstractCredentialControllerTest {

	private String hash;
	
	private String iv;
	
	private AbstractCredentialController test;

	@BeforeEach
	public void setUp() {
	    this.iv = "4f76476692be7171";
	    this.hash = "76c3299759c8512c";
		MessageSourceAccessor messages = mock(MessageSourceAccessor.class);
		this.test = new AbstractCredentialController(messages, new Constants(), this.hash, this.iv) {
			@Override
			public String readCredentials(String username, String serviceName, Object... serviceParams) {
				return super.readCredentials(username, serviceName, serviceParams);
			}
		};
	}

	@Test
	public void readCredentials() throws Exception {
		final String input = "neydSOpJJYhRE3EbFZgWEw==:NGY3NjQ3NjY5MmJlNzE3MQ==";
		final String username = this.test.readCredentials(input, "myserv");
		assertEquals("mylogin", username);
	}

}
