/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.utbm.ciad.labmanager.data.teaching;

import com.google.common.base.Strings;
import org.springframework.context.support.MessageSourceAccessor;

import java.util.Locale;

/** Describe the role of a teaching in the activity.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.4
 */
public enum TeacherRole {

	/** Creator of the activity.
	 */
	CREATOR,

	/** Supervisor of the activity.
	 */
	SUPERVISOR,

	/** Participant to the activity.
	 */
	PARTICIPANT;

	private static final String MESSAGE_PREFIX = "teacherRole."; //$NON-NLS-1$

	/** Replies the label of the teacher role in the given language.
	 *
	 * @param messages the accessor to the localized labels.
	 * @param locale the locale to use.
	 * @return the label of the teacher role in the given  language.
	 */
	public String getLabel(MessageSourceAccessor messages, Locale locale) {
		final var label = messages.getMessage(MESSAGE_PREFIX + name(), locale);
		return Strings.nullToEmpty(label);
	}

	/** Replies the teacher role that corresponds to the given name, with a case-insensitive
	 * test of the name.
	 *
	 * @param name the name of the teacher role, to search for.
	 * @return the teacher role.
	 * @throws IllegalArgumentException if the given name does not corresponds to a teacher role.
	 */
	public static TeacherRole valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final var ranking : values()) {
				if (name.equalsIgnoreCase(ranking.name())) {
					return ranking;
				}
			}
		}
		throw new IllegalArgumentException("Invalid teacher role: " + name); //$NON-NLS-1$
	}

}
