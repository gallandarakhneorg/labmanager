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

package fr.ciadlab.labmanager.entities.member;

import java.net.MalformedURLException;
import java.net.URI;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

/** Type of webpage naming. This type deescribes how the address of a person's webpage could be built up.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public enum WebPageNaming {
	/** Naming convention not specified, i.e., no web page.
	 */
	UNSPECIFIED {
		@Override
		public URI getWebpageURIFor(Person person) {
			return null;
		}
	},

	/** The URL of the person's webpage is: {@code author-<ID>}.
	 */
	AUTHOR_ID {
		@Override
		public URI getWebpageURIFor(Person person) {
			UriBuilder b = FACTORY.builder();
			b = b.path("/author-" + person.getId()); //$NON-NLS-1$
			return b.build();
		}
	},

	/** The URL of the person's webpage is: {@code <EMAIL_ID>} where the email of the person is defined by {@code <EMAIL_ID>@<DOMAIN>}.
	 */
	EMAIL_ID {
		@Override
		public URI getWebpageURIFor(Person person) {
			final String email = person.getEmail();
			final String id = StringUtils.substringBefore(email, "@"); //$NON-NLS-1$
			if (Strings.isNullOrEmpty(id)) {
				throw new IllegalStateException("Cannot build an URI from email for: " + person); //$NON-NLS-1$
			}
			UriBuilder b = FACTORY.builder();
			b = b.path("/" + id); //$NON-NLS-1$
			return b.build();
		}
	},

	/** The URL of the person's webpage is: {@code <FIRST_NAME>_<LAST_NAME>}.
	 * Accents are removed, and characters that are not an ASCII letter, digit, or one of
	 * {@code _-.} are stripped.
	 */
	FIRST_LAST {
		@Override
		public URI getWebpageURIFor(Person person) {
			UriBuilder b = FACTORY.builder();
			final String first = StringUtils.stripAccents(person.getFirstName());
			final String last = StringUtils.stripAccents(person.getLastName());
			final String ref;
			if (Strings.isNullOrEmpty(last)) {
				ref = first.toLowerCase();
			} else if (Strings.isNullOrEmpty(first)) {
				ref = last.toLowerCase();
			} else {
				ref = first.toLowerCase() + "_" + last.toLowerCase(); //$NON-NLS-1$
			}
			return b.path("/" + ref.replaceAll("[^a-z0-9_\\-\\.]+", "")).build(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	};

	private static final UriBuilderFactory FACTORY = new DefaultUriBuilderFactory();

	/** Replies the URI of the webpage for the given person.
	 *
	 * @param person the person.
	 * @return the URI.
	 * @throws MalformedURLException if the URL is malformed. This problem should never appear.
	 */
	public abstract URI getWebpageURIFor(Person person);

	/** Replies the naming that corresponds to the given name, with a case-insensitive
	 * test of the name.
	 *
	 * @param name the name of the naming, to search for.
	 * @return the naming.
	 * @throws IllegalArgumentException if the given name does not corresponds to a naming.
	 */
	public static WebPageNaming valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final WebPageNaming status : values()) {
				if (name.equalsIgnoreCase(status.name())) {
					return status;
				}
			}
		}
		throw new IllegalArgumentException("Invalid webpage naming: " + name); //$NON-NLS-1$
	}

}
