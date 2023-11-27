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

/** Utilities for exporting an activity report based on RIPEC standard to Open Document Text.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.7
 */
public interface OpenDocumentTextRipecExporter {

	/** Replies the ODT representation of the RIPEC-C3 document.
	 *
	 * @param configurator the configurator for the export, never {@code null}.
	 * @return the ODT representation of the report.
	 * @throws Exception if the report cannot be generated to ODT.
	 */
	byte[] exportRipecC3(ExporterConfigurator configurator) throws Exception;

}
