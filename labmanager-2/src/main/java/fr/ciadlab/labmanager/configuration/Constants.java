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

package fr.ciadlab.labmanager.configuration;

import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** Definition of global constants. Some constants are obtaining from the Spring boot configuration file. That's why they
 * are not defined as global constants, by replied through static getter methods.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Component
public class Constants {

	/** Name of the endpoint for exporting to JSON.
	 */
	public static final String EXPORT_JSON_ENDPOINT = "exportJson"; //$NON-NLS-1$

	/** Name of the endpoint for exporting to BibTeX.
	 */
	public static final String EXPORT_BIBTEX_ENDPOINT = "exportBibTeX"; //$NON-NLS-1$

	/** Name of the endpoint for exporting to OpenDocument Text.
	 */
	public static final String EXPORT_ODT_ENDPOINT = "exportOdt"; //$NON-NLS-1$

	/** Name of the endpoint for exporting to HTML document.
	 */
	public static final String EXPORT_HTML_ENDPOINT = "exportHtml"; //$NON-NLS-1$

	/** Name of the endpoint parameter "id".
	 */
	public static final String ID_ENDPOINT_PARAMETER = "id"; //$NON-NLS-1$

	/** Name of the endpoint parameter "forAjax".
	 */
	public static final String FORAJAX_ENDPOINT_PARAMETER = "forAjax"; //$NON-NLS-1$

	/** Name of the endpoint parameter "organization".
	 */
	public static final String ORGANIZATION_ENDPOINT_PARAMETER = "organization"; //$NON-NLS-1$

	/** Name of the endpoint parameter "author".
	 */
	public static final String AUTHOR_ENDPOINT_PARAMETER = "author"; //$NON-NLS-1$

	/** Name of the endpoint parameter "journal".
	 */
	public static final String JOURNAL_ENDPOINT_PARAMETER = "journal"; //$NON-NLS-1$

	/** Name of the endpoint parameter "inAttachment".
	 */
	public static final String INATTACHMENT_ENDPOINT_PARAMETER = "inAttachment"; //$NON-NLS-1$

	/** Name of the endpoint parameter "success".
	 */
	public static final String SUCCESS_ENDPOINT_PARAMETER = "success"; //$NON-NLS-1$

	/** Name of the endpoint parameter "failure".
	 */
	public static final String FAILURE_ENDPOINT_PARAMETER = "failure"; //$NON-NLS-1$

	/** Name of the endpoint parameter "message".
	 */
	public static final String MESSAGE_ENDPOINT_PARAMETER = "message"; //$NON-NLS-1$

	/** Default basename of the files in attachment.
	 */
	public static final String DEFAULT_ATTACHMENT_BASENAME = "publications"; //$NON-NLS-1$

	/** Name of the endpoint for editing an organization.
	 *
	 * @see #ORGANIZATION_SAVING_ENDPOINT
	 */
	public static final String ORGANIZATION_EDITING_ENDPOINT = "organizationEditor"; //$NON-NLS-1$

	/** Name of the endpoint for saving an organization.
	 *
	 * @see #ORGANIZATION_EDITING_ENDPOINT
	 */
	public static final String ORGANIZATION_SAVING_ENDPOINT = "organizationSave"; //$NON-NLS-1$

	/** Name of the endpoint for editing a person.
	 *
	 * @see #PERSON_SAVING_ENDPOINT
	 */
	public static final String PERSON_EDITING_ENDPOINT = "personEditor"; //$NON-NLS-1$

	/** Name of the endpoint for saving a person.
	 *
	 * @see #PERSON_EDITING_ENDPOINT
	 */
	public static final String PERSON_SAVING_ENDPOINT = "personSave"; //$NON-NLS-1$

	/** Name of the endpoint for editing a journal.
	 *
	 * @see #JOURNAL_SAVING_ENDPOINT
	 */
	public static final String JOURNAL_EDITING_ENDPOINT = "journalEditor"; //$NON-NLS-1$

	/** Name of the endpoint for saving a journal.
	 *
	 * @see #JOURNAL_EDITING_ENDPOINT
	 */
	public static final String JOURNAL_SAVING_ENDPOINT = "journalSave"; //$NON-NLS-1$

	/** Name of the endpoint for editing a publication.
	 *
	 * @see #PUBLICATION_SAVING_ENDPOINT
	 */
	public static final String PUBLICATION_EDITING_ENDPOINT = "publicationEditor"; //$NON-NLS-1$

	/** Name of the endpoint for saving a publication.
	 *
	 * @see #PUBLICATION_EDITING_ENDPOINT
	 */
	public static final String PUBLICATION_SAVING_ENDPOINT = "publicationSave"; //$NON-NLS-1$

	/** Name of the endpoint for selecting a BibTeX file to import.
	 */
	public static final String IMPORT_BIBTEX_VIEW_ENDPOINT = "showBibTeXImporter"; //$NON-NLS-1$

	/** Name of the endpoint for reading a BibTeX file and providing a JSON description.
	 */
	public static final String GET_JSON_FROM_BIBTEX_ENDPOINT = "getJsonFromBibTeX"; //$NON-NLS-1$

	/** Name of the endpoint for saving a BibTeX file and providing a JSON description.
	 */
	public static final String SAVE_BIBTEX_ENDPOINT = "saveBibTeX"; //$NON-NLS-1$

	private static final String DEFAULT_SERVER_NAME = "LabManagerApi"; //$NON-NLS-1$

	private static final String DEFAULT_ORGANIZATION = "ResearchOrganization"; //$NON-NLS-1$

	@Value("${server.servlet.context-path}")
	private String serverName;

	@Value("${labmanager.default-organization}")
	private String defaultOganization;

	/** Replies the name of the Spring boot server that is used for accessing the end-points and online resources.
	 *
	 * @return the name of the Spring Boot application
	 */
	public String getServerName() {
		if (Strings.isNullOrEmpty(this.serverName)) {
			this.serverName = DEFAULT_SERVER_NAME;
		} else {
			while (this.serverName.startsWith("/")) { //$NON-NLS-1$
				this.serverName = this.serverName.substring(1);
			}
		}
		return this.serverName;
	}

	/** Replies the name or acronym of the default organization to consider.
	 *
	 * @return the name or acronym of the default organization
	 */
	public String getDefaultOrganization() {
		if (Strings.isNullOrEmpty(this.defaultOganization)) {
			this.serverName = DEFAULT_ORGANIZATION;
		}
		return this.defaultOganization;
	}

}
