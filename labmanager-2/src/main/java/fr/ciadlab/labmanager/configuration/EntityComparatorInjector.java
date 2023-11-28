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

package fr.ciadlab.labmanager.configuration;

import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.assostructure.AssociatedStructureComparator;
import fr.ciadlab.labmanager.entities.assostructure.AssociatedStructureHolderComparator;
import fr.ciadlab.labmanager.entities.conference.ConferenceComparator;
import fr.ciadlab.labmanager.entities.invitation.PersonInvitationComparator;
import fr.ciadlab.labmanager.entities.journal.JournalComparator;
import fr.ciadlab.labmanager.entities.jury.JuryMembershipComparator;
import fr.ciadlab.labmanager.entities.member.MembershipComparator;
import fr.ciadlab.labmanager.entities.member.NameBasedMembershipComparator;
import fr.ciadlab.labmanager.entities.member.PersonComparator;
import fr.ciadlab.labmanager.entities.member.PersonListComparator;
import fr.ciadlab.labmanager.entities.organization.OrganizationAddressComparator;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganizationComparator;
import fr.ciadlab.labmanager.entities.project.ProjectBudgetComparator;
import fr.ciadlab.labmanager.entities.project.ProjectComparator;
import fr.ciadlab.labmanager.entities.project.ProjectMemberComparator;
import fr.ciadlab.labmanager.entities.publication.PublicationComparator;
import fr.ciadlab.labmanager.entities.scientificaxis.ScientificAxisComparator;
import fr.ciadlab.labmanager.entities.supervision.SupervisionComparator;
import fr.ciadlab.labmanager.entities.supervision.SupervisorComparator;
import fr.ciadlab.labmanager.entities.teaching.TeachingActivityComparator;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Spring component that is injecting the comparators for the Spring entities.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@Component
public class EntityComparatorInjector {

	@Autowired
	private PersonComparator personComparator;

	@Autowired
	private PersonListComparator personListComparator;

	@Autowired
	private MembershipComparator membershipComparator;

	@Autowired
	private NameBasedMembershipComparator nameMembershipComparator;

	@Autowired
	private ResearchOrganizationComparator organizationComparator;

	@Autowired
	private OrganizationAddressComparator organizationAddressComparator;

	@Autowired
	private PublicationComparator publicationComparator;

	@Autowired
	private JournalComparator journalComparator;

	@Autowired
	private ConferenceComparator conferenceComparator;

	@Autowired
	private JuryMembershipComparator juryMembershipComparator;

	@Autowired
	private PersonInvitationComparator personInvitationComparator;

	@Autowired
	private SupervisorComparator supervisorComparator;

	@Autowired
	private SupervisionComparator supervisionComparator;

	@Autowired
	private ProjectComparator projectComparator;

	@Autowired
	private ProjectMemberComparator projectMemberComparator;

	@Autowired
	private ProjectBudgetComparator projectBudgetComparator;

	@Autowired
	private AssociatedStructureHolderComparator externalStructureHolderComparator;

	@Autowired
	private AssociatedStructureComparator externalStructureComparator;

	@Autowired
	private TeachingActivityComparator teachingActivityComparator;

	@Autowired
	private ScientificAxisComparator scientificAxisComparator;

	/** Invoked by the Spring engine is started and this injector is created in memory.
	 */
	@PostConstruct
	public void postConstruct() {
		EntityUtils.setPreferredResearchOrganizationComparator(this.organizationComparator);
		EntityUtils.setPreferredOrganizationAddressComparator(this.organizationAddressComparator);
		EntityUtils.setPreferredPersonComparator(this.personComparator);
		EntityUtils.setPreferredPersonListComparator(this.personListComparator);
		EntityUtils.setPreferredMembershipComparator(this.membershipComparator);
		EntityUtils.setPreferredPersonNameBasedMembershipComparator(this.nameMembershipComparator);
		EntityUtils.setPreferredPublicationComparator(this.publicationComparator);
		EntityUtils.setPreferredJournalComparator(this.journalComparator);
		EntityUtils.setPreferredJuryMembershipComparator(this.juryMembershipComparator);
		EntityUtils.setPreferredPersonInvitationComparator(this.personInvitationComparator);
		EntityUtils.setPreferredSupervisorComparator(this.supervisorComparator);
		EntityUtils.setPreferredSupervisionComparator(this.supervisionComparator);
		EntityUtils.setPreferredProjectComparator(this.projectComparator);
		EntityUtils.setPreferredProjectMemberComparator(this.projectMemberComparator);
		EntityUtils.setPreferredProjectBudgetComparator(this.projectBudgetComparator);
		EntityUtils.setPreferredAssociatedStructureHolderComparator(this.externalStructureHolderComparator);
		EntityUtils.setPreferredAssociatedStructureComparator(this.externalStructureComparator);
		EntityUtils.setPreferredTeachingActivityComparator(this.teachingActivityComparator);
		EntityUtils.setPreferredScientificAxisComparator(this.scientificAxisComparator);
		EntityUtils.setPreferredConferenceComparator(this.conferenceComparator);
	}

}
