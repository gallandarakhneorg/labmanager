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

	/** Default basename of the files in attachment.
	 */
	public static final String DEFAULT_ATTACHMENT_BASENAME = "publications"; //$NON-NLS-1$

}
