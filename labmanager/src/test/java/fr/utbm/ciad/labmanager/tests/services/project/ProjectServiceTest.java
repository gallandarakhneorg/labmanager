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

package fr.utbm.ciad.labmanager.tests.services.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.member.Membership;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.member.PersonRepository;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganizationRepository;
import fr.utbm.ciad.labmanager.data.project.Project;
import fr.utbm.ciad.labmanager.data.project.ProjectMember;
import fr.utbm.ciad.labmanager.data.project.ProjectMemberRepository;
import fr.utbm.ciad.labmanager.data.project.ProjectRepository;
import fr.utbm.ciad.labmanager.data.project.ProjectStatus;
import fr.utbm.ciad.labmanager.services.member.MembershipService;
import fr.utbm.ciad.labmanager.services.project.ProjectService;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;

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

	private SessionFactory sessionFactory;

	@BeforeEach
	public void setUp() {
		this.messages = mock(MessageSourceAccessor.class);
		this.projectRepository = mock(ProjectRepository.class);
		this.projectMemberRepository = mock(ProjectMemberRepository.class);
		this.organizationRepository = mock(ResearchOrganizationRepository.class);
		this.personRepository = mock(PersonRepository.class);
		this.membershipService = mock(MembershipService.class);
		this.fileManager = mock(DownloadableFileManager.class);
		this.sessionFactory = mock(SessionFactory.class);
		this.test = new ProjectService(this.projectRepository, this.projectMemberRepository,
				this.organizationRepository, this.personRepository, this.membershipService, this.fileManager,
				this.messages, new Constants(), this.sessionFactory);
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
		when(this.projectRepository.findById(eq(Long.valueOf(1234l)))).thenReturn(Optional.of(expected));
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
		when(this.projectRepository.findDistinctOrganizationProjects(eq(Long.valueOf(1234l)))).thenReturn(
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
		when(this.projectRepository.findDistinctOrganizationProjects(eq(Boolean.FALSE), eq(ProjectStatus.ACCEPTED), eq(Long.valueOf(1234)))).thenReturn(
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
		when(this.projectRepository.findDistinctPersonProjects(eq(Long.valueOf(1234)))).thenReturn(
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
		when(this.projectRepository.findDistinctPersonProjects(eq(Boolean.FALSE), eq(ProjectStatus.ACCEPTED), eq(Long.valueOf(1234)))).thenReturn(
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
		when(pers0.getId()).thenReturn(12345l);
		Person pers1 = mock(Person.class);
		when(pers1.getId()).thenReturn(23456l);
		//
		when(mbr0.isActiveIn(any(LocalDate.class), any(LocalDate.class))).thenReturn(Boolean.FALSE);
		when(mbr0.isPermanentPosition()).thenReturn(Boolean.FALSE);
		when(mbr1.isActiveIn(any(LocalDate.class), any(LocalDate.class))).thenReturn(Boolean.TRUE);
		when(mbr1.isPermanentPosition()).thenReturn(Boolean.FALSE);
		when(mbr2.isPermanentPosition()).thenReturn(Boolean.TRUE);
		//
		when(this.membershipService.getMembershipsForPerson(eq(12345l))).thenReturn(Arrays.asList(mbr0, mbr1));
		when(this.membershipService.getMembershipsForPerson(eq(23456l))).thenReturn(Arrays.asList(mbr2));
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
