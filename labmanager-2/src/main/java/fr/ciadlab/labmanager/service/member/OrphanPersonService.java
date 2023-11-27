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

package fr.ciadlab.labmanager.service.member;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.service.AbstractOrphanService;
import fr.ciadlab.labmanager.service.assostructure.AssociatedStructureService;
import fr.ciadlab.labmanager.service.invitation.PersonInvitationService;
import fr.ciadlab.labmanager.service.jury.JuryMembershipService;
import fr.ciadlab.labmanager.service.project.ProjectService;
import fr.ciadlab.labmanager.service.publication.PublicationService;
import fr.ciadlab.labmanager.service.supervision.SupervisionService;
import fr.ciadlab.labmanager.service.teaching.TeachingService;
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
