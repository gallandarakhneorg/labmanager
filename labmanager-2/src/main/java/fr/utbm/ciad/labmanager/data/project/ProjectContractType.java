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

package fr.utbm.ciad.labmanager.data.project;

import java.util.Locale;

import com.google.common.base.Strings;
import fr.utbm.ciad.labmanager.configuration.BaseMessageSource;
import org.springframework.context.support.MessageSourceAccessor;

/** Describe the type of contract that is related to a project.
 * 
 * RCO Recherhe contractuelle collaborative
 * RCD Recherche contractuelle directe
 * PR Prestation
 * PI Propriété intellectuelle
 * 
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.2
 * @see "https://anr.fr/fileadmin/documents/2017/guide-CIR-2017.pdf"
 */
public enum ProjectContractType {

	/** Type is not specified.
	 */
	NOT_SPECIFIED,

	/** Collaborative contractual research, e.g., public funding projects, including those with a not academic partner.
	 */
	RCO,

	/** Direct contractual research, IP is shared with the partner.
	 */
	RCD,

	/** Contractual service, the research organization transfer the IP to the partner.
	 */
	PR,

	/** Contract related to the intellectual property.
	 */
	PI;

	private static final String MESSAGE_PREFIX = "projectContractType."; //$NON-NLS-1$
	
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

	/** Replies the label of the contract type in the current language.
	 *
	 * @return the label of the contract type in the current language.
	 */
	public String getLabel() {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name());
		return Strings.nullToEmpty(label);
	}

	/** Replies the label of the contract type in the given language.
	 *
	 * @param locale the locale to use.
	 * @return the label of the contract type in the given  language.
	 */
	public String getLabel(Locale locale) {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name(), locale);
		return Strings.nullToEmpty(label);
	}

	/** Replies the type of type that corresponds to the given name, with a case-insensitive
	 * test of the name.
	 *
	 * @param name the name of the contract type, to search for.
	 * @return the type of contract.
	 * @throws IllegalArgumentException if the given name does not corresponds to a type of contract.
	 */
	public static ProjectContractType valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final ProjectContractType ranking : values()) {
				if (name.equalsIgnoreCase(ranking.name())) {
					return ranking;
				}
			}
		}
		throw new IllegalArgumentException("Invalid contract type: " + name); //$NON-NLS-1$
	}

}
