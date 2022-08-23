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

import fr.ciadlab.labmanager.configuration.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

/** Tests for {@link AbstractController}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class AbstractControllerTest {

	@Test
	public void addUrlToPublicationListEndPoint() {
		final UriBuilderFactory factory = new DefaultUriBuilderFactory();
		
		final StringBuilder path = new StringBuilder();
		path.append("/").append(new Constants().getServerName()).append("/").append(Constants.EXPORT_JSON_ENDPOINT); //$NON-NLS-1$ //$NON-NLS-2$
		UriBuilder uriBuilder = factory.builder();
		uriBuilder = uriBuilder.path(path.toString());
		final String url = uriBuilder.build().toString();

		assertEquals("/SpringRestHibernate/exportJson", url);
	}
	
}
