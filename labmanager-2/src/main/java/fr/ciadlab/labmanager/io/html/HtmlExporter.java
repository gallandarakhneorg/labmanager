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

package fr.ciadlab.labmanager.io.html;

import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import fr.ciadlab.labmanager.io.PublicationExporter;

/** Utilities for exporting publications to HTML content.
 * This exporter is generic and not dedicated to a specific target.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public interface HtmlExporter extends PublicationExporter<String> {

	/** Replies the unsecable separator.
	 *
	 * @return the HTML code.
	 */
	String separator();

	/** Replies the unsecable separator with double length.
	 *
	 * @return the HTML code.
	 */
	String doubleSeparator();

	/** Replies the HTML representation of the publications that are given as argument.
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
	 *         <li>Selected person: bold+underline</li>
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
	 * @return the HTML representation of the publications.
	 * @throws Exception if the publication cannot be converted to HTML.
	 */
	@Override
	String exportPublications(Iterable<? extends Publication> publications, ExporterConfigurator configurator) throws Exception;

}
