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

package fr.ciadlab.labmanager.io.json;

import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import fr.ciadlab.labmanager.io.PublicationExporter;

/** Utilities for exporting publications to JSON.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public interface JsonExporter extends PublicationExporter<String> {

	@Override
	default String exportPublications(Iterable<? extends Publication> publications, ExporterConfigurator configurator) throws Exception {
		return exportPublicationsWithRootKeys(publications, configurator);
	}

	/** Export publications. The content of the exported flow depends on the sub-interfaces.
	 *
	 * @param publications the publications to export.
	 * @param configurator the configurator for the export, never {@code null}.
	 * @param rootKeys the sequence of keys for building the root of the tree. The exported data is then
	 *     output into the last created node with the {@code rootKeys}.
	 * @return the representation of the publications.
	 * @throws Exception if the publication cannot be converted.
	 */
	String exportPublicationsWithRootKeys(Iterable<? extends Publication> publications, ExporterConfigurator configurator,
			String... rootKeys) throws Exception;

}
