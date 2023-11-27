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

package fr.ciadlab.labmanager.io;

import fr.ciadlab.labmanager.entities.publication.Publication;

/** Exporter of publications.
 * 
 * @param <T> the type of the replied value after exporting.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public interface PublicationExporter<T> {

	/** Export publications. The content of the exported flow depends on the sub-interfaces.
	 *
	 * @param publications the publications to export.
	 * @param configurator the configurator for the export, never {@code null}.
	 * @return the representation of the publications.
	 * @throws Exception if the publication cannot be converted.
	 */
	T exportPublications(Iterable<? extends Publication> publications, ExporterConfigurator configurator) throws Exception;

}
