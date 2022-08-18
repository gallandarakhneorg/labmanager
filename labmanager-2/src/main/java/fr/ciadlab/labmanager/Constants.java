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

package fr.ciadlab.labmanager;

/** Definition of global constants.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Constants {

	/** Default name of organization that is assumed by the author controller.
	 */
	public static final String DEFAULT_ORGANIZATION = "CIAD"; //$NON-NLS-1$

	/** Default name of the server to build the endpoints' URLs.
	 */
	public static final String DEFAULT_SERVER_NAME = "SpringRestHibernate"; //$NON-NLS-1$

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

}
