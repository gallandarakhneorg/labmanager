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

package fr.utbm.ciad.labmanager.services.member;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.member.PersonRepository;
import fr.utbm.ciad.labmanager.services.AbstractOrphanService;
import fr.utbm.ciad.labmanager.services.assostructure.AssociatedStructureService;
import fr.utbm.ciad.labmanager.services.invitation.PersonInvitationService;
import fr.utbm.ciad.labmanager.services.jury.JuryMembershipService;
import fr.utbm.ciad.labmanager.services.project.ProjectService;
import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import fr.utbm.ciad.labmanager.services.supervision.SupervisionService;
import fr.utbm.ciad.labmanager.services.teaching.TeachingService;
import org.arakhne.afc.progress.Progression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for managing the orphan persons.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Service
public class OrphanPersonService extends AbstractOrphanService<Person> {

	private static final String MESSAGE_PREFIX = "orphanPersonService."; //$NON-NLS-1$

	private PublicationService publicationService;

	private PersonRepository personRepository;

	private AssociatedStructureService structureService;

	private PersonInvitationService invitationService;

	private JuryMembershipService juryMembershipService;

	private MembershipService membershipService;

	private ProjectService projectService;

	private SupervisionService supervisionService;

	private TeachingService teachingService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param publicationService the publication service.
	 * @param personRepository the person repository.
	 * @param structureService the service for accessing the associated structures.
	 * @param invitationService the service for accessing the person invitations.
	 * @param juryMembershipService the service for accessing the jury memberships.
	 * @param membershipService the service for accessing the organization memberships.
	 * @param projectService the service for accessing the projects.
	 * @param supervisionService the service for accessing the student supervisions.
	 * @param teachingService the service for accessing the teaching activities.
	 */
	public OrphanPersonService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired PublicationService publicationService,
			@Autowired PersonRepository personRepository,
			@Autowired AssociatedStructureService structureService,
			@Autowired PersonInvitationService invitationService,
			@Autowired JuryMembershipService juryMembershipService,
			@Autowired MembershipService membershipService,
			@Autowired ProjectService projectService,
			@Autowired SupervisionService supervisionService,
			@Autowired TeachingService teachingService) {
		super(messages, constants);
		this.publicationService = publicationService;
		this.personRepository = personRepository;
		this.structureService = structureService;
		this.invitationService = invitationService;
		this.juryMembershipService = juryMembershipService;
		this.membershipService = membershipService;
		this.projectService = projectService;
		this.supervisionService = supervisionService;
		this.teachingService = teachingService;
	}

	@Override
	public void computeOrphans(ArrayNode receiver, Progression progress) {
		computeOrphansInJson(receiver, this.personRepository, this,
				Constants.PERSON_EDITING_ENDPOINT, Constants.PERSON_ENDPOINT_PARAMETER,
				Constants.PERSON_DELETING_ENDPOINT, Constants.PERSON_ENDPOINT_PARAMETER,
				progress);
	}

	@Override
	public String getOrphanCriteria(Person person) {
		final int id = person.getId();
		if (this.publicationService.isAuthor(id)) {
			return null;
		}
		if (this.membershipService.isMember(id)) {
			return null;
		}
		if (this.juryMembershipService.isInvolved(id)) {
			return null;
		}
		if (this.supervisionService.isInvolved(id)) {
			return null;
		}
		if (this.projectService.isInvolved(id)) {
			return null;
		}
		if (this.structureService.isInvolved(id)) {
			return null;
		}
		if (this.invitationService.isAssociated(id)) {
			return null;
		}
		if (this.teachingService.isTeacher(id)) {
			return null;
		}
		return getMessage(MESSAGE_PREFIX + "UnlinkedPerson"); //$NON-NLS-1$
	}

	@Override
	public String getOrphanEntityLabel(Person entity) {
		return entity.getFullName();
	}

	@Override
	public String getOrphanTypeLabel() {
		return getMessage(MESSAGE_PREFIX + "Name"); //$NON-NLS-1$
	}

}
