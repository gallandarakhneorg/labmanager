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

package fr.ciadlab.labmanager.service.project;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.member.Gender;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.member.WebPageNaming;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.project.Project;
import fr.ciadlab.labmanager.entities.project.ProjectActivityType;
import fr.ciadlab.labmanager.entities.project.ProjectBudget;
import fr.ciadlab.labmanager.entities.project.ProjectContractType;
import fr.ciadlab.labmanager.entities.project.ProjectMember;
import fr.ciadlab.labmanager.entities.project.ProjectStatus;
import fr.ciadlab.labmanager.entities.project.ProjectWebPageNaming;
import fr.ciadlab.labmanager.entities.project.Role;
import fr.ciadlab.labmanager.io.filemanager.DownloadableFileManager;
import fr.ciadlab.labmanager.io.wos.WebOfSciencePlatform;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.repository.organization.ResearchOrganizationRepository;
import fr.ciadlab.labmanager.repository.project.ProjectMemberRepository;
import fr.ciadlab.labmanager.repository.project.ProjectRepository;
import fr.ciadlab.labmanager.repository.publication.AuthorshipRepository;
import fr.ciadlab.labmanager.repository.publication.PublicationRepository;
import fr.ciadlab.labmanager.service.member.MembershipService;
import fr.ciadlab.labmanager.utils.names.DefaultPersonNameParser;
import fr.ciadlab.labmanager.utils.names.PersonNameComparator;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import fr.ciadlab.labmanager.utils.names.SorensenDicePersonNameComparator;
import fr.ciadlab.labmanager.utils.trl.TRL;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.FileSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.multipart.MultipartFile;

