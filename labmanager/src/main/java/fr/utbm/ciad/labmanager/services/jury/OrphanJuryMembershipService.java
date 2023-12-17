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

package fr.utbm.ciad.labmanager.services.jury;

import java.util.Locale;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.jury.JuryMembership;
import fr.utbm.ciad.labmanager.data.jury.JuryMembershipRepository;
import fr.utbm.ciad.labmanager.services.AbstractOrphanService;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.progress.Progression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for the orphan jury memberships.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Service
public class OrphanJuryMembershipService extends AbstractOrphanService<JuryMembership> {

	private static final String MESSAGE_PREFIX = "orphanJuryMembershipService."; //$NON-NLS-1$

	private JuryMembershipRepository membershipRepository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param membershipRepository the jury membership repository.
	 */
	public OrphanJuryMembershipService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired JuryMembershipRepository membershipRepository) {
		super(messages, constants);
		this.membershipRepository = membershipRepository;
	}

	@Override
	public void computeOrphans(ArrayNode receiver, Locale locale, Progression progress) {
		computeOrphansInJson(receiver, this.membershipRepository, this,
				Constants.JURY_MEMBERSHIP_EDITING_ENDPOINT, Constants.PERSON_ENDPOINT_PARAMETER,
				Constants.GOTO_ENDPOINT_PARAMETER, it -> Integer.toString(it.getId()),
				Constants.JURY_MEMBERSHIP_DELETION_ENDPOINT, Constants.ID_ENDPOINT_PARAMETER,
				locale, progress);
	}

	@Override
	protected Object getEditionParameterValue(JuryMembership entity) {
		if (entity.getPerson() != null) {
			return Integer.valueOf(entity.getPerson().getId());
		}
		return Integer.valueOf(0);
	}

	@Override
	public String getOrphanCriteria(JuryMembership jury, Locale locale) {
		if (jury.getPerson() == null) {
			return getMessage(locale, MESSAGE_PREFIX + "NoPerson"); //$NON-NLS-1$
		}
		if (jury.getCandidate() == null) {
			return getMessage(locale, MESSAGE_PREFIX + "NoCandidate"); //$NON-NLS-1$
		}
		if (jury.getDate() == null) {
			return getMessage(locale, MESSAGE_PREFIX + "NoDate"); //$NON-NLS-1$
		}
		if (Strings.isNullOrEmpty(jury.getTitle())) {
			return getMessage(locale, MESSAGE_PREFIX + "EmptyTitle"); //$NON-NLS-1$
		}
		if (Strings.isNullOrEmpty(jury.getUniversity())) {
			return getMessage(locale, MESSAGE_PREFIX + "NoUniversity"); //$NON-NLS-1$
		}
		if (jury.getPromoters().isEmpty()) {
			return getMessage(locale, MESSAGE_PREFIX + "EmptyPromoterList"); //$NON-NLS-1$
		}
		return null;
	}

	@Override
	public String getOrphanEntityLabel(JuryMembership entity, Locale locale) {
		if (entity.getCandidate() != null && entity.getPerson() != null) {
			return entity.getCandidate().getFullName() + " - " + entity.getPerson().getFullName() + " - " + entity.getDate(); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (entity.getCandidate() != null) {
			return entity.getCandidate().getFullName() + " - ? - " + entity.getDate(); //$NON-NLS-1$
		}
		if (entity.getPerson() != null) {
			return "? - " + entity.getPerson().getFullName() + " - " + entity.getDate(); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return "? - ? - " + entity.getDate(); //$NON-NLS-1$
	}

	@Override
	public String getOrphanTypeLabel(Locale locale) {
		return getMessage(locale, MESSAGE_PREFIX + "Name"); //$NON-NLS-1$
	}

}
