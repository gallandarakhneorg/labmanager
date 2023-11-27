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

import fr.ciadlab.labmanager.io.ExporterConfigurator;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.springframework.beans.factory.annotation.Autowired;

/** Utilities for exporting an activity report based on RIPEC-C3 standard to Open Document Text.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.7
 */
public class DefaultOdfToolkitOpenDocumentTextRipecExporter extends AbstractOdfToolkitOpenDocumentTextRipecExporter {

	/** Construct the exporter using the given text document helper.
	 *
	 * @param textHelper the helper for the ODF text documents.
	 */
	public DefaultOdfToolkitOpenDocumentTextRipecExporter(@Autowired OdfTextDocumentHelper textHelper) {
		super(textHelper);
	}

	@Override
	protected void exportRipecC3(OdfTextDocument receiver, ExporterConfigurator configurator) throws Exception {
		//
	}

}
