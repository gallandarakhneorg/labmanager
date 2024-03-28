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

package fr.utbm.ciad.labmanager.tests.data.jury;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import fr.utbm.ciad.labmanager.configuration.messages.BaseMessageSource;
import fr.utbm.ciad.labmanager.data.jury.JuryMembershipType;
import fr.utbm.ciad.labmanager.data.member.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link JuryMembershipType}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@SuppressWarnings("all")
public class JuryMembershipTypeTest {

	private List<JuryMembershipType> items;
	
	private MessageSourceAccessor messages;

	@BeforeEach
	public void setUp() {
		this.messages = BaseMessageSource.getGlobalMessageAccessor();
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(JuryMembershipType.values()));
	}

	private JuryMembershipType cons(JuryMembershipType type) {
		assertTrue(this.items.remove(type), "Expecting enumeration item: " + type.toString());
		return type;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void getLabel_Locale_US_MALE() {
		assertEquals("Examiner", cons(JuryMembershipType.EXAMINER).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Invited member", cons(JuryMembershipType.INVITED_PERSON).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("President", cons(JuryMembershipType.PRESIDENT).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Reviewer", cons(JuryMembershipType.REVIEWER).getLabel(this.messages, Gender.MALE, Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US_FEMALE() {
		assertEquals("Examiner", cons(JuryMembershipType.EXAMINER).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Invited member", cons(JuryMembershipType.INVITED_PERSON).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("President", cons(JuryMembershipType.PRESIDENT).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Reviewer", cons(JuryMembershipType.REVIEWER).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US_OTHER() {
		assertEquals("Examiner", cons(JuryMembershipType.EXAMINER).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Invited member", cons(JuryMembershipType.INVITED_PERSON).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("President", cons(JuryMembershipType.PRESIDENT).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Reviewer", cons(JuryMembershipType.REVIEWER).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertAllTreated();
	}

}
