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

package fr.utbm.ciad.labmanager.tests.data.supervision;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import fr.utbm.ciad.labmanager.configuration.messages.BaseMessageSource;
import fr.utbm.ciad.labmanager.data.member.Gender;
import fr.utbm.ciad.labmanager.data.supervision.SupervisorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link SupervisorType}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.1
 */@SuppressWarnings("all")
 public class SupervisorTypeTest {

	 private List<SupervisorType> items;

	 private MessageSourceAccessor messages;

	 @BeforeEach
	 public void setUp() {
		 this.messages = BaseMessageSource.getGlobalMessageAccessor();
		 this.items = new ArrayList<>();
		 this.items.addAll(Arrays.asList(SupervisorType.values()));
	 }

	 private SupervisorType cons(SupervisorType status) {
		 assertTrue(this.items.remove(status), "Expecting enumeration item: " + status.toString());
		 return status;
	 }

	 private void assertAllTreated() {
		 assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	 }

	 @Test
	 public void getLabel_Locale_FEMALE_US() throws Exception {
		 assertEquals("Director", cons(SupervisorType.DIRECTOR).getLabel(this.messages, Gender.FEMALE, Locale.US));
		 assertEquals("Supervisor", cons(SupervisorType.SUPERVISOR).getLabel(this.messages, Gender.FEMALE, Locale.US));
		 assertEquals("Monitoring Committee Member", cons(SupervisorType.COMMITTEE_MEMBER).getLabel(this.messages, Gender.FEMALE, Locale.US));
		 assertAllTreated();
	 }

	 @Test
	 public void getLabel_Locale_MALE_US() throws Exception {
		 assertEquals("Director", cons(SupervisorType.DIRECTOR).getLabel(this.messages, Gender.MALE, Locale.US));
		 assertEquals("Supervisor", cons(SupervisorType.SUPERVISOR).getLabel(this.messages, Gender.MALE, Locale.US));
		 assertEquals("Monitoring Committee Member", cons(SupervisorType.COMMITTEE_MEMBER).getLabel(this.messages, Gender.MALE, Locale.US));
		 assertAllTreated();
	 }

	 @Test
	 public void getLabel_Locale_OTHER_US() throws Exception {
		 assertEquals("Director", cons(SupervisorType.DIRECTOR).getLabel(this.messages, Gender.OTHER, Locale.US));
		 assertEquals("Supervisor", cons(SupervisorType.SUPERVISOR).getLabel(this.messages, Gender.OTHER, Locale.US));
		 assertEquals("Monitoring Committee Member", cons(SupervisorType.COMMITTEE_MEMBER).getLabel(this.messages, Gender.OTHER, Locale.US));
		 assertAllTreated();
	 }

	 @Test
	 public void getLabel_Locale_NOTSPECIFIED_US() throws Exception {
		 assertEquals("Director", cons(SupervisorType.DIRECTOR).getLabel(this.messages, Gender.NOT_SPECIFIED, Locale.US));
		 assertEquals("Supervisor", cons(SupervisorType.SUPERVISOR).getLabel(this.messages, Gender.NOT_SPECIFIED, Locale.US));
		 assertEquals("Monitoring Committee Member", cons(SupervisorType.COMMITTEE_MEMBER).getLabel(this.messages, Gender.NOT_SPECIFIED, Locale.US));
		 assertAllTreated();
	 }

 }