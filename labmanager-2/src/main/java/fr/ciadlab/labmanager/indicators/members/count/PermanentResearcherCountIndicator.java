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

package fr.ciadlab.labmanager.indicators.members.count;

import java.util.List;
import java.util.stream.Collectors;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.member.MemberStatus;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.indicators.AbstractInstantIndicator;
import fr.ciadlab.labmanager.indicators.members.fte.PermanentResearcherFteIndicator;
import fr.ciadlab.labmanager.utils.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/** Count the current number of permanent researchers in a specific organization.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 * @see ResearcherCountIndicator
 * @see PermanentResearcherFteIndicator
 */
@Component
public class PermanentResearcherCountIndicator extends AbstractInstantIndicator {

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 */
	public PermanentResearcherCountIndicator(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants) {
		super(messages, constants);
	}

	@Override
	public String getName() {
		return getMessage("permanentResearcherCountIndicator.name"); //$NON-NLS-1$
	}

	@Override
	public String getLabel(Unit unit) {
		return getLabelWithoutYears("permanentResearcherCountIndicator.label"); //$NON-NLS-1$
	}

	@Override
	protected Number computeValue(ResearchOrganization organization) {
		final List<Membership> researchers = organization.getMemberships()
				.parallelStream()
				.filter(PermanentResearcherCountIndicator::isPermanentResearcher)
				.collect(Collectors.toList());
		final long nb = researchers.size();
		setComputationDetails(researchers, it -> it.getPerson().getFullNameWithLastNameFirst());
		return Long.valueOf(nb);
	}

	/** Replies if the given membership is for a permanent researcher.
	 *
	 * @param membership the membership to test.
	 * @return {@code true} if the membership is for a permanent researcher.
	 */
	public static boolean isPermanentResearcher(Membership membership) {
		if (membership != null && membership.isActive() && membership.isPermanentPosition()) {
			final MemberStatus status = membership.getMemberStatus();
			return !status.isExternalPosition() && status != MemberStatus.PHD_STUDENT
					&& status.isResearcher() && status.isPermanentPositionAllowed();
		}
		return false;
	}

}
