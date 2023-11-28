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

package fr.ciadlab.labmanager.entities.project;

import java.util.Locale;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.BaseMessageSource;
import org.springframework.context.support.MessageSourceAccessor;

/** Role of a person in a project.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.0
 */
public enum Role {

	/** Coordinator of the whole project.
	 */
	PROJECT_COORDINATOR {
		@Override
		public boolean isHead() {
			return true;
		}
	},

	/** Scientific head for a partner.
	 */
	SCIENTIFIC_HEAD {
		@Override
		public boolean isHead() {
			return true;
		}
	},

	/** Leader of a work package.
	 */
	WORK_PACKAGE_LEADER {
		@Override
		public boolean isHead() {
			return true;
		}
	},

	/** Leader of a task.
	 */
	TASK_LEADER {
		@Override
		public boolean isHead() {
			return true;
		}
	},

	/** Participant of a project.
	 */
	PARTICIPANT {
		@Override
		public boolean isHead() {
			return false;
		}
	};
	
	private static final String MESSAGE_PREFIX = "role."; //$NON-NLS-1$
	
	private MessageSourceAccessor messages;

	/** Replies the message accessor to be used.
	 *
	 * @return the accessor.
	 */
	public MessageSourceAccessor getMessageSourceAccessor() {
		if (this.messages == null) {
			this.messages = BaseMessageSource.getStaticMessageSourceAccessor();
		}
		return this.messages;
	}

	/** Change the message accessor to be used.
	 *
	 * @param messages the accessor.
	 */
	public void setMessageSourceAccessor(MessageSourceAccessor messages) {
		this.messages = messages;
	}

	/** Replies the label of the role in the current language.
	 *
	 * @return the label of the role in the current language.
	 */
	public String getLabel() {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name());
		return Strings.nullToEmpty(label);
	}

	/** Replies the label of the role in the given language.
	 *
	 * @param locale the locale to use.
	 * @return the label of the role in the given  language.
	 */
	public String getLabel(Locale locale) {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name(), locale);
		return Strings.nullToEmpty(label);
	}

	/** Replies the role that corresponds to the given name, with a case-insensitive
	 * test of the name.
	 *
	 * @param name the name of the role, to search for.
	 * @return the role.
	 * @throws IllegalArgumentException if the given name does not corresponds to a role.
	 */
	public static Role valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final Role ranking : values()) {
				if (name.equalsIgnoreCase(ranking.name())) {
					return ranking;
				}
			}
		}
		throw new IllegalArgumentException("Invalid role: " + name); //$NON-NLS-1$
	}

	/** Replies if this role is for a head of the project.
	 *
	 * @return {@code true} if the role is for a head.
	 */
	public abstract boolean isHead();

}
