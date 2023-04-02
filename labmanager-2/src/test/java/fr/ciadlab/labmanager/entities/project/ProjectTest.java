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

package fr.ciadlab.labmanager.entities.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.scientificaxis.ScientificAxis;
import fr.ciadlab.labmanager.utils.funding.FundingScheme;
import fr.ciadlab.labmanager.utils.trl.TRL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link Project}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ProjectTest {

	private Project test;

	@BeforeEach
	public void setUp() {
		this.test = new Project();
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
	public void getScientificTitle() {
		assertNull(this.test.getScientificTitle());
	}

	@Test
	public void setScientificTitle_null() {
		this.test.setScientificTitle("xyz");
		this.test.setScientificTitle(null);
		assertNull(this.test.getScientificTitle());
	}

	@Test
	public void setScientificTitle_empty() {
		this.test.setScientificTitle("xyz");
		this.test.setScientificTitle("");
		assertNull(this.test.getScientificTitle());
	}

	@Test
	public void setScientificTitle() {
		this.test.setScientificTitle("xyz");
		assertEquals("xyz", this.test.getScientificTitle());

		this.test.setScientificTitle("abc");
		assertEquals("abc", this.test.getScientificTitle());
	}

	@Test
	public void getAcronym() {
		assertNull(this.test.getAcronym());
	}

	@Test
	public void setAcronym_null() {
		this.test.setAcronym("xyz");
		this.test.setAcronym(null);
		assertNull(this.test.getAcronym());
	}

	@Test
	public void setAcronym_empty() {
		this.test.setAcronym("xyz");
		this.test.setAcronym("");
		assertNull(this.test.getAcronym());
	}

	@Test
	public void setAcronym() {
		this.test.setAcronym("xyz");
		assertEquals("xyz", this.test.getAcronym());

		this.test.setAcronym("abc");
		assertEquals("abc", this.test.getAcronym());
	}

	@Test
	public void getDescription() {
		assertNull(this.test.getDescription());
	}

	@Test
	public void setDescription_null() {
		this.test.setDescription("xyz");
		this.test.setDescription(null);
		assertNull(this.test.getDescription());
	}

	@Test
	public void setDescription_empty() {
		this.test.setDescription("xyz");
		this.test.setDescription("");
		assertNull(this.test.getDescription());
	}

	@Test
	public void setDescription() {
		this.test.setDescription("xyz");
		assertEquals("xyz", this.test.getDescription());

		this.test.setDescription("abc");
		assertEquals("abc", this.test.getDescription());
	}

	@Test
	public void getAcronymOrScientificTitle_notNull() {
		this.test.setScientificTitle("xyz");
		this.test.setAcronym("abc");
		assertEquals("abc", this.test.getAcronymOrScientificTitle());
	}

	@Test
	public void getAcronymOrScientificTitle_nullName() {
		this.test.setScientificTitle(null);
		this.test.setAcronym("abc");
		assertEquals("abc", this.test.getAcronymOrScientificTitle());
	}

	@Test
	public void getAcronymOrScientificTitle_nullAcronym() {
		this.test.setScientificTitle("xyz");
		this.test.setAcronym(null);
		assertEquals("xyz", this.test.getAcronymOrScientificTitle());
	}

	@Test
	public void getAcronymOrScientificTitle_emptyName() {
		this.test.setScientificTitle("");
		this.test.setAcronym("abc");
		assertEquals("abc", this.test.getAcronymOrScientificTitle());
	}

	@Test
	public void getAcronymOrScientificTitle_emptyAcronym() {
		this.test.setScientificTitle("xyz");
		this.test.setAcronym("");
		assertEquals("xyz", this.test.getAcronymOrScientificTitle());
	}

	@Test
	public void getAcronymOrScientificTitle_allNull() {
		this.test.setScientificTitle(null);
		this.test.setAcronym(null);
		assertNull(this.test.getAcronymOrScientificTitle());
	}

	@Test
	public void getAcronymOrScientificTitle_allEmpty() {
		this.test.setScientificTitle("");
		this.test.setAcronym("");
		assertNull(this.test.getAcronymOrScientificTitle());
	}

	@Test
	public void getAcronymOrScientificTitle_nullempty() {
		this.test.setScientificTitle(null);
		this.test.setAcronym("");
		assertNull(this.test.getAcronymOrScientificTitle());
	}

	@Test
	public void getAcronymOrScientificTitle_emptynull() {
		this.test.setScientificTitle("");
		this.test.setAcronym(null);
		assertNull(this.test.getAcronymOrScientificTitle());
	}

	@Test
	public void getScientificTitleOrAcronym_notNull() {
		this.test.setScientificTitle("xyz");
		this.test.setAcronym("abc");
		assertEquals("xyz", this.test.getScientificTitleOrAcronym());
	}

	@Test
	public void getScientificTitleOrAcronym_nullName() {
		this.test.setScientificTitle(null);
		this.test.setAcronym("abc");
		assertEquals("abc", this.test.getScientificTitleOrAcronym());
	}

	@Test
	public void getScientificTitleOrAcronym_nullAcronym() {
		this.test.setScientificTitle("xyz");
		this.test.setAcronym(null);
		assertEquals("xyz", this.test.getScientificTitleOrAcronym());
	}

	@Test
	public void getScientificTitleOrAcronym_emptyName() {
		this.test.setScientificTitle("");
		this.test.setAcronym("abc");
		assertEquals("abc", this.test.getScientificTitleOrAcronym());
	}

	@Test
	public void getScientificTitleOrAcronym_emptyAcronym() {
		this.test.setScientificTitle("xyz");
		this.test.setAcronym("");
		assertEquals("xyz", this.test.getScientificTitleOrAcronym());
	}

	@Test
	public void getScientificTitleOrAcronym_allNull() {
		this.test.setScientificTitle(null);
		this.test.setAcronym(null);
		assertNull(this.test.getScientificTitleOrAcronym());
	}

	@Test
	public void getScientificTitleOrAcronym_allEmpty() {
		this.test.setScientificTitle("");
		this.test.setAcronym("");
		assertNull(this.test.getScientificTitleOrAcronym());
	}

	@Test
	public void getScientificTitleOrAcronym_nullempty() {
		this.test.setScientificTitle(null);
		this.test.setAcronym("");
		assertNull(this.test.getScientificTitleOrAcronym());
	}

	@Test
	public void getScientificTitleOrAcronym_emptynull() {
		this.test.setScientificTitle("");
		this.test.setAcronym(null);
		assertNull(this.test.getScientificTitleOrAcronym());
	}

	@Test
	public void getGlobalBudget() {
		assertEquals(0f, this.test.getGlobalBudget());
	}

	@Test
	public void setGlobalBudget_float() {
		this.test.setGlobalBudget(123.456f);
		assertEquals(123.456f, this.test.getGlobalBudget());

		this.test.setGlobalBudget(-123.456f);
		assertEquals(0f, this.test.getGlobalBudget());
	}

	@Test
	public void setGlobalBudget_Float_null() {
		this.test.setGlobalBudget(null);
		assertEquals(0f, this.test.getGlobalBudget());
	}

	@Test
	public void setGlobalBudget_Float() {
		this.test.setGlobalBudget(Float.valueOf(123.456f));
		assertEquals(123.456f, this.test.getGlobalBudget());

		this.test.setGlobalBudget(Float.valueOf(-123.456f));
		assertEquals(0f, this.test.getGlobalBudget());
	}

	@Test
	public void getContractType() {
		assertSame(ProjectContractType.NOT_SPECIFIED, this.test.getContractType());
	}

	@Test
	public void setContractType_ProjectContractType_null() {
		this.test.setContractType(ProjectContractType.PR);
		this.test.setContractType((ProjectContractType) null);
		assertSame(ProjectContractType.NOT_SPECIFIED, this.test.getContractType());
	}

	@Test
	public void setContractType_ProjectContractType() {
		this.test.setContractType(ProjectContractType.PI);
		assertSame(ProjectContractType.PI, this.test.getContractType());
		this.test.setContractType(ProjectContractType.PR);
		assertSame(ProjectContractType.PR, this.test.getContractType());
		this.test.setContractType(ProjectContractType.RCD);
		assertSame(ProjectContractType.RCD, this.test.getContractType());
		this.test.setContractType(ProjectContractType.RCO);
		assertSame(ProjectContractType.RCO, this.test.getContractType());
		this.test.setContractType(ProjectContractType.NOT_SPECIFIED);
		assertSame(ProjectContractType.NOT_SPECIFIED, this.test.getContractType());
	}

	@Test
	public void setContractType_String_null() {
		this.test.setContractType(ProjectContractType.PR);
		this.test.setContractType((String) null);
		assertSame(ProjectContractType.NOT_SPECIFIED, this.test.getContractType());
	}

	@Test
	public void setContractType_String_empty() {
		this.test.setContractType(ProjectContractType.PR);
		this.test.setContractType("");
		assertSame(ProjectContractType.NOT_SPECIFIED, this.test.getContractType());
	}

	@Test
	public void setContractType_String_valid() {
		this.test.setContractType("pr");
		assertSame(ProjectContractType.PR, this.test.getContractType());
		this.test.setContractType("pi");
		assertSame(ProjectContractType.PI, this.test.getContractType());
		this.test.setContractType("rco");
		assertSame(ProjectContractType.RCO, this.test.getContractType());
		this.test.setContractType("rcd");
		assertSame(ProjectContractType.RCD, this.test.getContractType());
		this.test.setContractType("not_specified");
		assertSame(ProjectContractType.NOT_SPECIFIED, this.test.getContractType());
	}

	@Test
	public void setContractType_String_invalid() {
		this.test.setContractType("xyz");
		assertSame(ProjectContractType.NOT_SPECIFIED, this.test.getContractType());
	}

	@Test
	public void getActivityType() {
		assertNull(this.test.getActivityType());
	}

	@Test
	public void setActivityType_ProjectActivityType_null() {
		this.test.setActivityType(ProjectActivityType.FUNDAMENTAL_RESEARCH);
		this.test.setActivityType((ProjectActivityType) null);
		assertNull(this.test.getActivityType());
	}

	@Test
	public void setActivityType_ProjectActivityType() {
		this.test.setActivityType(ProjectActivityType.FUNDAMENTAL_RESEARCH);
		assertSame(ProjectActivityType.FUNDAMENTAL_RESEARCH, this.test.getActivityType());
		this.test.setActivityType(ProjectActivityType.APPLIED_RESEARCH);
		assertSame(ProjectActivityType.APPLIED_RESEARCH, this.test.getActivityType());
		this.test.setActivityType(ProjectActivityType.EXPERIMENTAL_DEVELOPMENT);
		assertSame(ProjectActivityType.EXPERIMENTAL_DEVELOPMENT, this.test.getActivityType());
	}

	@Test
	public void setActivityType_String_null() {
		this.test.setActivityType(ProjectActivityType.FUNDAMENTAL_RESEARCH);
		this.test.setActivityType((String) null);
		assertNull(this.test.getActivityType());
	}

	@Test
	public void setActivityType_String_empty() {
		this.test.setActivityType(ProjectActivityType.FUNDAMENTAL_RESEARCH);
		this.test.setActivityType("");
		assertNull(this.test.getActivityType());
	}

	@Test
	public void setActivityType_String_valid() {
		this.test.setActivityType("fundamental_research");
		assertSame(ProjectActivityType.FUNDAMENTAL_RESEARCH, this.test.getActivityType());
		this.test.setActivityType("applied_research");
		assertSame(ProjectActivityType.APPLIED_RESEARCH, this.test.getActivityType());
		this.test.setActivityType("experimental_development");
		assertSame(ProjectActivityType.EXPERIMENTAL_DEVELOPMENT, this.test.getActivityType());
	}

	@Test
	public void setActivityType_String_invalid() {
		this.test.setActivityType("xyz");
		assertNull(this.test.getActivityType());
	}

	@Test
	public void getStatus() {
		assertNull(this.test.getStatus());
	}

	@Test
	public void setStatus_ProjectStatus_null() {
		this.test.setStatus(ProjectStatus.EVALUATION);
		this.test.setStatus((ProjectStatus) null);
		assertNull(this.test.getStatus());
	}

	@Test
	public void setStatus_ProjectStatus() {
		this.test.setStatus(ProjectStatus.CANCELED);
		assertSame(ProjectStatus.CANCELED, this.test.getStatus());
		this.test.setStatus(ProjectStatus.ACCEPTED);
		assertSame(ProjectStatus.ACCEPTED, this.test.getStatus());
		this.test.setStatus(ProjectStatus.EVALUATION);
		assertSame(ProjectStatus.EVALUATION, this.test.getStatus());
	}

	@Test
	public void setStatus_String_null() {
		this.test.setStatus(ProjectStatus.REJECTED);
		this.test.setStatus((String) null);
		assertNull(this.test.getStatus());
	}

	@Test
	public void setStatus_String_empty() {
		this.test.setStatus(ProjectStatus.EVALUATION);
		this.test.setStatus("");
		assertNull(this.test.getStatus());
	}

	@Test
	public void setStatus_String_valid() {
		this.test.setStatus("rejected");
		assertSame(ProjectStatus.REJECTED, this.test.getStatus());
		this.test.setStatus("evaluation");
		assertSame(ProjectStatus.EVALUATION, this.test.getStatus());
		this.test.setStatus("canceled");
		assertSame(ProjectStatus.CANCELED, this.test.getStatus());
	}

	@Test
	public void setStatus_String_invalid() {
		this.test.setStatus("xyz");
		assertNull(this.test.getStatus());
	}

	@Test
	public void getProjectURL() {
		assertNull(this.test.getProjectURL());
	}

	@Test
	public void getProjectURLObject() {
		assertNull(this.test.getProjectURLObject());
	}

	@Test
	public void setProjectURL_String_null() {
		this.test.setProjectURL("xyz");
		this.test.setProjectURL((String) null);
		assertNull(this.test.getProjectURL());
	}

	@Test
	public void setProjectURL_String_empty() {
		this.test.setProjectURL("xyz");
		this.test.setProjectURL("");
		assertNull(this.test.getProjectURL());
	}

	@Test
	public void setProjectURL_String() {
		this.test.setProjectURL("xyz");
		this.test.setProjectURL("http://www.ciad-lab.fr");
		assertEquals("http://www.ciad-lab.fr", this.test.getProjectURL());
	}

	@Test
	public void setProjectURL_URL_null() {
		this.test.setProjectURL("xyz");
		this.test.setProjectURL((URL) null);
		assertNull(this.test.getProjectURL());
	}

	@Test
	public void setProjectURL_URL() throws Exception {
		this.test.setProjectURL("xyz");
		this.test.setProjectURL(new URL("http://www.ciad-lab.fr"));
		assertEquals("http://www.ciad-lab.fr", this.test.getProjectURL());
	}

	@Test
	public void getVideoURLs() {
		assertTrue(this.test.getVideoURLs().isEmpty());
	}

	@Test
	public void getVideoURLsObject() {
		assertTrue(this.test.getVideoURLsObject().isEmpty());
	}

	@Test
	public void setVideoURLs_null() {
		this.test.setVideoURLs(Collections.singletonList("xyz"));
		this.test.setVideoURLs((List<String>) null);
		assertTrue(this.test.getVideoURLs().isEmpty());
	}

	@Test
	public void setVideoURLs_empty() {
		this.test.setVideoURLs(Collections.singletonList("xyz"));
		this.test.setVideoURLs(Collections.emptyList());
		assertTrue(this.test.getVideoURLs().isEmpty());
	}

	@Test
	public void setVideoURLs() {
		this.test.setVideoURLs(Collections.singletonList("xyz"));
		this.test.setVideoURLs(Collections.singletonList("http://www.ciad-lab.fr"));
		List<String> list = this.test.getVideoURLs();
		assertEquals(1, list.size());
		assertTrue(list.contains("http://www.ciad-lab.fr"));
	}

	@Test
	public void setVideoURLsObject_null() {
		this.test.setVideoURLs(Collections.singletonList("xyz"));
		this.test.setVideoURLsObject((List<URL>) null);
		assertTrue(this.test.getVideoURLs().isEmpty());
	}

	@Test
	public void setVideoURLsObject() throws Exception {
		this.test.setVideoURLs(Collections.singletonList("xyz"));
		this.test.setVideoURLsObject(Collections.singletonList(new URL("http://www.ciad-lab.fr")));
		List<String> list = this.test.getVideoURLs();
		assertEquals(1, list.size());
		assertTrue(list.contains("http://www.ciad-lab.fr"));
	}

	@Test
	public void getPathToScientificRequirements() {
		assertNull(this.test.getPathToScientificRequirements());
	}

	@Test
	public void setPathToScientificRequirements_null() {
		this.test.setPathToScientificRequirements("xyz");
		this.test.setPathToScientificRequirements(null);
		assertNull(this.test.getPathToScientificRequirements());
	}

	@Test
	public void setPathToScientificRequirements_empty() {
		this.test.setPathToScientificRequirements("xyz");
		this.test.setPathToScientificRequirements("");
		assertNull(this.test.getPathToScientificRequirements());
	}

	@Test
	public void setPathToScientificRequirements() {
		this.test.setPathToScientificRequirements("xyz");
		assertEquals("xyz", this.test.getPathToScientificRequirements());
	}

	@Test
	public void getPathToPressDocument() {
		assertNull(this.test.getPathToPressDocument());
	}

	@Test
	public void setPathToPressDocument_null() {
		this.test.setPathToPressDocument("xyz");
		this.test.setPathToPressDocument(null);
		assertNull(this.test.getPathToPressDocument());
	}

	@Test
	public void setPathToPressDocument_empty() {
		this.test.setPathToPressDocument("xyz");
		this.test.setPathToPressDocument("");
		assertNull(this.test.getPathToPressDocument());
	}

	@Test
	public void setPathToPressDocument() {
		this.test.setPathToPressDocument("xyz");
		assertEquals("xyz", this.test.getPathToPressDocument());
	}

	@Test
	public void getPathToLogo() {
		assertNull(this.test.getPathToLogo());
	}

	@Test
	public void setPathToLogo_null() {
		this.test.setPathToLogo("xyz");
		this.test.setPathToLogo(null);
		assertNull(this.test.getPathToLogo());
	}

	@Test
	public void setPathToLogo_empty() {
		this.test.setPathToLogo("xyz");
		this.test.setPathToLogo("");
		assertNull(this.test.getPathToLogo());
	}

	@Test
	public void setPathToLogo() {
		this.test.setPathToLogo("xyz");
		assertEquals("xyz", this.test.getPathToLogo());
	}

	@Test
	public void getPathsToImages() {
		assertTrue(this.test.getPathsToImages().isEmpty());
	}

	@Test
	public void setPathsToImages_null() {
		this.test.setPathsToImages(Collections.singletonList("xyz"));
		this.test.setPathsToImages(null);
		assertTrue(this.test.getPathsToImages().isEmpty());
	}

	@Test
	public void setPathsToImages_empty() {
		this.test.setPathsToImages(Collections.singletonList("xyz"));
		this.test.setPathsToImages(Collections.emptyList());
		assertTrue(this.test.getPathsToImages().isEmpty());
	}

	@Test
	public void setPathsToImages() {
		this.test.setPathsToImages(Collections.singletonList("xyz"));
		List<String> paths = this.test.getPathsToImages();
		assertEquals(1, paths.size());
		assertTrue(this.test.getPathsToImages().contains("xyz"));
	}

	@Test
	public void getPathToPowerpoint() {
		assertNull(this.test.getPathToPowerpoint());
	}

	@Test
	public void setPathToPowerpoint_null() {
		this.test.setPathToPowerpoint("xyz");
		this.test.setPathToPowerpoint(null);
		assertNull(this.test.getPathToPowerpoint());
	}

	@Test
	public void setPathToPowerpoint_empty() {
		this.test.setPathToPowerpoint("xyz");
		this.test.setPathToPowerpoint("");
		assertNull(this.test.getPathToPowerpoint());
	}

	@Test
	public void setPathToPowerpoint() {
		this.test.setPathToPowerpoint("xyz");
		assertEquals("xyz", this.test.getPathToPowerpoint());
	}

	@Test
	public void getTRL() {
		assertNull(this.test.getTRL());
	}

	@Test
	public void setTRL_TRL_null() {
		this.test.setTRL(TRL.TRL4);
		this.test.setTRL((TRL) null);
		assertNull(this.test.getTRL());
	}

	@Test
	public void setTRL_TRL() {
		this.test.setTRL(TRL.TRL4);
		assertSame(TRL.TRL4, this.test.getTRL());
	}

	@Test
	public void setTRL_String_null() {
		this.test.setTRL(TRL.TRL5);
		this.test.setTRL((String) null);
		assertNull(this.test.getTRL());
	}

	@Test
	public void setTRL_String_empty() {
		this.test.setTRL(TRL.TRL9);
		this.test.setTRL("");
		assertNull(this.test.getTRL());
	}

	@Test
	public void setTRL_String_validString() {
		this.test.setTRL("trl5");
		assertSame(TRL.TRL5, this.test.getTRL());
		this.test.setTRL("TRL3");
		assertSame(TRL.TRL3, this.test.getTRL());
	}

	@Test
	public void setTRL_String_validNumber() {
		this.test.setTRL("5");
		assertSame(TRL.TRL5, this.test.getTRL());
		this.test.setTRL("3");
		assertSame(TRL.TRL3, this.test.getTRL());
	}

	@Test
	public void setTRL_String_invalid() {
		this.test.setTRL("xyz");
		assertNull(this.test.getTRL());
	}

	@Test
	public void isConfidential() {
		assertFalse(this.test.isConfidential());
	}

	@Test
	public void setConfidential_boolean() {
		this.test.setConfidential(true);
		assertTrue(this.test.isConfidential());
		this.test.setConfidential(false);
		assertFalse(this.test.isConfidential());
	}

	@Test
	public void setConfidential_Boolean_null() {
		this.test.setConfidential(true);
		this.test.setConfidential(null);
		assertFalse(this.test.isConfidential());
	}

	@Test
	public void setConfidential_Boolean() {
		this.test.setConfidential(Boolean.TRUE);
		assertTrue(this.test.isConfidential());
		this.test.setConfidential(Boolean.FALSE);
		assertFalse(this.test.isConfidential());
	}

	@Test
	public void isOpenSource() {
		assertFalse(this.test.isOpenSource());
	}

	@Test
	public void setOpenSource_boolean() {
		this.test.setOpenSource(true);
		assertTrue(this.test.isOpenSource());
		this.test.setOpenSource(false);
		assertFalse(this.test.isOpenSource());
	}

	@Test
	public void setOpenSource_Boolean_null() {
		this.test.setOpenSource(true);
		this.test.setOpenSource(null);
		assertFalse(this.test.isOpenSource());
	}

	@Test
	public void setOpenSource_Boolean() {
		this.test.setOpenSource(Boolean.TRUE);
		assertTrue(this.test.isOpenSource());
		this.test.setOpenSource(Boolean.FALSE);
		assertFalse(this.test.isOpenSource());
	}

	@Test
	public void isValidated() {
		assertFalse(this.test.isValidated());
	}

	@Test
	public void setValidated_boolean() {
		this.test.setValidated(true);
		assertTrue(this.test.isValidated());
		this.test.setValidated(false);
		assertFalse(this.test.isValidated());
	}

	@Test
	public void setValidated_Boolean_null() {
		this.test.setValidated(true);
		this.test.setValidated(null);
		assertFalse(this.test.isValidated());
	}

	@Test
	public void setValidated_Boolean() {
		this.test.setValidated(Boolean.TRUE);
		assertTrue(this.test.isValidated());
		this.test.setValidated(Boolean.FALSE);
		assertFalse(this.test.isValidated());
	}

	@Test
	public void getStartDate() {
		assertNull(this.test.getStartDate());
	}

	@Test
	public void setStartDate_LocalDate_null() {
		this.test.setStartDate(LocalDate.of(2023,  1,  1));
		this.test.setStartDate((LocalDate) null);
		assertNull(this.test.getStartDate());
	}

	@Test
	public void setStartDate_LocalDate() {
		this.test.setStartDate(LocalDate.of(2023,  1,  1));
		assertEquals(LocalDate.parse("2023-01-01"), this.test.getStartDate());
	}

	@Test
	public void setStartDate_String_null() {
		assertThrows(IllegalArgumentException.class, () -> this.test.setStartDate((String) null));
	}

	@Test
	public void setStartDate_String_empty() {
		assertThrows(IllegalArgumentException.class, () -> this.test.setStartDate(""));
	}

	@Test
	public void setStartDate_String() {
		this.test.setStartDate("2023-01-01");
		assertEquals(LocalDate.of(2023,  1,  1), this.test.getStartDate());
	}

	@Test
	public void getDuration() {
		assertEquals(1, this.test.getDuration());
	}

	@Test
	public void setDuration_int() {
		this.test.setDuration(123456);
		assertEquals(123456, this.test.getDuration());
		this.test.setDuration(-15);
		assertEquals(0, this.test.getDuration());
		this.test.setDuration(0);
		assertEquals(0, this.test.getDuration());
		this.test.setDuration(5);
		assertEquals(5, this.test.getDuration());
	}

	@Test
	public void setDuration_Integer_null() {
		this.test.setDuration(123456);
		this.test.setDuration(null);
		assertEquals(0, this.test.getDuration());
	}

	@Test
	public void setDuration_Integer() {
		this.test.setDuration(Integer.valueOf(123456));
		assertEquals(123456, this.test.getDuration());
		this.test.setDuration(Integer.valueOf(-15));
		assertEquals(0, this.test.getDuration());
		this.test.setDuration(Integer.valueOf(-1));
		assertEquals(0, this.test.getDuration());
		this.test.setDuration(Integer.valueOf(0));
		assertEquals(0, this.test.getDuration());
		this.test.setDuration(Integer.valueOf(5));
		assertEquals(5, this.test.getDuration());
	}

	@Test
	public void getEndDate_noStart() {
		LocalDate dt = this.test.getEndDate();
		assertNull(dt);
	}
	
	@Test
	public void getEndDate() {
		this.test.setStartDate("2023-01-12");
		this.test.setDuration(6);
		assertEquals(LocalDate.of(2023, 07, 12), this.test.getEndDate());
	}

	@Test
	public void getStartYear_noStart() {
		assertEquals(0, this.test.getStartYear());
	}
	
	@Test
	public void getStartYear() {
		this.test.setStartDate("2023-01-12");
		assertEquals(2023, this.test.getStartYear());
	}

	@Test
	public void getEndYear_noStart() {
		assertEquals(0, this.test.getEndYear());
	}
	
	@Test
	public void getEndYear() {
		this.test.setStartDate("2023-01-12");
		this.test.setDuration(6);
		assertEquals(2023, this.test.getEndYear());
	}

	@Test
	public void getCoordinator() {
		assertNull(this.test.getCoordinator());
	}

	@Test
	public void setCoordinator_null() {
		ResearchOrganization orga = mock(ResearchOrganization.class);
		this.test.setCoordinator(orga);
		this.test.setCoordinator(null);
		assertNull(this.test.getCoordinator());
	}

	@Test
	public void setCoordinator() {
		ResearchOrganization orga = mock(ResearchOrganization.class);
		this.test.setCoordinator(orga);
		assertSame(orga, this.test.getCoordinator());
	}

	@Test
	public void getLocalOrganization() {
		assertNull(this.test.getLocalOrganization());
	}

	@Test
	public void setLocalOrganization_null() {
		ResearchOrganization orga = mock(ResearchOrganization.class);
		this.test.setLocalOrganization(orga);
		this.test.setLocalOrganization(null);
		assertNull(this.test.getLocalOrganization());
	}

	@Test
	public void setLocalOrganization() {
		ResearchOrganization orga = mock(ResearchOrganization.class);
		this.test.setLocalOrganization(orga);
		assertSame(orga, this.test.getLocalOrganization());
	}

	@Test
	public void getSuperOrganization() {
		assertNull(this.test.getSuperOrganization());
	}

	@Test
	public void setSuperOrganization_null() {
		ResearchOrganization orga = mock(ResearchOrganization.class);
		this.test.setSuperOrganization(orga);
		this.test.setSuperOrganization(null);
		assertNull(this.test.getSuperOrganization());
	}

	@Test
	public void setSuperOrganization() {
		ResearchOrganization orga = mock(ResearchOrganization.class);
		this.test.setSuperOrganization(orga);
		assertSame(orga, this.test.getSuperOrganization());
	}

	@Test
	public void getLearOrganization() {
		assertNull(this.test.getLearOrganization());
	}

	@Test
	public void setLearOrganization_null() {
		ResearchOrganization orga = mock(ResearchOrganization.class);
		this.test.setLearOrganization(orga);
		this.test.setLearOrganization(null);
		assertNull(this.test.getLearOrganization());
	}

	@Test
	public void setLearOrganization() {
		ResearchOrganization orga = mock(ResearchOrganization.class);
		this.test.setLearOrganization(orga);
		assertSame(orga, this.test.getLearOrganization());
	}

	@Test
	public void getOtherPartners() {
		assertTrue(this.test.getOtherPartners().isEmpty());
	}

	@Test
	public void getOtherPartnersRaw() {
		Set<ResearchOrganization> list = this.test.getOtherPartnersRaw();
		assertNotNull(list);
		assertTrue(list.isEmpty());
	}

	@Test
	public void setOtherPartners_null() {
		ResearchOrganization o1 = mock(ResearchOrganization.class);
		ResearchOrganization o2 = mock(ResearchOrganization.class);
		Set<ResearchOrganization> list = new HashSet<>();
		list.add(o1);
		list.add(o2);
		this.test.setOtherPartners(list);

		this.test.setOtherPartners(null);

		Set<ResearchOrganization> actual = this.test.getOtherPartnersRaw();
		assertNotNull(actual);
		assertNotSame(list, actual);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void setOtherPartners() {
		ResearchOrganization o1 = mock(ResearchOrganization.class);
		ResearchOrganization o2 = mock(ResearchOrganization.class);
		Set<ResearchOrganization> list = new HashSet<>();
		list.add(o1);
		list.add(o2);

		this.test.setOtherPartners(list);

		Set<ResearchOrganization> actual = this.test.getOtherPartnersRaw();
		assertNotNull(actual);
		assertNotSame(list, actual);
		assertEquals(list, actual);
	}

	@Test
	public void getParticipants() {
		assertTrue(this.test.getParticipants().isEmpty());
	}

	@Test
	public void setParticipants_null() {
		ProjectMember m1 = mock(ProjectMember.class);
		ProjectMember m2 = mock(ProjectMember.class);
		List<ProjectMember> list = new ArrayList<>();
		list.add(m1);
		list.add(m2);
		this.test.setParticipants(list);

		this.test.setParticipants(null);

		List<ProjectMember> actual = this.test.getParticipants();
		assertNotNull(actual);
		assertNotSame(list, actual);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void setParticipants() {
		ProjectMember m1 = mock(ProjectMember.class);
		ProjectMember m2 = mock(ProjectMember.class);
		List<ProjectMember> list = new ArrayList<>();
		list.add(m1);
		list.add(m2);

		this.test.setParticipants(list);

		List<ProjectMember> actual = this.test.getParticipants();
		assertNotNull(actual);
		assertNotSame(list, actual);
		assertEquals(2, actual.size());
		assertTrue(actual.contains(m1));
		assertTrue(actual.contains(m2));
	}

	@Test
	public void addParticipant_null() {
		ProjectMember m1 = mock(ProjectMember.class);

		this.test.addParticipant(null);
		this.test.addParticipant(m1);
		this.test.addParticipant(null);

		List<ProjectMember> actual = this.test.getParticipants();
		assertNotNull(actual);
		assertEquals(1, actual.size());
		assertTrue(actual.contains(m1));
	}

	@Test
	public void addParticipant() {
		ProjectMember m1 = mock(ProjectMember.class);
		ProjectMember m2 = mock(ProjectMember.class);

		this.test.addParticipant(m2);
		this.test.addParticipant(m1);

		List<ProjectMember> actual = this.test.getParticipants();
		assertNotNull(actual);
		assertEquals(2, actual.size());
		assertTrue(actual.contains(m1));
		assertTrue(actual.contains(m2));
	}

	@Test
	public void removeParticipant_null() {
		ProjectMember m1 = mock(ProjectMember.class);
		ProjectMember m2 = mock(ProjectMember.class);
		this.test.addParticipant(m2);
		this.test.addParticipant(m1);

		this.test.removeParticipant(null);

		List<ProjectMember> actual = this.test.getParticipants();
		assertNotNull(actual);
		assertEquals(2, actual.size());
		assertTrue(actual.contains(m1));
		assertTrue(actual.contains(m2));
	}

	@Test
	public void removeParticipant_valid() {
		ProjectMember m1 = mock(ProjectMember.class);
		ProjectMember m2 = mock(ProjectMember.class);
		this.test.addParticipant(m2);
		this.test.addParticipant(m1);

		this.test.removeParticipant(m2);

		List<ProjectMember> actual = this.test.getParticipants();
		assertNotNull(actual);
		assertEquals(1, actual.size());
		assertTrue(actual.contains(m1));
	}

	@Test
	public void removeParticipant_invalid() {
		ProjectMember m1 = mock(ProjectMember.class);
		ProjectMember m2 = mock(ProjectMember.class);
		ProjectMember m3 = mock(ProjectMember.class);
		this.test.addParticipant(m2);
		this.test.addParticipant(m1);

		this.test.removeParticipant(m3);

		List<ProjectMember> actual = this.test.getParticipants();
		assertNotNull(actual);
		assertEquals(2, actual.size());
		assertTrue(actual.contains(m1));
		assertTrue(actual.contains(m2));
		assertFalse(actual.contains(m3));
	}

	@Test
	public void getMajorFundingScheme() {
		ProjectBudget b0 = mock(ProjectBudget.class);
		when(b0.getFundingScheme()).thenReturn(FundingScheme.CIFRE);
		ProjectBudget b1 = mock(ProjectBudget.class);
		when(b1.getFundingScheme()).thenReturn(FundingScheme.H2020);
		ProjectBudget b2 = mock(ProjectBudget.class);
		when(b2.getFundingScheme()).thenReturn(FundingScheme.CPER);
		this.test.setBudgets(Arrays.asList(b0, b1, b2));
		assertSame(FundingScheme.H2020, this.test.getMajorFundingScheme());
	}

	@Test
	public void getCategory_competitive_notOpen() {
		this.test.setContractType(ProjectContractType.RCO);
		this.test.setOpenSource(false);
		assertSame(ProjectCategory.COMPETITIVE_CALL_PROJECT, this.test.getCategory());
	}

	@Test
	public void getCategory_competitive_open() {
		this.test.setContractType(ProjectContractType.RCO);
		this.test.setOpenSource(true);
		assertSame(ProjectCategory.COMPETITIVE_CALL_PROJECT, this.test.getCategory());
	}

	@Test
	public void getCategory_notAcademic_notOpen_rcd() {
		this.test.setContractType(ProjectContractType.RCD);
		this.test.setOpenSource(false);
		assertSame(ProjectCategory.NOT_ACADEMIC_PROJECT, this.test.getCategory());
	}

	@Test
	public void getCategory_notAcademic_notOpen_pr() {
		this.test.setContractType(ProjectContractType.PR);
		this.test.setOpenSource(false);
		assertSame(ProjectCategory.NOT_ACADEMIC_PROJECT, this.test.getCategory());
	}

	@Test
	public void getCategory_notAcademic_notOpen_pi() {
		this.test.setContractType(ProjectContractType.PI);
		this.test.setOpenSource(false);
		assertSame(ProjectCategory.NOT_ACADEMIC_PROJECT, this.test.getCategory());
	}

	@Test
	public void getCategory_notAcademic_open_rcd() {
		this.test.setContractType(ProjectContractType.RCD);
		this.test.setOpenSource(true);
		assertSame(ProjectCategory.NOT_ACADEMIC_PROJECT, this.test.getCategory());
	}

	@Test
	public void getCategory_notAcademic_open_pr() {
		this.test.setContractType(ProjectContractType.PR);
		this.test.setOpenSource(true);
		assertSame(ProjectCategory.NOT_ACADEMIC_PROJECT, this.test.getCategory());
	}

	@Test
	public void getCategory_notAcademic_open_pi() {
		this.test.setContractType(ProjectContractType.PI);
		this.test.setOpenSource(true);
		assertSame(ProjectCategory.NOT_ACADEMIC_PROJECT, this.test.getCategory());
	}

	@Test
	public void getCategory_nothing_notOpen() {
		this.test.setOpenSource(false);
		assertSame(ProjectCategory.AUTO_FUNDING, this.test.getCategory());
	}

	@Test
	public void getCategory_nothing_open() {
		this.test.setOpenSource(true);
		assertSame(ProjectCategory.OPEN_SOURCE, this.test.getCategory());
	}

	@Test
	public void getBudgets_0() {
		List<ProjectBudget> budgets = this.test.getBudgets();
		assertNotNull(budgets);
		assertTrue(budgets.isEmpty());
	}

	@Test
	public void getBudgets_1() {
		ProjectBudget b0 = mock(ProjectBudget.class);
		this.test.setBudgets(Collections.singletonList(b0));
		//
		List<ProjectBudget> budgets = this.test.getBudgets();
		assertNotNull(budgets);
		assertEquals(1, budgets.size());
		assertTrue(budgets.contains(b0));
	}

	@Test
	public void getBudgets_2() {
		ProjectBudget b0 = mock(ProjectBudget.class);
		ProjectBudget b1 = mock(ProjectBudget.class);
		this.test.setBudgets(Arrays.asList(b0, b1));
		//
		List<ProjectBudget> budgets = this.test.getBudgets();
		assertNotNull(budgets);
		assertEquals(2, budgets.size());
		assertTrue(budgets.contains(b0));
		assertTrue(budgets.contains(b1));
	}

	@Test
	public void getBudgets_3() {
		ProjectBudget b0 = mock(ProjectBudget.class);
		ProjectBudget b1 = mock(ProjectBudget.class);
		ProjectBudget b2 = mock(ProjectBudget.class);
		this.test.setBudgets(Arrays.asList(b0, b1, b2));
		//
		List<ProjectBudget> budgets = this.test.getBudgets();
		assertNotNull(budgets);
		assertEquals(3, budgets.size());
		assertTrue(budgets.contains(b0));
		assertTrue(budgets.contains(b1));
		assertTrue(budgets.contains(b2));
	}

	@Test
	public void getTotalLocalOrganizationBudget_0() {
		float budget = this.test.getTotalLocalOrganizationBudget();
		assertEquals(0f, budget);
	}

	@Test
	public void getTotalLocalOrganizationBudget_1() {
		ProjectBudget b0 = mock(ProjectBudget.class);
		when(b0.getBudget()).thenReturn(123.456f);
		this.test.setBudgets(Collections.singletonList(b0));
		//
		float budget = this.test.getTotalLocalOrganizationBudget();
		assertEquals(123.456f, budget);
	}

	@Test
	public void getTotalLocalOrganizationBudget_2() {
		ProjectBudget b0 = mock(ProjectBudget.class);
		when(b0.getBudget()).thenReturn(123.456f);
		ProjectBudget b1 = mock(ProjectBudget.class);
		when(b1.getBudget()).thenReturn(458.741f);
		this.test.setBudgets(Arrays.asList(b0, b1));
		//
		float budget = this.test.getTotalLocalOrganizationBudget();
		assertEquals(582.197f, budget);
	}

	@Test
	public void getTotalLocalOrganizationBudget_3() {
		ProjectBudget b0 = mock(ProjectBudget.class);
		when(b0.getBudget()).thenReturn(123.456f);
		ProjectBudget b1 = mock(ProjectBudget.class);
		when(b1.getBudget()).thenReturn(458.741f);
		ProjectBudget b2 = mock(ProjectBudget.class);
		when(b2.getBudget()).thenReturn(69452f);
		this.test.setBudgets(Arrays.asList(b0, b1, b2));
		//
		float budget = this.test.getTotalLocalOrganizationBudget();
		assertEquals(70034.197f, budget);
	}

	@Test
	public void getEstimatedAnnualLocalOrganizationBudgetFor() {
		this.test.setStartDate(LocalDate.of(2023, 2, 1));
		this.test.setDuration(12);
		//
		ProjectBudget b0 = mock(ProjectBudget.class);
		when(b0.getBudget()).thenReturn(123.456f);
		ProjectBudget b1 = mock(ProjectBudget.class);
		when(b1.getBudget()).thenReturn(458.741f);
		ProjectBudget b2 = mock(ProjectBudget.class);
		when(b2.getBudget()).thenReturn(69452f);
		this.test.setBudgets(Arrays.asList(b0, b1, b2));
		// Total is 70034.197f
		//
		float budget = this.test.getEstimatedAnnualLocalOrganizationBudgetFor(2022);
		assertEquals(0f, budget);
		//
		budget = this.test.getEstimatedAnnualLocalOrganizationBudgetFor(2023);
		assertEquals(64198.0139f, budget);
		//
		budget = this.test.getEstimatedAnnualLocalOrganizationBudgetFor(2024);
		assertEquals(5836.183f, budget);
		//
		budget = this.test.getEstimatedAnnualLocalOrganizationBudgetFor(2025);
		assertEquals(0f, budget);
	}

	@Test
	public void getWebPageNaming() {
		assertSame(ProjectWebPageNaming.UNSPECIFIED, this.test.getWebPageNaming());
	}

	@Test
	public void setWebPageNaming_ProjectWebPageNaming_null() {
		this.test.setWebPageNaming(ProjectWebPageNaming.PROJECT_ID);
		this.test.setWebPageNaming((ProjectWebPageNaming) null);
		assertSame(ProjectWebPageNaming.UNSPECIFIED, this.test.getWebPageNaming());
	}

	@Test
	public void setWebPageNaming_ProjectWebPageNaming() {
		this.test.setWebPageNaming(ProjectWebPageNaming.ACRONYM);
		assertSame(ProjectWebPageNaming.ACRONYM, this.test.getWebPageNaming());
		this.test.setWebPageNaming(ProjectWebPageNaming.PROJECT_ID);
		assertSame(ProjectWebPageNaming.PROJECT_ID, this.test.getWebPageNaming());
		this.test.setWebPageNaming(ProjectWebPageNaming.UNSPECIFIED);
		assertSame(ProjectWebPageNaming.UNSPECIFIED, this.test.getWebPageNaming());
	}

	@Test
	public void setWebPageNaming_String_null() {
		this.test.setWebPageNaming(ProjectWebPageNaming.PROJECT_ID);
		this.test.setWebPageNaming((String) null);
		assertSame(ProjectWebPageNaming.UNSPECIFIED, this.test.getWebPageNaming());
	}

	@Test
	public void setWebPageNaming_String_empty() {
		this.test.setWebPageNaming(ProjectWebPageNaming.PROJECT_ID);
		this.test.setWebPageNaming("");
		assertSame(ProjectWebPageNaming.UNSPECIFIED, this.test.getWebPageNaming());
	}

	@Test
	public void setWebPageNaming_String_valid() {
		this.test.setWebPageNaming("project_id");
		assertSame(ProjectWebPageNaming.PROJECT_ID, this.test.getWebPageNaming());
		this.test.setWebPageNaming("acronym");
		assertSame(ProjectWebPageNaming.ACRONYM, this.test.getWebPageNaming());
		this.test.setWebPageNaming("unspecified");
		assertSame(ProjectWebPageNaming.UNSPECIFIED, this.test.getWebPageNaming());
	}

	@Test
	public void setWebPageNaming_String_invalid() {
		this.test.setWebPageNaming("xyz");
		assertSame(ProjectWebPageNaming.UNSPECIFIED, this.test.getWebPageNaming());
	}

	@Test
	public void getWebPageURI_unspecified() throws Exception {
		this.test.setWebPageNaming(ProjectWebPageNaming.UNSPECIFIED);
		assertNull(this.test.getWebPageURI());
	}

	@Test
	public void getWebPageURI_acronym_null() throws Exception {
		this.test.setWebPageNaming(ProjectWebPageNaming.ACRONYM);
		assertNull(this.test.getWebPageURI());
	}

	@Test
	public void getWebPageURI_acronym() throws Exception {
		this.test.setWebPageNaming(ProjectWebPageNaming.ACRONYM);
		this.test.setAcronym("xyz");
		assertEquals(new URI("/xyz"), this.test.getWebPageURI());
	}

	@Test
	public void getWebPageURI_id_zero() throws Exception {
		this.test.setWebPageNaming(ProjectWebPageNaming.PROJECT_ID);
		assertNull(this.test.getWebPageURI());
	}

	@Test
	public void getWebPageURI_id() throws Exception {
		this.test.setWebPageNaming(ProjectWebPageNaming.PROJECT_ID);
		this.test.setId(123456);
		assertEquals(new URI("/project-123456"), this.test.getWebPageURI());
	}

	@Test
	public void getScientificAxes() {
		assertTrue(this.test.getScientificAxes().isEmpty());
	}

	@Test
	public void setScientificAxes() {
		Set<Project> l0 = new HashSet<>();
		ScientificAxis a0 = mock(ScientificAxis.class);
		when(a0.getProjects()).thenReturn(l0);
		Set<Project> l1 = new HashSet<>();
		ScientificAxis a1 = mock(ScientificAxis.class);
		when(a1.getProjects()).thenReturn(l1);
		List<ScientificAxis> axes = Arrays.asList(a0, a1);

		this.test.setScientificAxes(axes);

		Set<ScientificAxis> actual = this.test.getScientificAxes();
		assertNotNull(actual);
		assertEquals(2, actual.size());
		assertTrue(actual.contains(a0));
		assertTrue(actual.contains(a1));

		assertFalse(l0.contains(this.test));
		assertFalse(l1.contains(this.test));
	}

	@Test
	public void isActive_noStart_noEnd() {
		assertTrue(this.test.isActive());
	}

	@Test
	public void isActive_noEnd() {
		LocalDate now = LocalDate.now();
		LocalDate past = now.minus(7, ChronoUnit.MONTHS);
		LocalDate future = now.plus(7, ChronoUnit.MONTHS);

		this.test.setStartDate(past);
		this.test.setDuration(12);
		assertTrue(this.test.isActive());
		//
		this.test.setStartDate(now);
		this.test.setDuration(12);
		assertTrue(this.test.isActive());
		//
		this.test.setStartDate(future);
		this.test.setDuration(12);
		assertFalse(this.test.isActive());
	}

	@Test
	public void isActive() {
		LocalDate now = LocalDate.now();
		LocalDate past = now.minus(7, ChronoUnit.MONTHS);
		LocalDate future = now.plus(7, ChronoUnit.MONTHS);

		this.test.setStartDate(past);
		this.test.setDuration(3);
		assertFalse(this.test.isActive());
		//
		this.test.setStartDate(past);
		this.test.setDuration(7);
		assertTrue(this.test.isActive());
		//
		this.test.setStartDate(past);
		this.test.setDuration(24);
		assertTrue(this.test.isActive());
		//
		this.test.setStartDate(now);
		this.test.setDuration(3);
		assertTrue(this.test.isActive());
		//
		this.test.setStartDate(future);
		this.test.setDuration(24);
		assertFalse(this.test.isActive());
	}

	@Test
	public void isActiveAt_LocalDate() {
		final LocalDate ld = LocalDate.of(2022, 5, 5);
		//
		this.test.setStartDate(LocalDate.of(2021, 5, 5));
		this.test.setDuration(1);
		assertFalse(this.test.isActiveAt(ld));
		//
		this.test.setStartDate(LocalDate.of(2021, 5, 5));
		this.test.setDuration(12);
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setStartDate(LocalDate.of(2021, 5, 5));
		this.test.setDuration(24);
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setStartDate(LocalDate.of(2022, 5, 5));
		this.test.setDuration(9);
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setStartDate(LocalDate.of(2022, 5, 5));
		this.test.setDuration(12);
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setStartDate(LocalDate.of(2023, 5, 5));
		this.test.setDuration(3);
		assertFalse(this.test.isActiveAt(ld));
	}

	@Test
	public void isActiveAt_int() {
		this.test.setStartDate(LocalDate.of(2021, 5, 5));
		this.test.setDuration(1);
		assertFalse(this.test.isActiveAt(2020));
		assertTrue(this.test.isActiveAt(2021));
		assertFalse(this.test.isActiveAt(2022));
		assertFalse(this.test.isActiveAt(2023));
		//
		this.test.setStartDate(LocalDate.of(2021, 5, 5));
		this.test.setDuration(12);
		assertFalse(this.test.isActiveAt(2020));
		assertTrue(this.test.isActiveAt(2021));
		assertTrue(this.test.isActiveAt(2022));
		assertFalse(this.test.isActiveAt(2023));
	}

	@Test
	public void isActiveIn() {
		final LocalDate s = LocalDate.of(2022, 1, 1);
		final LocalDate e = LocalDate.of(2022, 12, 31);
		//
		this.test.setStartDate(LocalDate.of(2021, 5, 5));
		this.test.setDuration(12);
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setStartDate(LocalDate.of(2022, 5, 5));
		this.test.setDuration(12);
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setStartDate(LocalDate.of(2023, 5, 5));
		this.test.setDuration(12);
		assertFalse(this.test.isActiveIn(s, e));
		//
		this.test.setStartDate(LocalDate.of(2022, 12, 31));
		this.test.setDuration(12);
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setStartDate(LocalDate.of(2023, 01, 01));
		this.test.setDuration(12);
		assertFalse(this.test.isActiveIn(s, e));
	}

}
