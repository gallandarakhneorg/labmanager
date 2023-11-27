/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
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

package fr.ciadlab.labmanager.utils.youtube;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

/** Utilities for YouTube.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Component
@Primary
public class DefaultYouTubeTools implements YouTubeTools {

	private static final String PROTOCOL = "https"; //$NON-NLS-1$

	private static final String EMBEDDED_HOST = "www.youtube.com"; //$NON-NLS-1$

	private static final String PATH_PREFIX = "embed"; //$NON-NLS-1$

	private static final String PARAM_NAME = "feature"; //$NON-NLS-1$

	private static final String PARAM_VALUE = "oembed"; //$NON-NLS-1$

	private static final Set<String> HOSTS = new HashSet<>();
	
	static {
		HOSTS.add("youtu.be"); //$NON-NLS-1$
		HOSTS.add("www.youtu.be"); //$NON-NLS-1$
		HOSTS.add("youtube.com"); //$NON-NLS-1$
		HOSTS.add(EMBEDDED_HOST);
		HOSTS.add("youtube.fr"); //$NON-NLS-1$
		HOSTS.add("www.youtube.fr"); //$NON-NLS-1$
	}

	private final UriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();

	private static String getVideoId(URL url) {
		final String host = url.getHost();
		if (HOSTS.contains(host)) {
			String path = url.getPath();
			path = path.replaceFirst("^/+", "").replaceAll("/+$", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			if (!Strings.isNullOrEmpty(path)) {
				return path;
			}
		}
		return null;
	}
	
	@Override
	public URL embeddedVideoLink(URL videoLink) {
		final String id = getVideoId(videoLink);
		if (!Strings.isNullOrEmpty(id)) {
			final UriBuilder builder = this.uriBuilderFactory.builder();
			builder.scheme(PROTOCOL).host(EMBEDDED_HOST);
			builder.pathSegment(PATH_PREFIX, id);
			builder.queryParam(PARAM_NAME, PARAM_VALUE);
			try {
				return builder.build().toURL();
			} catch (MalformedURLException e) {
				//
			}
		}
		return null;
	}

}
