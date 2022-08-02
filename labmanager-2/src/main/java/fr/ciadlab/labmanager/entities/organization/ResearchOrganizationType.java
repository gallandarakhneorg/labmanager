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
 * you entered into with the CIAD.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.entities.organization;

import com.google.common.base.Strings;
import org.arakhne.afc.vmutil.locale.Locale;

/** Type of research organization.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum ResearchOrganizationType {
	/** Research team.
	 */
	RESEARCH_TEAM,
	/** Research department.
	 */
	LABORATORY_DEPARTMENT,
	/** Research laboratory or institute.
	 */
	LABORATORY,
	/** Faculty.
	 */
	FACULTY,
	/** University.
	 */
	UNIVERSITY,
	/** University community or network.
	 */
	COMMUNITY,
	/** OTHER.
	 */
	OTHER;

	/** Replies the label of the type of organization.
	 *
	 * @return the label of the type of organization.
	 */
	public String getLabel() {
		final String label = Locale.getString(ResearchOrganizationType.class, name());
		return Strings.nullToEmpty(label);
	}

	/** Replies the type of organization that corresponds to the given name, with a case-insensitive
	 * test of the name.
	 *
	 * @param name the name of the type of organization, to search for.
	 * @return the status.
	 * @throws IllegalArgumentException if the given name does not corresponds to a type.
	 */
	public static ResearchOrganizationType valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final ResearchOrganizationType status : values()) {
				if (name.equalsIgnoreCase(status.name())) {
					return status;
				}
			}
		}
		throw new IllegalArgumentException("Invalid type of organization: " + name); //$NON-NLS-1$
	}

}
