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
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link ProjectContractType}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ProjectContractTypeTest {

	private List<ProjectContractType> items;

	@BeforeEach
	public void setUp() {
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(ProjectContractType.values()));
	}

	private ProjectContractType cons(ProjectContractType status) {
		assertTrue(this.items.remove(status), "Expecting enumeration item: " + status.toString());
		return status;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void valueOfCaseInsensitive_String_special() {
		assertThrows(IllegalArgumentException.class, () -> ProjectContractType.valueOfCaseInsensitive(null));
		assertThrows(IllegalArgumentException.class, () -> ProjectContractType.valueOfCaseInsensitive(""));
	}

	@Test
	public void valueOfCaseInsensitive_String_upperCase() {
		assertEquals(cons(ProjectContractType.NOT_SPECIFIED), ProjectContractType.valueOfCaseInsensitive("NOT_SPECIFIED"));
		assertEquals(cons(ProjectContractType.RCO), ProjectContractType.valueOfCaseInsensitive("RCO"));
		assertEquals(cons(ProjectContractType.RCD), ProjectContractType.valueOfCaseInsensitive("RCD"));
		assertEquals(cons(ProjectContractType.PR), ProjectContractType.valueOfCaseInsensitive("PR"));
		assertEquals(cons(ProjectContractType.PI), ProjectContractType.valueOfCaseInsensitive("PI"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> ProjectContractType.valueOfCaseInsensitive("XYZ"));
	}

	@Test
	public void valueOfCaseInsensitive_String_lowerCase() {
		assertEquals(cons(ProjectContractType.NOT_SPECIFIED), ProjectContractType.valueOfCaseInsensitive("not_specified"));
		assertEquals(cons(ProjectContractType.RCO), ProjectContractType.valueOfCaseInsensitive("rco"));
		assertEquals(cons(ProjectContractType.RCD), ProjectContractType.valueOfCaseInsensitive("rcd"));
		assertEquals(cons(ProjectContractType.PR), ProjectContractType.valueOfCaseInsensitive("pr"));
		assertEquals(cons(ProjectContractType.PI), ProjectContractType.valueOfCaseInsensitive("pi"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> ProjectContractType.valueOfCaseInsensitive("xyz"));
	}

	@Test
	public void getLabel_FR() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Unspecified type", cons(ProjectContractType.NOT_SPECIFIED).getLabel());
		assertEquals("Contract for collaborative contractual research, funded by public bodies", cons(ProjectContractType.RCO).getLabel());
		assertEquals("Contract for direct contractual research, with shared IP or CIFRE", cons(ProjectContractType.RCD).getLabel());
		assertEquals("Contract for contractual services, with transfer of IP", cons(ProjectContractType.PR).getLabel());
		assertEquals("Contract dedicated to IP transfer", cons(ProjectContractType.PI).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_US() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Unspecified type", cons(ProjectContractType.NOT_SPECIFIED).getLabel());
		assertEquals("Contract for collaborative contractual research, funded by public bodies", cons(ProjectContractType.RCO).getLabel());
		assertEquals("Contract for direct contractual research, with shared IP or CIFRE", cons(ProjectContractType.RCD).getLabel());
		assertEquals("Contract for contractual services, with transfer of IP", cons(ProjectContractType.PR).getLabel());
		assertEquals("Contract dedicated to IP transfer", cons(ProjectContractType.PI).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR() throws Exception {
		assertEquals("Type non spécifié", cons(ProjectContractType.NOT_SPECIFIED).getLabel(Locale.FRANCE));
		assertEquals("Contrat de recherche contractuelle collaborative, subventionné par des institutions publiques", cons(ProjectContractType.RCO).getLabel(Locale.FRANCE));
		assertEquals("Contrat de recherche contractuelle directe, avec partage de PI ou CIFRE", cons(ProjectContractType.RCD).getLabel(Locale.FRANCE));
		assertEquals("Contrat de prestation, avec transfert de PI", cons(ProjectContractType.PR).getLabel(Locale.FRANCE));
		assertEquals("Contrat de transfert de PI uniquement", cons(ProjectContractType.PI).getLabel(Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US() throws Exception {
		assertEquals("Unspecified type", cons(ProjectContractType.NOT_SPECIFIED).getLabel(Locale.US));
		assertEquals("Contract for collaborative contractual research, funded by public bodies", cons(ProjectContractType.RCO).getLabel(Locale.US));
		assertEquals("Contract for direct contractual research, with shared IP or CIFRE", cons(ProjectContractType.RCD).getLabel(Locale.US));
		assertEquals("Contract for contractual services, with transfer of IP", cons(ProjectContractType.PR).getLabel(Locale.US));
		assertEquals("Contract dedicated to IP transfer", cons(ProjectContractType.PI).getLabel(Locale.US));
		assertAllTreated();
	}

}