/** Tests for {@link ProjectService}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

	private MessageSourceAccessor messages;

	private ProjectRepository projectRepository;

	private ProjectMemberRepository projectMemberRepository;

	private ResearchOrganizationRepository organizationRepository;

	private PersonRepository personRepository;

	private MembershipService membershipService;

	private DownloadableFileManager fileManager;

	private ProjectService test;

	@BeforeEach
	public void setUp() {
		this.messages = mock(MessageSourceAccessor.class);
		this.projectRepository = mock(ProjectRepository.class);
		this.projectMemberRepository = mock(ProjectMemberRepository.class);
		this.organizationRepository = mock(ResearchOrganizationRepository.class);
		this.personRepository = mock(PersonRepository.class);
		this.membershipService = mock(MembershipService.class);
		this.fileManager = mock(DownloadableFileManager.class);
		this.test = new ProjectService(this.messages, new Constants(), this.projectRepository, this.projectMemberRepository,
				this.organizationRepository, this.personRepository, this.membershipService, this.fileManager);
	}
	
	@Test
	public void getAllProjects() {
		Project prj0 = mock(Project.class);
		Project prj1 = mock(Project.class);
		Project prj2 = mock(Project.class);
		when(this.projectRepository.findAll()).thenReturn(Arrays.asList(prj0, prj1, prj2));
		//
		List<Project> list = this.test.getAllProjects();
		assertNotNull(list);
		assertEquals(3, list.size());
		assertTrue(list.contains(prj0));
		assertTrue(list.contains(prj1));
		assertTrue(list.contains(prj2));
	}

	@Test
	public void getAllPublicProjects() {
		Project prj0 = mock(Project.class);
		Project prj1 = mock(Project.class);
		when(this.projectRepository.findDistinctByConfidentialAndStatus(eq(Boolean.FALSE), eq(ProjectStatus.ACCEPTED))).thenReturn(
				Arrays.asList(prj0, prj1));
		//
		List<Project> list = this.test.getAllPublicProjects();
		assertNotNull(list);
		assertEquals(2, list.size());
		assertTrue(list.contains(prj0));
		assertTrue(list.contains(prj1));
	}

	@Test
	public void getProjectById() {
		Project expected = mock(Project.class);
		when(this.projectRepository.findById(eq(Integer.valueOf(1234)))).thenReturn(Optional.of(expected));
		//
		Project project = this.test.getProjectById(1234);
		assertNotNull(project);
		assertSame(expected, project);
		//
		project = this.test.getProjectById(7896);
		assertNull(project);
	}

	@Test
	public void getProjectsByOrganizationId() {
		Project prj0 = mock(Project.class);
		Project prj1 = mock(Project.class);
		when(this.projectRepository.findDistinctOrganizationProjects(eq(Integer.valueOf(1234)))).thenReturn(
				Arrays.asList(prj0, prj1));
		//
		List<Project> projects = this.test.getProjectsByOrganizationId(1234);
		assertNotNull(projects);
		assertEquals(2, projects.size());
		assertTrue(projects.contains(prj0));
		assertTrue(projects.contains(prj1));
	}

	@Test
	public void getPublicProjectsByOrganizationId() {
		Project prj0 = mock(Project.class);
		Project prj1 = mock(Project.class);
		when(this.projectRepository.findDistinctOrganizationProjects(eq(Boolean.FALSE), eq(ProjectStatus.ACCEPTED), eq(Integer.valueOf(1234)))).thenReturn(
				Arrays.asList(prj0, prj1));
		//
		List<Project> projects = this.test.getPublicProjectsByOrganizationId(1234);
		assertNotNull(projects);
		assertEquals(2, projects.size());
		assertTrue(projects.contains(prj0));
		assertTrue(projects.contains(prj1));
	}

	@Test
	public void getProjectsByPersonId() {
		Project prj0 = mock(Project.class);
		Project prj1 = mock(Project.class);
		when(this.projectRepository.findDistinctPersonProjects(eq(Integer.valueOf(1234)))).thenReturn(
				new HashSet<>(Arrays.asList(prj0, prj1)));
		//
		Set<Project> projects = this.test.getProjectsByPersonId(1234);
		assertNotNull(projects);
		assertEquals(2, projects.size());
		assertTrue(projects.contains(prj0));
		assertTrue(projects.contains(prj1));
	}

	@Test
	public void getPublicProjectsByPersonId() {
		Project prj0 = mock(Project.class);
		Project prj1 = mock(Project.class);
		when(this.projectRepository.findDistinctPersonProjects(eq(Boolean.FALSE), eq(ProjectStatus.ACCEPTED), eq(Integer.valueOf(1234)))).thenReturn(
				Arrays.asList(prj0, prj1));
		//
		List<Project> projects = this.test.getPublicProjectsByPersonId(1234);
		assertNotNull(projects);
		assertEquals(2, projects.size());
		assertTrue(projects.contains(prj0));
		assertTrue(projects.contains(prj1));
	}

	private Project createProjectWithRecuitedPersons(Membership mbr0, Membership mbr1, Membership mbr2) {
		LocalDate sdt = LocalDate.now().minusDays(1);
		LocalDate edt = LocalDate.now().plusDays(1);
		//
		Person pers0 = mock(Person.class);
		when(pers0.getId()).thenReturn(12345);
		Person pers1 = mock(Person.class);
		when(pers1.getId()).thenReturn(23456);
		//
		when(mbr0.isActiveIn(any(LocalDate.class), any(LocalDate.class))).thenReturn(Boolean.FALSE);
		when(mbr0.isPermanentPosition()).thenReturn(Boolean.FALSE);
		when(mbr1.isActiveIn(any(LocalDate.class), any(LocalDate.class))).thenReturn(Boolean.TRUE);
		when(mbr1.isPermanentPosition()).thenReturn(Boolean.FALSE);
		when(mbr2.isPermanentPosition()).thenReturn(Boolean.TRUE);
		//
		when(this.membershipService.getMembershipsForPerson(eq(12345))).thenReturn(Arrays.asList(mbr0, mbr1));
		when(this.membershipService.getMembershipsForPerson(eq(23456))).thenReturn(Arrays.asList(mbr2));
		//
		ProjectMember part0 = mock(ProjectMember.class);
		when(part0.getPerson()).thenReturn(pers0);
		ProjectMember part1 = mock(ProjectMember.class);
		when(part1.getPerson()).thenReturn(pers1);
		//
		Project prj = mock(Project.class);
		when(prj.getStartDate()).thenReturn(sdt);
		when(prj.getEndDate()).thenReturn(edt);
		when(prj.getParticipants()).thenReturn(Arrays.asList(part0, part1));
		//
		return prj;
	}
	
	@Test
	public void getRecuitedPersons() {
		Membership mbr0 = mock(Membership.class);
		Membership mbr1 = mock(Membership.class);
		Membership mbr2 = mock(Membership.class);
		Project prj = createProjectWithRecuitedPersons(mbr0, mbr1, mbr2);
		List<Membership> list = this.test.getRecuitedPersons(prj);
		assertNotNull(list);
		assertFalse(list.contains(mbr0));
		assertTrue(list.contains(mbr1));
		assertFalse(list.contains(mbr2));
	}

	@Test
	public void getRecuitedPersonStream() {
		Membership mbr0 = mock(Membership.class);
		Membership mbr1 = mock(Membership.class);
		Membership mbr2 = mock(Membership.class);
		Project prj = createProjectWithRecuitedPersons(mbr0, mbr1, mbr2);
		Stream<Membership> stream = this.test.getRecuitedPersonStream(prj);
		assertNotNull(stream);
		List<Membership> list = stream.collect(Collectors.toList());
		assertFalse(list.contains(mbr0));
		assertTrue(list.contains(mbr1));
		assertFalse(list.contains(mbr2));
	}

}
