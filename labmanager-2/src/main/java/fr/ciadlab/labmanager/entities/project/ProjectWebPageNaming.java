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

package fr.ciadlab.labmanager.entities.project;

import java.net.URI;
import java.util.Locale;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.BaseMessageSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

/** Type of webpage naming for projects. This type describes how the address of a project's webpage could be built up.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.0
 */
public enum ProjectWebPageNaming {
	/** Naming convention not specified, i.e., no web page.
	 */
	UNSPECIFIED {
		@Override
		public String getWebpageIdFor(Project project) {
			return null;
		}
	},

	/** The URL of the project's webpage is: {@code project-<ID>}.
	 */
	PROJECT_ID {
		@Override
		public String getWebpageIdFor(Project project) {
			final int id = project != null ? project.getId() : 0;
			if (id != 0) {
				return "project-" + id; //$NON-NLS-1$
			}
			return null;
		}
	},

	/** The URL of the project's webpage is: {@code <ACRONYM>}.
	 * Accents are removed, and characters that are not an ASCII letter, digit, or one of
	 * {@code _-.} are stripped.
	 */
	ACRONYM {
		@Override
		public String getWebpageIdFor(Project project) {
			if (project != null) {
				String acronym = StringUtils.stripAccents(project.getAcronym());
				if (!Strings.isNullOrEmpty(acronym)) {
					acronym = acronym.toLowerCase();
					return acronym.replaceAll("[^a-z0-9_\\-\\.]+", ""); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
			return null;
		}
	};

	private static final UriBuilderFactory FACTORY = new DefaultUriBuilderFactory();

	private static final String MESSAGE_PREFIX = "projectwebpagenaming."; //$NON-NLS-1$

	private MessageSourceAccessor messages;
	
	/** Replies the message accessor to be used.
	 *
	 * @return the accessor.
	 */
	public MessageSourceAccessor getMessageSourceAccessor() {
		if (this.messages == null) {
			this.messages = BaseMessageSource.getStaticMessageSourceAccessor();
		}
		return this.messages;
	}

	/** Change the message accessor to be used.
	 *
	 * @param messages the accessor.
	 */
	public void setMessageSourceAccessor(MessageSourceAccessor messages) {
		this.messages = messages;
	}

	/** Replies the label of the status in the current language.
	 *
	 * @return the label of the status in the current language.
	 */
	public String getLabel() {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name());
		return Strings.nullToEmpty(label);
	}

	/** Replies the label of the status in the current language.
	 *
	 * @param locale the locale to use.
	 * @return the label of the status in the current language.
	 */
	public String getLabel(Locale locale) {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name(), locale);
		return Strings.nullToEmpty(label);
	}

	/** Replies the URI of the webpage for the given project.
	 *
	 * @param project the project.
	 * @return the URI, or {@code null}.
	 */
	public URI getWebpageURIFor(Project project) {
		final String id = getWebpageIdFor(project);
		if (Strings.isNullOrEmpty(id)) {
			return null;
		}
		try {
			UriBuilder b = FACTORY.builder();
			b = b.path("/" + id); //$NON-NLS-1$
			return b.build();
		} catch (Throwable ex) {
			return null;
		}
	}

	/** Replies the identifier of the webpage for the given project.
	 *
	 * @param project the project.
	 * @return the identifier.
	 */
	public abstract String getWebpageIdFor(Project project);

	/** Replies the naming that corresponds to the given name, with a case-insensitive
	 * test of the name.
	 *
	 * @param name the name of the naming, to search for.
	 * @return the naming.
	 * @throws IllegalArgumentException if the given name does not corresponds to a naming.
	 */
	public static ProjectWebPageNaming valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final ProjectWebPageNaming status : values()) {
				if (name.equalsIgnoreCase(status.name())) {
					return status;
				}
			}
		}
		throw new IllegalArgumentException("Invalid webpage naming: " + name); //$NON-NLS-1$
	}

}
