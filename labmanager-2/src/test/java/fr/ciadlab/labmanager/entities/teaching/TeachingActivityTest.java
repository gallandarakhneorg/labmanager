/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
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

package fr.ciadlab.labmanager.entities.teaching;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.utils.country.CountryCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link TeachingActivity}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class TeachingActivityTest {

	private TeachingActivity test;

	@BeforeEach
	public void setUp() {
		this.test = new TeachingActivity();
	}

	@Test
	public void getId() {
		assertEquals(0, this.test.getId());
	}

	@Test
	public void setId() {
		this.test.setId(12547);
		assertEquals(12547, this.test.getId());
		this.test.setId(9251568);
		assertEquals(9251568, this.test.getId());
	}

	@Test
	public void getPerson() {
		assertNull(this.test.getPerson());
	}

	@Test
	public void setPerson() {
		Person person = mock(Person.class);
		this.test.setPerson(person);
		assertSame(person, this.test.getPerson());
	}

	@Test
	public void setPerson_null() {
		Person person = mock(Person.class);
		this.test.setPerson(person);

		this.test.setPerson(null);
		assertNull(this.test.getPerson());
	}

	@Test
	public void getUniversity() {
		assertNull(this.test.getUniversity());
	}

	@Test
	public void setUniversity() {
		ResearchOrganization university = mock(ResearchOrganization.class);
		this.test.setUniversity(university);
		assertSame(university, this.test.getUniversity());
	}

	@Test
	public void setUniversity_null() {
		ResearchOrganization university = mock(ResearchOrganization.class);
		this.test.setUniversity(university);

		this.test.setUniversity(null);
		assertNull(this.test.getUniversity());
	}

	@Test
	public void getCode() {
		assertNull(this.test.getCode());
	}

	@Test
	public void setCode() {
		this.test.setCode("xyz");
		assertEquals("xyz", this.test.getCode());
		this.test.setCode("abc");
		assertEquals("abc", this.test.getCode());
	}

	@Test
	public void setCode_null() {
		this.test.setCode("xyz");
		assertEquals("xyz", this.test.getCode());
		this.test.setCode(null);
		assertNull(this.test.getCode());
	}

	@Test
	public void setCode_empty() {
		this.test.setCode("xyz");
		assertEquals("xyz", this.test.getCode());
		this.test.setCode("");
		assertNull(this.test.getCode());
	}

	@Test
	public void getTitle() {
		assertNull(this.test.getTitle());
	}

	@Test
	public void setTitle() {
		this.test.setTitle("xyz");
		assertEquals("xyz", this.test.getTitle());
		this.test.setTitle("abc");
		assertEquals("abc", this.test.getTitle());
	}

	@Test
	public void setTitle_null() {
		this.test.setTitle("xyz");
		assertEquals("xyz", this.test.getTitle());
		this.test.setTitle(null);
		assertNull(this.test.getTitle());
	}

	@Test
	public void setTitle_empty() {
		this.test.setTitle("xyz");
		assertEquals("xyz", this.test.getTitle());
		this.test.setTitle("");
		assertNull(this.test.getTitle());
	}

	@Test
	public void getLevel() {
		assertSame(TeachingActivityLevel.MASTER_DEGREE, this.test.getLevel());
	}

	@Test
	public void setLevel_TeachingActivityLevel() {
		this.test.setLevel(TeachingActivityLevel.DOCTORAL_DEGREE);
		assertSame(TeachingActivityLevel.DOCTORAL_DEGREE, this.test.getLevel());
		this.test.setLevel(TeachingActivityLevel.HIGH_SCHOOL_DEGREE);
		assertSame(TeachingActivityLevel.HIGH_SCHOOL_DEGREE, this.test.getLevel());
		this.test.setLevel(TeachingActivityLevel.MASTER_DEGREE);
		assertSame(TeachingActivityLevel.MASTER_DEGREE, this.test.getLevel());
		this.test.setLevel(TeachingActivityLevel.HIGH_SCHOOL_DEGREE);
		assertSame(TeachingActivityLevel.HIGH_SCHOOL_DEGREE, this.test.getLevel());
	}

	@Test
	public void setLevel_TeachingActivityLevel_null() {
		this.test.setLevel(TeachingActivityLevel.HIGH_SCHOOL_DEGREE);
		this.test.setLevel((TeachingActivityLevel) null);
		assertSame(TeachingActivityLevel.MASTER_DEGREE, this.test.getLevel());
	}

	@Test
	public void setLevel_String() {
		this.test.setLevel("DOCTORAL_DEGREE");
		assertSame(TeachingActivityLevel.DOCTORAL_DEGREE, this.test.getLevel());
		this.test.setLevel("High_SCHOOL_DEGREE");
		assertSame(TeachingActivityLevel.HIGH_SCHOOL_DEGREE, this.test.getLevel());
		this.test.setLevel("Master_Degree");
		assertSame(TeachingActivityLevel.MASTER_DEGREE, this.test.getLevel());
		this.test.setLevel("HIGH_SCHOOL_DEGREE");
		assertSame(TeachingActivityLevel.HIGH_SCHOOL_DEGREE, this.test.getLevel());
	}

	@Test
	public void setLevel_String_null() {
		this.test.setLevel(TeachingActivityLevel.HIGH_SCHOOL_DEGREE);
		this.test.setLevel((String) null);
		assertSame(TeachingActivityLevel.MASTER_DEGREE, this.test.getLevel());
	}

	@Test
	public void setLevel_String_empty() {
		this.test.setLevel(TeachingActivityLevel.HIGH_SCHOOL_DEGREE);
		this.test.setLevel("");
		assertSame(TeachingActivityLevel.MASTER_DEGREE, this.test.getLevel());
	}

	@Test
	public void getStudentType() {
		assertSame(StudentType.INITIAL_TRAINING, this.test.getStudentType());
	}

	@Test
	public void setStudentType_StudentType() {
		this.test.setStudentType(StudentType.CONTINUOUS);
		assertSame(StudentType.CONTINUOUS, this.test.getStudentType());
		this.test.setStudentType(StudentType.APPRENTICESHIP);
		assertSame(StudentType.APPRENTICESHIP, this.test.getStudentType());
		this.test.setStudentType(StudentType.INITIAL_TRAINING);
		assertSame(StudentType.INITIAL_TRAINING, this.test.getStudentType());
	}

	@Test
	public void setStudentType_StudentType_null() {
		this.test.setStudentType(StudentType.CONTINUOUS);
		this.test.setStudentType((StudentType) null);
		assertSame(StudentType.INITIAL_TRAINING, this.test.getStudentType());
	}

	@Test
	public void setStudentType_String() {
		this.test.setStudentType("continuous");
		assertSame(StudentType.CONTINUOUS, this.test.getStudentType());
		this.test.setStudentType("apprenticeship");
		assertSame(StudentType.APPRENTICESHIP, this.test.getStudentType());
	}

	@Test
	public void setStudentType_String_null() {
		this.test.setStudentType(StudentType.CONTINUOUS);
		this.test.setStudentType((String) null);
		assertSame(StudentType.INITIAL_TRAINING, this.test.getStudentType());
	}

	@Test
	public void setStudentType_String_empty() {
		this.test.setStudentType(StudentType.CONTINUOUS);
		this.test.setStudentType("");
		assertSame(StudentType.INITIAL_TRAINING, this.test.getStudentType());
	}

	@Test
	public void getRole() {
		assertSame(TeacherRole.PARTICIPANT, this.test.getRole());
	}

	@Test
	public void setRole_TeacherRole() {
		this.test.setRole(TeacherRole.SUPERVISOR);
		assertSame(TeacherRole.SUPERVISOR, this.test.getRole());
		this.test.setRole(TeacherRole.PARTICIPANT);
		assertSame(TeacherRole.PARTICIPANT, this.test.getRole());
		this.test.setRole(TeacherRole.CREATOR);
		assertSame(TeacherRole.CREATOR, this.test.getRole());
	}

	@Test
	public void setRole_TeacherRole_null() {
		this.test.setRole(TeacherRole.SUPERVISOR);
		this.test.setRole((TeacherRole) null);
		assertSame(TeacherRole.PARTICIPANT, this.test.getRole());
	}

	@Test
	public void setRole_String() {
		this.test.setRole("supervisor");
		assertSame(TeacherRole.SUPERVISOR, this.test.getRole());
		this.test.setRole("creator");
		assertSame(TeacherRole.CREATOR, this.test.getRole());
	}

	@Test
	public void setRole_String_null() {
		this.test.setRole(TeacherRole.SUPERVISOR);
		this.test.setRole((String) null);
		assertSame(TeacherRole.PARTICIPANT, this.test.getRole());
	}

	@Test
	public void setRole_String_empty() {
		this.test.setRole(TeacherRole.SUPERVISOR);
		this.test.setRole("");
		assertSame(TeacherRole.PARTICIPANT, this.test.getRole());
	}


	@Test
	public void getLanguage() {
		assertSame(CountryCode.FRANCE, this.test.getLanguage());
	}

	@Test
	public void setLanguage_CountryCode() {
		this.test.setLanguage(CountryCode.ZAMBIA);
		assertSame(CountryCode.ZAMBIA, this.test.getLanguage());
		this.test.setLanguage(CountryCode.ARGENTINA);
		assertSame(CountryCode.ARGENTINA, this.test.getLanguage());
		this.test.setLanguage(CountryCode.ALBANIA);
		assertSame(CountryCode.ALBANIA, this.test.getLanguage());
	}

	@Test
	public void setLanguage_CountryCode_null() {
		this.test.setLanguage(CountryCode.ANDORRA);
		this.test.setLanguage((CountryCode) null);
		assertSame(CountryCode.FRANCE, this.test.getLanguage());
	}

	@Test
	public void setLanguage_String() {
		this.test.setLanguage("italy");
		assertSame(CountryCode.ITALY, this.test.getLanguage());
		this.test.setLanguage("albania");
		assertSame(CountryCode.ALBANIA, this.test.getLanguage());
	}

	@Test
	public void setLanguage_String_null() {
		this.test.setLanguage(CountryCode.UNITED_STATES);
		this.test.setLanguage((String) null);
		assertSame(CountryCode.FRANCE, this.test.getLanguage());
	}

	@Test
	public void setLanguage_String_empty() {
		this.test.setLanguage(CountryCode.UNITED_KINGDOM);
		this.test.setLanguage("");
		assertSame(CountryCode.FRANCE, this.test.getLanguage());
	}

	@Test
	public void getExplanation() {
		assertNull(this.test.getExplanation());
	}

	@Test
	public void setExplanation() {
		this.test.setExplanation("xyz");
		assertEquals("xyz", this.test.getExplanation());
		this.test.setExplanation("abc");
		assertEquals("abc", this.test.getExplanation());
	}

	@Test
	public void setExplanation_null() {
		this.test.setExplanation("xyz");
		this.test.setExplanation(null);
		assertNull(this.test.getExplanation());
	}

	@Test
	public void setExplanation_empty() {
		this.test.setExplanation("xyz");
		this.test.setExplanation("");
		assertNull(this.test.getExplanation());
	}


	@Test
	public void getDegree() {
		assertNull(this.test.getDegree());
	}

	@Test
	public void setDegree() {
		this.test.setDegree("xyz");
		assertEquals("xyz", this.test.getDegree());
		this.test.setDegree("abc");
		assertEquals("abc", this.test.getDegree());
	}

	@Test
	public void setDegree_null() {
		this.test.setDegree("xyz");
		this.test.setDegree(null);
		assertNull(this.test.getDegree());
	}

	@Test
	public void setDegree_empty() {
		this.test.setDegree("xyz");
		this.test.setDegree("");
		assertNull(this.test.getDegree());
	}

	@Test
	public void getStartDate() {
		assertNull(this.test.getStartDate());
	}

	@Test
	public void setStartDate_LocalDate() {
		this.test.setStartDate(LocalDate.of(2023, 1, 12));
		assertEquals(LocalDate.of(2023, 1,12), this.test.getStartDate());
	}

	@Test
	public void setStartDate_LocalDate_null() {
		this.test.setStartDate(LocalDate.of(2023, 1, 12));
		this.test.setStartDate((LocalDate) null);
		assertNull(this.test.getStartDate());
	}

	@Test
	public void setStartDate_String() {
		this.test.setStartDate(LocalDate.of(2023, 1, 24));
		this.test.setStartDate("2023-01-12");
		assertEquals(LocalDate.of(2023, 1,12), this.test.getStartDate());
	}

	@Test
	public void setStartDate_String_null() {
		this.test.setStartDate(LocalDate.of(2023, 1, 24));
		this.test.setStartDate((String) null);
		assertNull(this.test.getStartDate());
	}

	@Test
	public void setStartDate_String_empty() {
		this.test.setStartDate(LocalDate.of(2023, 1, 24));
		this.test.setStartDate("");
		assertNull(this.test.getStartDate());
	}

	@Test
	public void setStartDate_String_invalid() {
		this.test.setStartDate(LocalDate.of(2023, 1, 24));
		this.test.setStartDate("214/dfrd-dgf");
		assertNull(this.test.getStartDate());
	}


	@Test
	public void getEndDate() {
		assertNull(this.test.getEndDate());
	}

	@Test
	public void setEndDate_LocalDate() {
		this.test.setEndDate(LocalDate.of(2023, 1, 12));
		assertEquals(LocalDate.of(2023, 1,12), this.test.getEndDate());
	}

	@Test
	public void setEndDate_LocalDate_null() {
		this.test.setEndDate(LocalDate.of(2023, 1, 12));
		this.test.setEndDate((LocalDate) null);
		assertNull(this.test.getEndDate());
	}

	@Test
	public void setEndDate_String() {
		this.test.setEndDate(LocalDate.of(2023, 1, 24));
		this.test.setEndDate("2023-01-12");
		assertEquals(LocalDate.of(2023, 1,12), this.test.getEndDate());
	}

	@Test
	public void setEndDate_String_null() {
		this.test.setEndDate(LocalDate.of(2023, 1, 24));
		this.test.setEndDate((String) null);
		assertNull(this.test.getEndDate());
	}

	@Test
	public void setEndDate_String_empty() {
		this.test.setEndDate(LocalDate.of(2023, 1, 24));
		this.test.setEndDate("");
		assertNull(this.test.getEndDate());
	}

	@Test
	public void setEndDate_String_invalid() {
		this.test.setEndDate(LocalDate.of(2023, 1, 24));
		this.test.setEndDate("214/dfrd-dgf");
		assertNull(this.test.getEndDate());
	}

	@Test
	public void getActivityUrl() {
		assertNull(this.test.getActivityUrl());
		assertNull(this.test.getActivityUrlObject());
	}

	@Test
	public void setActivityUrl_String() throws Exception {
		this.test.setActivityUrl("http://xyz.com");
		assertEquals("http://xyz.com", this.test.getActivityUrl());
		assertEquals(new URL("http://xyz.com"), this.test.getActivityUrlObject());
	}

	@Test
	public void setActivityUrl_String_null() {
		this.test.setActivityUrl("http://xyz.com");
		this.test.setActivityUrl((String) null);
		assertNull(this.test.getActivityUrl());
		assertNull(this.test.getActivityUrlObject());
	}

	@Test
	public void setActivityUrl_String_empty() {
		this.test.setActivityUrl("http://xyz.com");
		this.test.setActivityUrl("");
		assertNull(this.test.getActivityUrl());
		assertNull(this.test.getActivityUrlObject());
	}

	@Test
	public void setActivityUrl_String_invalid() {
		this.test.setActivityUrl("http://xyz.com");
		this.test.setActivityUrl("//////");
		assertEquals("//////", this.test.getActivityUrl());
		assertNull(this.test.getActivityUrlObject());
	}

	@Test
	public void setActivityUrl_URL() throws Exception {
		this.test.setActivityUrl(new URL("http://xyz.com"));
		assertEquals("http://xyz.com", this.test.getActivityUrl());
		assertEquals(new URL("http://xyz.com"), this.test.getActivityUrlObject());
	}

	@Test
	public void setActivityUrl_URL_null() {
		this.test.setActivityUrl("http://xyz.com");
		this.test.setActivityUrl((URL) null);
		assertNull(this.test.getActivityUrl());
		assertNull(this.test.getActivityUrlObject());
	}

	@Test
	public void getSourceUrl() {
		assertNull(this.test.getSourceUrl());
		assertNull(this.test.getSourceUrlObject());
	}

	@Test
	public void setSourceUrl_String() throws Exception {
		this.test.setSourceUrl("http://xyz.com");
		assertEquals("http://xyz.com", this.test.getSourceUrl());
		assertEquals(new URL("http://xyz.com"), this.test.getSourceUrlObject());
	}

	@Test
	public void setSourceUrl_String_null() {
		this.test.setSourceUrl("http://xyz.com");
		this.test.setSourceUrl((String) null);
		assertNull(this.test.getSourceUrl());
		assertNull(this.test.getSourceUrlObject());
	}

	@Test
	public void setSourceUrl_String_empty() {
		this.test.setSourceUrl("http://xyz.com");
		this.test.setSourceUrl("");
		assertNull(this.test.getSourceUrl());
		assertNull(this.test.getSourceUrlObject());
	}

	@Test
	public void setSourceUrl_String_invalid() {
		this.test.setSourceUrl("http://xyz.com");
		this.test.setSourceUrl("//////");
		assertEquals("//////", this.test.getSourceUrl());
		assertNull(this.test.getSourceUrlObject());
	}

	@Test
	public void setSourceUrl_URL() throws Exception {
		this.test.setSourceUrl(new URL("http://xyz.com"));
		assertEquals("http://xyz.com", this.test.getSourceUrl());
		assertEquals(new URL("http://xyz.com"), this.test.getSourceUrlObject());
	}

	@Test
	public void setSourceUrl_URL_null() {
		this.test.setSourceUrl("http://xyz.com");
		this.test.setSourceUrl((URL) null);
		assertNull(this.test.getSourceUrl());
		assertNull(this.test.getSourceUrlObject());
	}

	@Test
	public void getPathToSlides() {
		assertNull(this.test.getPathToSlides());
	}

	@Test
	public void setPathToSlides() {
		this.test.setPathToSlides("xyz");
		assertEquals("xyz", this.test.getPathToSlides());
		this.test.setPathToSlides("abc");
		assertEquals("abc", this.test.getPathToSlides());
	}

	@Test
	public void setPathToSlides_null() {
		this.test.setPathToSlides("xyz");
		assertEquals("xyz", this.test.getPathToSlides());
		this.test.setPathToSlides(null);
		assertNull(this.test.getPathToSlides());
	}

	@Test
	public void setPathToSlides_empty() {
		this.test.setPathToSlides("xyz");
		assertEquals("xyz", this.test.getPathToSlides());
		this.test.setPathToSlides("");
		assertNull(this.test.getPathToSlides());
	}

	@Test
	public void isDifferentHetdForTdTp() {
		assertFalse(this.test.isDifferentHetdForTdTp());
	}

	@Test
	public void setDifferentHetdForTdTp_boolean() {
		this.test.setDifferentHetdForTdTp(true);
		assertTrue(this.test.isDifferentHetdForTdTp());
		this.test.setDifferentHetdForTdTp(false);
		assertFalse(this.test.isDifferentHetdForTdTp());
		this.test.setDifferentHetdForTdTp(true);
		assertTrue(this.test.isDifferentHetdForTdTp());
	}

	@Test
	public void setDifferentHetdForTdTp_Boolean() {
		this.test.setDifferentHetdForTdTp(Boolean.TRUE);
		assertTrue(this.test.isDifferentHetdForTdTp());
		this.test.setDifferentHetdForTdTp(Boolean.FALSE);
		assertFalse(this.test.isDifferentHetdForTdTp());
		this.test.setDifferentHetdForTdTp(Boolean.TRUE);
		assertTrue(this.test.isDifferentHetdForTdTp());
	}

	@Test
	public void setDifferentHetdForTdTp_Boolean_null() {
		this.test.setDifferentHetdForTdTp(Boolean.TRUE);
		this.test.setDifferentHetdForTdTp(null);
		assertFalse(this.test.isDifferentHetdForTdTp());
	}

	@Test
	public void getNumberOfStudents() {
		assertEquals(0, this.test.getNumberOfStudents());
	}

	@Test
	public void setNumberOfStudents_int() {
		this.test.setNumberOfStudents(125445);
		assertEquals(125445, this.test.getNumberOfStudents());
		this.test.setNumberOfStudents(0);
		assertEquals(0, this.test.getNumberOfStudents());
		this.test.setNumberOfStudents(457);
		assertEquals(457, this.test.getNumberOfStudents());
		this.test.setNumberOfStudents(-457);
		assertEquals(0, this.test.getNumberOfStudents());
	}

	@Test
	public void setNumberOfStudents_Number() {
		this.test.setNumberOfStudents(Integer.valueOf(125445));
		assertEquals(125445, this.test.getNumberOfStudents());
		this.test.setNumberOfStudents(Integer.valueOf(0));
		assertEquals(0, this.test.getNumberOfStudents());
		this.test.setNumberOfStudents(Integer.valueOf(457));
		assertEquals(457, this.test.getNumberOfStudents());
		this.test.setNumberOfStudents(Integer.valueOf(-457));
		assertEquals(0, this.test.getNumberOfStudents());
	}

	@Test
	public void setNumberOfStudents_Number_null() {
		this.test.setNumberOfStudents(Integer.valueOf(125445));
		this.test.setNumberOfStudents(null);
		assertEquals(0, this.test.getNumberOfStudents());
	}

	@Test
	public void getAnnualWorkPerType() {
		assertTrue(this.test.getAnnualWorkPerType().isEmpty());
	}

	@Test
	public void setAnnualWorkPerType() {
		this.test.setAnnualWorkPerType(setupHours());
		//
		Map<TeachingActivityType, Float> actual = this.test.getAnnualWorkPerType();
		assertEquals(2, actual.size());
		assertTrue(actual.containsKey(TeachingActivityType.LECTURES));
		assertEquals(5f, actual.get(TeachingActivityType.LECTURES));
		assertTrue(actual.containsKey(TeachingActivityType.PRACTICAL_WORKS));
		assertEquals(4.5f, actual.get(TeachingActivityType.PRACTICAL_WORKS));
	}

	@Test
	public void setAnnualWorkPerType_null() {
		this.test.setAnnualWorkPerType(setupHours());
		//
		this.test.setAnnualWorkPerType(null);
		//
		Map<TeachingActivityType, Float> actual = this.test.getAnnualWorkPerType();
		assertTrue(actual.isEmpty());
	}

	private static Map<TeachingActivityType, Float> setupHours() {
		Map<TeachingActivityType, Float> map = new HashMap<>();
		map.put(TeachingActivityType.PRACTICAL_WORKS, Float.valueOf(4.5f));
		map.put(TeachingActivityType.LECTURES, Float.valueOf(5f));
		return map;
	}

	@Test
	public void getAnnualTotalHours() {
		this.test.setAnnualWorkPerType(setupHours());
		assertEquals(9.5f, this.test.getAnnualTotalHours());
	}
	
	@Test
	public void getAnnualTotalHetd_fullTime() {
		this.test.setDifferentHetdForTdTp(false);
		this.test.setAnnualWorkPerType(setupHours());
		assertEquals(12f, this.test.getAnnualTotalHetd());
	}

	@Test
	public void getAnnualTotalHetd_partTime() {
		this.test.setDifferentHetdForTdTp(true);
		this.test.setAnnualWorkPerType(setupHours());
		assertEquals(10.5f, this.test.getAnnualTotalHetd());
	}

	@Test
	public void isActive_LocalDate_beforeS() {
		this.test.setStartDate(LocalDate.of(2023, 1, 10));
		this.test.setEndDate(LocalDate.of(2023, 5, 15));
		//
		assertFalse(this.test.isActive(LocalDate.of(2022, 1, 10)));
	}

	@Test
	public void isActive_LocalDate_S() {
		this.test.setStartDate(LocalDate.of(2023, 1, 10));
		this.test.setEndDate(LocalDate.of(2023, 5, 15));
		//
		assertTrue(this.test.isActive(LocalDate.of(2023, 1, 10)));
	}

	@Test
	public void isActive_LocalDate_inSE() {
		this.test.setStartDate(LocalDate.of(2023, 1, 10));
		this.test.setEndDate(LocalDate.of(2023, 5, 15));
		//
		assertTrue(this.test.isActive(LocalDate.of(2023, 3, 18)));
	}

	@Test
	public void isActive_LocalDate_E() {
		this.test.setStartDate(LocalDate.of(2023, 1, 10));
		this.test.setEndDate(LocalDate.of(2023, 5, 15));
		//
		assertTrue(this.test.isActive(LocalDate.of(2023, 5, 15)));
	}

	@Test
	public void isActive_LocalDate_afterE() {
		this.test.setStartDate(LocalDate.of(2023, 1, 10));
		this.test.setEndDate(LocalDate.of(2023, 5, 15));
		//
		assertFalse(this.test.isActive(LocalDate.of(2024, 5, 15)));
	}

	@Test
	public void getFirstUrl_none() {
		assertNull(this.test.getFirstUrl());
	}

	@Test
	public void getFirstUrl_activity() {
		this.test.setActivityUrl("http://somewhere.com");
		assertEquals("http://somewhere.com", this.test.getFirstUrl());
	}

	@Test
	public void getFirstUrl_slides() {
		this.test.setPathToSlides("/path/to/slides");
		assertEquals("/path/to/slides", this.test.getFirstUrl());
	}

	@Test
	public void getFirstUrl_source() {
		this.test.setSourceUrl("https://github.com");
		assertEquals("https://github.com", this.test.getFirstUrl());
	}
	@Test
	public void getFirstUrl_activity_slides() {
		this.test.setActivityUrl("http://somewhere.com");
		this.test.setPathToSlides("/path/to/slides");
		assertEquals("http://somewhere.com", this.test.getFirstUrl());
	}

	@Test
	public void getFirstUrl_activity_source() {
		this.test.setActivityUrl("http://somewhere.com");
		this.test.setSourceUrl("https://github.com");
		assertEquals("http://somewhere.com", this.test.getFirstUrl());
	}

	@Test
	public void getFirstUrl_slides_source() {
		this.test.setPathToSlides("/path/to/slides");
		this.test.setSourceUrl("https://github.com");
		assertEquals("/path/to/slides", this.test.getFirstUrl());
	}

	@Test
	public void getFirstUrl_activity_slides_source() {
		this.test.setActivityUrl("http://somewhere.com");
		this.test.setPathToSlides("/path/to/slides");
		this.test.setSourceUrl("https://github.com");
		assertEquals("http://somewhere.com", this.test.getFirstUrl());
	}

	@Test
	public void getFirstUri_none() throws Exception {
		assertNull(this.test.getFirstUri());
	}

	@Test
	public void getFirstUri_activity() throws Exception {
		this.test.setActivityUrl("http://somewhere.com");
		assertEquals("http://somewhere.com", this.test.getFirstUri().toString());
	}

	@Test
	public void getFirstUri_slides() throws Exception {
		this.test.setPathToSlides("/path/to/slides");
		assertEquals("/path/to/slides", this.test.getFirstUri().toString());
	}

	@Test
	public void getFirstUri_source() throws Exception {
		this.test.setSourceUrl("https://github.com");
		assertEquals("https://github.com", this.test.getFirstUri().toString());
	}
	@Test
	public void getFirstUri_activity_slides() throws Exception {
		this.test.setActivityUrl("http://somewhere.com");
		this.test.setPathToSlides("/path/to/slides");
		assertEquals("http://somewhere.com", this.test.getFirstUri().toString());
	}

	@Test
	public void getFirstUri_activity_source() throws Exception {
		this.test.setActivityUrl("http://somewhere.com");
		this.test.setSourceUrl("https://github.com");
		assertEquals("http://somewhere.com", this.test.getFirstUri().toString());
	}

	@Test
	public void getFirstUri_slides_source() throws Exception {
		this.test.setPathToSlides("/path/to/slides");
		this.test.setSourceUrl("https://github.com");
		assertEquals("/path/to/slides", this.test.getFirstUri().toString());
	}

	@Test
	public void getFirstUri_activity_slides_source() throws Exception {
		this.test.setActivityUrl("http://somewhere.com");
		this.test.setPathToSlides("/path/to/slides");
		this.test.setSourceUrl("https://github.com");
		assertEquals("http://somewhere.com", this.test.getFirstUri().toString());
	}

}
