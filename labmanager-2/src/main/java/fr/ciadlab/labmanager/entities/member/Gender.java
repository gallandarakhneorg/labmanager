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

package fr.ciadlab.labmanager.entities.member;

import com.google.common.base.Strings;

/** Gender of a person.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public enum Gender {
	/** Unknown gender.
	 */
	NOT_SPECIFIED,
	/** Male.
	 */
	MALE,
	/** Female.
	 */
	FEMALE,
	/** Other gender.
	 */
	OTHER;


	/** Replies the gender that corresponds to the given name, with a case-insensitive
	 * test of the name.
	 *
	 * @param name the name of the gender, to search for.
	 * @return the status.
	 * @throws IllegalArgumentException if the given name does not corresponds to a type.
	 */
	public static Gender valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final Gender gender : values()) {
				if (name.equalsIgnoreCase(gender.name())) {
					return gender;
				}
			}
		}
		throw new IllegalArgumentException("Invalid gender: " + name); //$NON-NLS-1$
	}

}
