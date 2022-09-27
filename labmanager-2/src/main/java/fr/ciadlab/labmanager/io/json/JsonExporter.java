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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import fr.ciadlab.labmanager.io.PublicationExporter;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

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

	/** Export publications. The content of the exported flow depends on the sub-interfaces.
	 *
	 * @param publications the publications to export.
	 * @param configurator the configurator for the export, never {@code null}.
	 * @param callback a function that is invoked for each publication for giving the opportunity
	 *     to fill up the Json node of the publication.
	 * @param rootKeys the sequence of keys for building the root of the tree. The exported data is then
	 *     output into the last created node with the {@code rootKeys}.
	 * @return the representation of the publications with the Jackson API.
	 * @throws Exception if the publication cannot be converted.
	 */
	JsonNode exportPublicationsAsTreeWithRootKeys(Iterable<? extends Publication> publications, ExporterConfigurator configurator,
			Procedure2<Publication, ObjectNode> callback, String... rootKeys) throws Exception;

}
