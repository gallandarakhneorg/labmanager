/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.io.od;

import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.io.ExporterConfigurator;

/** Utilities for exporting publications to Open Document Text.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public interface OpenDocumentTextExporter {

	/** Replies the ODT representation of the publications that are given as argument.
	 * <p>The decorators for the names are usually the following (implementation may change them):
	 * <table>
	 * <thead>
	 *   <tr>
	 *     <th>Has person selector?</th>
	 *     <th>Has no person selector?</th>
	 *   </tr>
	 * </thead>
	 * <tbody>
	 *   <tr>
	 *     <td>Yes</td>
	 *     <td>
	 *       <ul>
	 *         <li>Selected person: bold + underline</li>
	 *         <li>Researcher: bold</li>
	 *         <li>PhD student: underline</li>
	 *         <li>PD or engineer: italic</li>
	 *       </ul>
	 *     </td>
	 *   </tr>
	 *   <tr>
	 *     <td>No</td>
	 *     <td>
	 *       <ul>
	 *         <li>Researcher: bold</li>
	 *         <li>PhD student: underline</li>
	 *         <li>PD or engineer: italic</li>
	 *       </ul>
	 *     </td>
	 *   </tr>
	 * </tbody>
	 * </table>
	 *
	 * @param publications the publications to export.
	 * @param configurator the configurator for the export, never {@code null}.
	 * @return the ODT representation of the publications.
	 * @throws Exception if the publication cannot be converted to ODT.
	 */
	byte[] exportPublications(Iterable<Publication> publications, ExporterConfigurator configurator) throws Exception;

}
