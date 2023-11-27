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

package fr.ciadlab.labmanager.io.od;

import org.odftoolkit.odfdom.type.Color;
import org.springframework.http.MediaType;

/** Constants for Open Document standard.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public interface OpenDocumentConstants {

	/** Mime type for ODT.
	 */
	String ODT_MIME_TYPE_VALUE = "application/vnd.oasis.opendocument.text"; //$NON-NLS-1$

	/** Mime type for ODT.
	 */
	MediaType ODT_MIME_TYPE = MediaType.parseMediaType(ODT_MIME_TYPE_VALUE);

	/** Green color for CIAD lab.
	 */
	Color CIAD_GREEN = Color.valueOf("#95bc0f"); //$NON-NLS-1$

	/** Dark green color for CIAD lab.
	 */
	Color CIAD_DARK_GREEN = Color.valueOf("#4b5e08"); //$NON-NLS-1$

	/** Relative path to the ODT document that is a template for the RIPEC-C3 activity report.
	 *
	 * @since 3.7
	 */
	String ODT_REPEC_C3_TEMPLATE_PATH = "odt/ripec_c3_template.odt"; //$NON-NLS-1$

}
