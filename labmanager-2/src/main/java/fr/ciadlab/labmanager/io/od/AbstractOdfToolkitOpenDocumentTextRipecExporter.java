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

import java.io.ByteArrayOutputStream;

import fr.ciadlab.labmanager.io.ExporterConfigurator;
import org.odftoolkit.odfdom.doc.OdfTextDocument;

/** Utilities for exporting an activity report based on RIPEC-C3 standard to Open Document Text.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.7
 */
public abstract class AbstractOdfToolkitOpenDocumentTextRipecExporter implements OpenDocumentTextRipecExporter {

	/** Reference to the utility helper for ODF text documents. 
	 */
	protected final OdfTextDocumentHelper textHelper;

	private String odtTemplatePath = OpenDocumentConstants.ODT_REPEC_C3_TEMPLATE_PATH;

	/** Construct the exporter using the given text document helper.
	 *
	 * @param textHelper the helper for the ODF text documents.
	 */
	public AbstractOdfToolkitOpenDocumentTextRipecExporter(OdfTextDocumentHelper textHelper) {
		this.textHelper = textHelper;
	}

	/** Replies the path to the ODT template for the RIPEC-C3 activity report.
	 *
	 * @return the template path.
	 */
	public String getRipecC3TemplatePath() {
		return this.odtTemplatePath;
	}
	
	/** Change the path to the ODT template for the RIPEC-C3 activity report.
	 *
	 * @param path the template path.
	 */
	public void setRipecC3TemplatePath(String path) {
		this.odtTemplatePath = path;
	}

	@Override
	public byte[] exportRipecC3(ExporterConfigurator configurator) throws Exception {
		final byte[] content;
		try (final OdfTextDocument odt = this.textHelper.openOdtTemplate(getRipecC3TemplatePath())) {
			exportRipecC3(odt, configurator);
			try (final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
				odt.save(output);
				output.flush();
				content = output.toByteArray();
			}
		}
		return content;
	}

	/** Export the inputs in the given receiver using the RIPEC-C3 template.
	 *
	 * @param receiver the receiver.
	 * @param configurator the export configurator.
	 * @throws Exception if it is impossible to export.
	 */
	protected abstract void exportRipecC3(OdfTextDocument receiver, ExporterConfigurator configurator) throws Exception;

}
