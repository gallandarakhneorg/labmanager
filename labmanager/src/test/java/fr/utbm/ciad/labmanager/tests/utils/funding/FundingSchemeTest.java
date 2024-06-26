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

package fr.utbm.ciad.labmanager.tests.utils.funding;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import fr.utbm.ciad.labmanager.configuration.messages.BaseMessageSource;
import fr.utbm.ciad.labmanager.utils.funding.FundingScheme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link FundingScheme}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.1
 */@SuppressWarnings("all")
public class FundingSchemeTest {

	private List<FundingScheme> items;

	private MessageSourceAccessor messages;

	@BeforeEach
	public void setUp() {
		this.messages = BaseMessageSource.getGlobalMessageAccessor();
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(FundingScheme.values()));
	}

	private FundingScheme cons(FundingScheme status) {
		assertTrue(this.items.remove(status), "Expecting enumeration item: " + status.toString());
		return status;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void isRegional() throws Exception {
		assertFalse(cons(FundingScheme.ADEME).isRegional());
		assertFalse(cons(FundingScheme.ANR).isRegional());
		assertFalse(cons(FundingScheme.CAMPUS_FRANCE).isRegional());
		assertFalse(cons(FundingScheme.CARNOT).isRegional());
		assertFalse(cons(FundingScheme.CIFRE).isRegional());
		assertFalse(cons(FundingScheme.CONACYT).isRegional());
		assertFalse(cons(FundingScheme.COST_ACTION).isRegional());
		assertTrue(cons(FundingScheme.CPER).isRegional());
		assertFalse(cons(FundingScheme.CSC).isRegional());
		assertFalse(cons(FundingScheme.EDIH).isRegional());
		assertFalse(cons(FundingScheme.EU_COMPANY).isRegional());
		assertFalse(cons(FundingScheme.EU_OTHER).isRegional());
		assertFalse(cons(FundingScheme.EU_UNIVERSITY).isRegional());
		assertFalse(cons(FundingScheme.EUREKA).isRegional());
		assertFalse(cons(FundingScheme.EUROSTAR).isRegional());
		assertTrue(cons(FundingScheme.FEDER).isRegional());
		assertFalse(cons(FundingScheme.FITEC).isRegional());
		assertFalse(cons(FundingScheme.FRENCH_COMPANY).isRegional());
		assertFalse(cons(FundingScheme.FRENCH_OTHER).isRegional());
		assertFalse(cons(FundingScheme.FRENCH_UNIVERSITY).isRegional());
		assertFalse(cons(FundingScheme.FUI).isRegional());
		assertFalse(cons(FundingScheme.H2020).isRegional());
		assertFalse(cons(FundingScheme.HORIZON_EUROPE).isRegional());
		assertTrue(cons(FundingScheme.HOSTING_ORGANIZATION).isRegional());
		assertFalse(cons(FundingScheme.IDEX).isRegional());
		assertFalse(cons(FundingScheme.INTERNATIONAL_COMPANY).isRegional());
		assertFalse(cons(FundingScheme.INTERNATIONAL_UNIVERSITY).isRegional());
		assertFalse(cons(FundingScheme.INTERNTATIONAL_OTHER).isRegional());
		assertFalse(cons(FundingScheme.INTERREG).isRegional());
		assertFalse(cons(FundingScheme.ISITE).isRegional());
		assertFalse(cons(FundingScheme.JPIEU).isRegional());
		assertFalse(cons(FundingScheme.LIFE).isRegional());
		assertTrue(cons(FundingScheme.LOCAL_INSTITUTION).isRegional());
		assertFalse(cons(FundingScheme.NICOLAS_BAUDIN).isRegional());
		assertFalse(cons(FundingScheme.NOT_FUNDED).isRegional());
		assertFalse(cons(FundingScheme.PHC).isRegional());
		assertFalse(cons(FundingScheme.PIA).isRegional());
		assertFalse(cons(FundingScheme.FRANCE_2030).isRegional());
		assertTrue(cons(FundingScheme.REGION_BFC).isRegional());
		assertFalse(cons(FundingScheme.SELF_FUNDING).isRegional());
		assertAllTreated();
	}

	@Test
	public void isNational() throws Exception {
		assertTrue(cons(FundingScheme.ADEME).isNational());
		assertTrue(cons(FundingScheme.ANR).isNational());
		assertFalse(cons(FundingScheme.CAMPUS_FRANCE).isNational());
		assertTrue(cons(FundingScheme.CARNOT).isNational());
		assertTrue(cons(FundingScheme.CIFRE).isNational());
		assertFalse(cons(FundingScheme.CONACYT).isNational());
		assertFalse(cons(FundingScheme.COST_ACTION).isNational());
		assertFalse(cons(FundingScheme.CPER).isNational());
		assertFalse(cons(FundingScheme.CSC).isNational());
		assertFalse(cons(FundingScheme.EDIH).isNational());
		assertFalse(cons(FundingScheme.EU_COMPANY).isNational());
		assertFalse(cons(FundingScheme.EU_OTHER).isNational());
		assertFalse(cons(FundingScheme.EU_UNIVERSITY).isNational());
		assertFalse(cons(FundingScheme.EUREKA).isNational());
		assertFalse(cons(FundingScheme.EUROSTAR).isNational());
		assertFalse(cons(FundingScheme.FEDER).isNational());
		assertFalse(cons(FundingScheme.FITEC).isNational());
		assertTrue(cons(FundingScheme.FRENCH_COMPANY).isNational());
		assertTrue(cons(FundingScheme.FRENCH_OTHER).isNational());
		assertTrue(cons(FundingScheme.FRENCH_UNIVERSITY).isNational());
		assertTrue(cons(FundingScheme.FUI).isNational());
		assertFalse(cons(FundingScheme.H2020).isNational());
		assertFalse(cons(FundingScheme.HORIZON_EUROPE).isNational());
		assertFalse(cons(FundingScheme.HOSTING_ORGANIZATION).isNational());
		assertTrue(cons(FundingScheme.IDEX).isNational());
		assertFalse(cons(FundingScheme.INTERNATIONAL_COMPANY).isNational());
		assertFalse(cons(FundingScheme.INTERNATIONAL_UNIVERSITY).isNational());
		assertFalse(cons(FundingScheme.INTERNTATIONAL_OTHER).isNational());
		assertFalse(cons(FundingScheme.INTERREG).isNational());
		assertTrue(cons(FundingScheme.ISITE).isNational());
		assertFalse(cons(FundingScheme.JPIEU).isNational());
		assertFalse(cons(FundingScheme.LIFE).isNational());
		assertFalse(cons(FundingScheme.LOCAL_INSTITUTION).isNational());
		assertFalse(cons(FundingScheme.NICOLAS_BAUDIN).isNational());
		assertFalse(cons(FundingScheme.NOT_FUNDED).isNational());
		assertFalse(cons(FundingScheme.PHC).isNational());
		assertTrue(cons(FundingScheme.PIA).isNational());
		assertTrue(cons(FundingScheme.FRANCE_2030).isNational());
		assertFalse(cons(FundingScheme.REGION_BFC).isNational());
		assertFalse(cons(FundingScheme.SELF_FUNDING).isNational());
		assertAllTreated();
	}

	@Test
	public void isEuropean() throws Exception {
		assertFalse(cons(FundingScheme.ADEME).isEuropean());
		assertFalse(cons(FundingScheme.ANR).isEuropean());
		assertFalse(cons(FundingScheme.CAMPUS_FRANCE).isEuropean());
		assertFalse(cons(FundingScheme.CARNOT).isEuropean());
		assertFalse(cons(FundingScheme.CIFRE).isEuropean());
		assertFalse(cons(FundingScheme.CONACYT).isEuropean());
		assertTrue(cons(FundingScheme.COST_ACTION).isEuropean());
		assertFalse(cons(FundingScheme.CPER).isEuropean());
		assertFalse(cons(FundingScheme.CSC).isEuropean());
		assertTrue(cons(FundingScheme.EDIH).isEuropean());
		assertTrue(cons(FundingScheme.EU_COMPANY).isEuropean());
		assertTrue(cons(FundingScheme.EU_OTHER).isEuropean());
		assertTrue(cons(FundingScheme.EU_UNIVERSITY).isEuropean());
		assertTrue(cons(FundingScheme.EUREKA).isEuropean());
		assertTrue(cons(FundingScheme.EUROSTAR).isEuropean());
		assertFalse(cons(FundingScheme.FEDER).isEuropean());
		assertFalse(cons(FundingScheme.FITEC).isEuropean());
		assertFalse(cons(FundingScheme.FRENCH_COMPANY).isEuropean());
		assertFalse(cons(FundingScheme.FRENCH_OTHER).isEuropean());
		assertFalse(cons(FundingScheme.FRENCH_UNIVERSITY).isEuropean());
		assertFalse(cons(FundingScheme.FUI).isEuropean());
		assertTrue(cons(FundingScheme.H2020).isEuropean());
		assertTrue(cons(FundingScheme.HORIZON_EUROPE).isEuropean());
		assertFalse(cons(FundingScheme.HOSTING_ORGANIZATION).isEuropean());
		assertFalse(cons(FundingScheme.IDEX).isEuropean());
		assertFalse(cons(FundingScheme.INTERNATIONAL_COMPANY).isEuropean());
		assertFalse(cons(FundingScheme.INTERNATIONAL_UNIVERSITY).isEuropean());
		assertFalse(cons(FundingScheme.INTERNTATIONAL_OTHER).isEuropean());
		assertTrue(cons(FundingScheme.INTERREG).isEuropean());
		assertFalse(cons(FundingScheme.ISITE).isEuropean());
		assertTrue(cons(FundingScheme.JPIEU).isEuropean());
		assertTrue(cons(FundingScheme.LIFE).isEuropean());
		assertFalse(cons(FundingScheme.LOCAL_INSTITUTION).isEuropean());
		assertFalse(cons(FundingScheme.NICOLAS_BAUDIN).isEuropean());
		assertFalse(cons(FundingScheme.NOT_FUNDED).isEuropean());
		assertFalse(cons(FundingScheme.PHC).isEuropean());
		assertFalse(cons(FundingScheme.PIA).isEuropean());
		assertFalse(cons(FundingScheme.FRANCE_2030).isEuropean());
		assertFalse(cons(FundingScheme.REGION_BFC).isEuropean());
		assertFalse(cons(FundingScheme.SELF_FUNDING).isEuropean());
		assertAllTreated();
	}

	@Test
	public void isInternational() throws Exception {
		assertFalse(cons(FundingScheme.ADEME).isInternational());
		assertFalse(cons(FundingScheme.ANR).isInternational());
		assertTrue(cons(FundingScheme.CAMPUS_FRANCE).isInternational());
		assertFalse(cons(FundingScheme.CARNOT).isInternational());
		assertFalse(cons(FundingScheme.CIFRE).isInternational());
		assertTrue(cons(FundingScheme.CONACYT).isInternational());
		assertFalse(cons(FundingScheme.COST_ACTION).isInternational());
		assertFalse(cons(FundingScheme.CPER).isInternational());
		assertTrue(cons(FundingScheme.CSC).isInternational());
		assertFalse(cons(FundingScheme.EDIH).isInternational());
		assertFalse(cons(FundingScheme.EU_COMPANY).isInternational());
		assertFalse(cons(FundingScheme.EU_OTHER).isInternational());
		assertFalse(cons(FundingScheme.EU_UNIVERSITY).isInternational());
		assertFalse(cons(FundingScheme.EUREKA).isInternational());
		assertFalse(cons(FundingScheme.EUROSTAR).isInternational());
		assertFalse(cons(FundingScheme.FEDER).isInternational());
		assertTrue(cons(FundingScheme.FITEC).isInternational());
		assertFalse(cons(FundingScheme.FRENCH_COMPANY).isInternational());
		assertFalse(cons(FundingScheme.FRENCH_OTHER).isInternational());
		assertFalse(cons(FundingScheme.FRENCH_UNIVERSITY).isInternational());
		assertFalse(cons(FundingScheme.FUI).isInternational());
		assertFalse(cons(FundingScheme.H2020).isInternational());
		assertFalse(cons(FundingScheme.HORIZON_EUROPE).isInternational());
		assertFalse(cons(FundingScheme.HOSTING_ORGANIZATION).isInternational());
		assertFalse(cons(FundingScheme.IDEX).isInternational());
		assertTrue(cons(FundingScheme.INTERNATIONAL_COMPANY).isInternational());
		assertTrue(cons(FundingScheme.INTERNATIONAL_UNIVERSITY).isInternational());
		assertTrue(cons(FundingScheme.INTERNTATIONAL_OTHER).isInternational());
		assertFalse(cons(FundingScheme.INTERREG).isInternational());
		assertFalse(cons(FundingScheme.ISITE).isInternational());
		assertFalse(cons(FundingScheme.JPIEU).isInternational());
		assertFalse(cons(FundingScheme.LIFE).isInternational());
		assertFalse(cons(FundingScheme.LOCAL_INSTITUTION).isInternational());
		assertTrue(cons(FundingScheme.NICOLAS_BAUDIN).isInternational());
		assertFalse(cons(FundingScheme.NOT_FUNDED).isInternational());
		assertTrue(cons(FundingScheme.PHC).isInternational());
		assertFalse(cons(FundingScheme.PIA).isInternational());
		assertFalse(cons(FundingScheme.FRANCE_2030).isInternational());
		assertFalse(cons(FundingScheme.REGION_BFC).isInternational());
		assertFalse(cons(FundingScheme.SELF_FUNDING).isInternational());
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US() throws Exception {
		assertEquals("ADEME - Agence de la transition écologique", cons(FundingScheme.ADEME).getLabel(this.messages, Locale.US));
		assertEquals("ANR - Agence Nationale pour le Recherche", cons(FundingScheme.ANR).getLabel(this.messages, Locale.US));
		assertEquals("Campus France", cons(FundingScheme.CAMPUS_FRANCE).getLabel(this.messages, Locale.US));
		assertEquals("Institut Carnot", cons(FundingScheme.CARNOT).getLabel(this.messages, Locale.US));
		assertEquals("CIFRE - Conventions industrielles de formation par la recherche", cons(FundingScheme.CIFRE).getLabel(this.messages, Locale.US));
		assertEquals("CONACYT Mexican Funding", cons(FundingScheme.CONACYT).getLabel(this.messages, Locale.US));
		assertEquals("COST Action", cons(FundingScheme.COST_ACTION).getLabel(this.messages, Locale.US));
		assertEquals("CPER - Contrat Plan \u00C9tat Région", cons(FundingScheme.CPER).getLabel(this.messages, Locale.US));
		assertEquals("CSC - Chinease Scholarship Council", cons(FundingScheme.CSC).getLabel(this.messages, Locale.US));
		assertEquals("EDIH - European Digital Innovation Hubs", cons(FundingScheme.EDIH).getLabel(this.messages, Locale.US));
		assertEquals("European Company", cons(FundingScheme.EU_COMPANY).getLabel(this.messages, Locale.US));
		assertEquals("Other european funds", cons(FundingScheme.EU_OTHER).getLabel(this.messages, Locale.US));
		assertEquals("European University", cons(FundingScheme.EU_UNIVERSITY).getLabel(this.messages, Locale.US));
		assertEquals("Eureka Clusters Programme", cons(FundingScheme.EUREKA).getLabel(this.messages, Locale.US));
		assertEquals("Eurostar - European Partnership on Innovative SMEs", cons(FundingScheme.EUROSTAR).getLabel(this.messages, Locale.US));
		assertEquals("FEDER - Fonds européen de développement régional", cons(FundingScheme.FEDER).getLabel(this.messages, Locale.US));
		assertEquals("FITEC - France Ingénieurs TEChnologie", cons(FundingScheme.FITEC).getLabel(this.messages, Locale.US));
		assertEquals("French Company", cons(FundingScheme.FRENCH_COMPANY).getLabel(this.messages, Locale.US));
		assertEquals("Other French funds", cons(FundingScheme.FRENCH_OTHER).getLabel(this.messages, Locale.US));
		assertEquals("French University", cons(FundingScheme.FRENCH_UNIVERSITY).getLabel(this.messages, Locale.US));
		assertEquals("FUI - Fonds unique interministériel", cons(FundingScheme.FUI).getLabel(this.messages, Locale.US));
		assertEquals("H2020 - Horizon 2020", cons(FundingScheme.H2020).getLabel(this.messages, Locale.US));
		assertEquals("Horizon Europe", cons(FundingScheme.HORIZON_EUROPE).getLabel(this.messages, Locale.US));
		assertEquals("Hosting organization", cons(FundingScheme.HOSTING_ORGANIZATION).getLabel(this.messages, Locale.US));
		assertEquals("IDEX - Initiatives d'excellence / Plan d'Investissement d'Avenir", cons(FundingScheme.IDEX).getLabel(this.messages, Locale.US));
		assertEquals("International Company", cons(FundingScheme.INTERNATIONAL_COMPANY).getLabel(this.messages, Locale.US));
		assertEquals("International University", cons(FundingScheme.INTERNATIONAL_UNIVERSITY).getLabel(this.messages, Locale.US));
		assertEquals("Other international funds", cons(FundingScheme.INTERNTATIONAL_OTHER).getLabel(this.messages, Locale.US));
		assertEquals("Interreg Europe", cons(FundingScheme.INTERREG).getLabel(this.messages, Locale.US));
		assertEquals("ISITE - Initiatives Science-Innovation-Territoire-\u00C9conomie / Plan d'Investissement d'Avenir", cons(FundingScheme.ISITE).getLabel(this.messages, Locale.US));
		assertEquals("JPI Urban Europe", cons(FundingScheme.JPIEU).getLabel(this.messages, Locale.US));
		assertEquals("LIFE - Programme for the Environment and Climate Action", cons(FundingScheme.LIFE).getLabel(this.messages, Locale.US));
		assertEquals("Local institution", cons(FundingScheme.LOCAL_INSTITUTION).getLabel(this.messages, Locale.US));
		assertEquals("Nicolas Baudin Programme", cons(FundingScheme.NICOLAS_BAUDIN).getLabel(this.messages, Locale.US));
		assertEquals("Not funded", cons(FundingScheme.NOT_FUNDED).getLabel(this.messages, Locale.US));
		assertEquals("PHC - Partenariats Hubert Curien", cons(FundingScheme.PHC).getLabel(this.messages, Locale.US));
		assertEquals("PIA - Plan d'Investissement d'Avenir", cons(FundingScheme.PIA).getLabel(this.messages, Locale.US));
		assertEquals("France 2030 - Plan d'Investissement d'Avenir France 2030", cons(FundingScheme.FRANCE_2030).getLabel(this.messages, Locale.US));
		assertEquals("Bourgogne-Franche-Comté Province", cons(FundingScheme.REGION_BFC).getLabel(this.messages, Locale.US));
		assertEquals("Self funded", cons(FundingScheme.SELF_FUNDING).getLabel(this.messages, Locale.US));
		assertAllTreated();
	}

	@Test
	public void isCompetitive() throws Exception {
		assertTrue(cons(FundingScheme.ADEME).isCompetitive());
		assertTrue(cons(FundingScheme.ANR).isCompetitive());
		assertTrue(cons(FundingScheme.CAMPUS_FRANCE).isCompetitive());
		assertTrue(cons(FundingScheme.CARNOT).isCompetitive());
		assertFalse(cons(FundingScheme.CIFRE).isCompetitive());
		assertTrue(cons(FundingScheme.CONACYT).isCompetitive());
		assertTrue(cons(FundingScheme.COST_ACTION).isCompetitive());
		assertTrue(cons(FundingScheme.CPER).isCompetitive());
		assertTrue(cons(FundingScheme.CSC).isCompetitive());
		assertTrue(cons(FundingScheme.EDIH).isCompetitive());
		assertFalse(cons(FundingScheme.EU_COMPANY).isCompetitive());
		assertFalse(cons(FundingScheme.EU_OTHER).isCompetitive());
		assertFalse(cons(FundingScheme.EU_UNIVERSITY).isCompetitive());
		assertTrue(cons(FundingScheme.EUREKA).isCompetitive());
		assertTrue(cons(FundingScheme.EUROSTAR).isCompetitive());
		assertTrue(cons(FundingScheme.FEDER).isCompetitive());
		assertTrue(cons(FundingScheme.FITEC).isCompetitive());
		assertFalse(cons(FundingScheme.FRENCH_COMPANY).isCompetitive());
		assertFalse(cons(FundingScheme.FRENCH_OTHER).isCompetitive());
		assertFalse(cons(FundingScheme.FRENCH_UNIVERSITY).isCompetitive());
		assertTrue(cons(FundingScheme.FUI).isCompetitive());
		assertTrue(cons(FundingScheme.H2020).isCompetitive());
		assertTrue(cons(FundingScheme.HORIZON_EUROPE).isCompetitive());
		assertFalse(cons(FundingScheme.HOSTING_ORGANIZATION).isCompetitive());
		assertTrue(cons(FundingScheme.IDEX).isCompetitive());
		assertFalse(cons(FundingScheme.INTERNATIONAL_COMPANY).isCompetitive());
		assertFalse(cons(FundingScheme.INTERNATIONAL_UNIVERSITY).isCompetitive());
		assertFalse(cons(FundingScheme.INTERNTATIONAL_OTHER).isCompetitive());
		assertTrue(cons(FundingScheme.INTERREG).isCompetitive());
		assertTrue(cons(FundingScheme.ISITE).isCompetitive());
		assertTrue(cons(FundingScheme.JPIEU).isCompetitive());
		assertTrue(cons(FundingScheme.LIFE).isCompetitive());
		assertFalse(cons(FundingScheme.LOCAL_INSTITUTION).isCompetitive());
		assertTrue(cons(FundingScheme.NICOLAS_BAUDIN).isCompetitive());
		assertFalse(cons(FundingScheme.NOT_FUNDED).isCompetitive());
		assertTrue(cons(FundingScheme.PHC).isCompetitive());
		assertTrue(cons(FundingScheme.PIA).isCompetitive());
		assertTrue(cons(FundingScheme.FRANCE_2030).isCompetitive());
		assertTrue(cons(FundingScheme.REGION_BFC).isCompetitive());
		assertFalse(cons(FundingScheme.SELF_FUNDING).isCompetitive());
		assertAllTreated();
	}

	@Test
	public void isNotAcademic() throws Exception {
		assertFalse(cons(FundingScheme.ADEME).isNotAcademic());
		assertFalse(cons(FundingScheme.ANR).isNotAcademic());
		assertFalse(cons(FundingScheme.CAMPUS_FRANCE).isNotAcademic());
		assertFalse(cons(FundingScheme.CARNOT).isNotAcademic());
		assertFalse(cons(FundingScheme.CIFRE).isNotAcademic());
		assertFalse(cons(FundingScheme.CONACYT).isNotAcademic());
		assertFalse(cons(FundingScheme.COST_ACTION).isNotAcademic());
		assertFalse(cons(FundingScheme.CPER).isNotAcademic());
		assertFalse(cons(FundingScheme.CSC).isNotAcademic());
		assertFalse(cons(FundingScheme.EDIH).isNotAcademic());
		assertTrue(cons(FundingScheme.EU_COMPANY).isNotAcademic());
		assertTrue(cons(FundingScheme.EU_OTHER).isNotAcademic());
		assertFalse(cons(FundingScheme.EU_UNIVERSITY).isNotAcademic());
		assertFalse(cons(FundingScheme.EUREKA).isNotAcademic());
		assertFalse(cons(FundingScheme.EUROSTAR).isNotAcademic());
		assertFalse(cons(FundingScheme.FEDER).isNotAcademic());
		assertFalse(cons(FundingScheme.FITEC).isNotAcademic());
		assertTrue(cons(FundingScheme.FRENCH_COMPANY).isNotAcademic());
		assertTrue(cons(FundingScheme.FRENCH_OTHER).isNotAcademic());
		assertFalse(cons(FundingScheme.FRENCH_UNIVERSITY).isNotAcademic());
		assertFalse(cons(FundingScheme.FUI).isNotAcademic());
		assertFalse(cons(FundingScheme.H2020).isNotAcademic());
		assertFalse(cons(FundingScheme.HORIZON_EUROPE).isNotAcademic());
		assertFalse(cons(FundingScheme.HOSTING_ORGANIZATION).isNotAcademic());
		assertFalse(cons(FundingScheme.IDEX).isNotAcademic());
		assertTrue(cons(FundingScheme.INTERNATIONAL_COMPANY).isNotAcademic());
		assertFalse(cons(FundingScheme.INTERNATIONAL_UNIVERSITY).isNotAcademic());
		assertTrue(cons(FundingScheme.INTERNTATIONAL_OTHER).isNotAcademic());
		assertFalse(cons(FundingScheme.INTERREG).isNotAcademic());
		assertFalse(cons(FundingScheme.ISITE).isNotAcademic());
		assertFalse(cons(FundingScheme.JPIEU).isNotAcademic());
		assertFalse(cons(FundingScheme.LIFE).isNotAcademic());
		assertFalse(cons(FundingScheme.LOCAL_INSTITUTION).isNotAcademic());
		assertFalse(cons(FundingScheme.NICOLAS_BAUDIN).isNotAcademic());
		assertFalse(cons(FundingScheme.NOT_FUNDED).isNotAcademic());
		assertFalse(cons(FundingScheme.PHC).isNotAcademic());
		assertFalse(cons(FundingScheme.PIA).isNotAcademic());
		assertFalse(cons(FundingScheme.FRANCE_2030).isNotAcademic());
		assertFalse(cons(FundingScheme.REGION_BFC).isNotAcademic());
		assertFalse(cons(FundingScheme.SELF_FUNDING).isNotAcademic());
		assertAllTreated();
	}

	@Test
	public void isAcademicButContractual() throws Exception {
		assertFalse(cons(FundingScheme.ADEME).isAcademicButContractual());
		assertFalse(cons(FundingScheme.ANR).isAcademicButContractual());
		assertFalse(cons(FundingScheme.CAMPUS_FRANCE).isAcademicButContractual());
		assertFalse(cons(FundingScheme.CARNOT).isAcademicButContractual());
		assertFalse(cons(FundingScheme.CIFRE).isAcademicButContractual());
		assertFalse(cons(FundingScheme.CONACYT).isAcademicButContractual());
		assertFalse(cons(FundingScheme.COST_ACTION).isAcademicButContractual());
		assertFalse(cons(FundingScheme.CPER).isAcademicButContractual());
		assertFalse(cons(FundingScheme.CSC).isAcademicButContractual());
		assertFalse(cons(FundingScheme.EDIH).isAcademicButContractual());
		assertFalse(cons(FundingScheme.EU_COMPANY).isAcademicButContractual());
		assertFalse(cons(FundingScheme.EU_OTHER).isAcademicButContractual());
		assertTrue(cons(FundingScheme.EU_UNIVERSITY).isAcademicButContractual());
		assertFalse(cons(FundingScheme.EUREKA).isAcademicButContractual());
		assertFalse(cons(FundingScheme.EUROSTAR).isAcademicButContractual());
		assertFalse(cons(FundingScheme.FEDER).isAcademicButContractual());
		assertFalse(cons(FundingScheme.FITEC).isAcademicButContractual());
		assertFalse(cons(FundingScheme.FRENCH_COMPANY).isAcademicButContractual());
		assertFalse(cons(FundingScheme.FRENCH_OTHER).isAcademicButContractual());
		assertTrue(cons(FundingScheme.FRENCH_UNIVERSITY).isAcademicButContractual());
		assertFalse(cons(FundingScheme.FUI).isAcademicButContractual());
		assertFalse(cons(FundingScheme.H2020).isAcademicButContractual());
		assertFalse(cons(FundingScheme.HORIZON_EUROPE).isAcademicButContractual());
		assertFalse(cons(FundingScheme.HOSTING_ORGANIZATION).isAcademicButContractual());
		assertFalse(cons(FundingScheme.IDEX).isAcademicButContractual());
		assertFalse(cons(FundingScheme.INTERNATIONAL_COMPANY).isAcademicButContractual());
		assertTrue(cons(FundingScheme.INTERNATIONAL_UNIVERSITY).isAcademicButContractual());
		assertFalse(cons(FundingScheme.INTERNTATIONAL_OTHER).isAcademicButContractual());
		assertFalse(cons(FundingScheme.INTERREG).isAcademicButContractual());
		assertFalse(cons(FundingScheme.ISITE).isAcademicButContractual());
		assertFalse(cons(FundingScheme.JPIEU).isAcademicButContractual());
		assertFalse(cons(FundingScheme.LIFE).isAcademicButContractual());
		assertFalse(cons(FundingScheme.LOCAL_INSTITUTION).isAcademicButContractual());
		assertFalse(cons(FundingScheme.NICOLAS_BAUDIN).isAcademicButContractual());
		assertFalse(cons(FundingScheme.NOT_FUNDED).isAcademicButContractual());
		assertFalse(cons(FundingScheme.PHC).isAcademicButContractual());
		assertFalse(cons(FundingScheme.PIA).isAcademicButContractual());
		assertFalse(cons(FundingScheme.FRANCE_2030).isAcademicButContractual());
		assertFalse(cons(FundingScheme.REGION_BFC).isAcademicButContractual());
		assertFalse(cons(FundingScheme.SELF_FUNDING).isAcademicButContractual());
		assertAllTreated();
	}

	@Test
	public void reverseOrdinal() throws Exception {
		assertEquals(0, cons(FundingScheme.INTERNATIONAL_COMPANY).reverseOrdinal());
		assertEquals(1, cons(FundingScheme.PHC).reverseOrdinal());
		assertEquals(2, cons(FundingScheme.CSC).reverseOrdinal());
		assertEquals(3, cons(FundingScheme.CONACYT).reverseOrdinal());
		assertEquals(4, cons(FundingScheme.NICOLAS_BAUDIN).reverseOrdinal());
		assertEquals(5, cons(FundingScheme.CAMPUS_FRANCE).reverseOrdinal());
		assertEquals(6, cons(FundingScheme.FITEC).reverseOrdinal());
		assertEquals(7, cons(FundingScheme.INTERNTATIONAL_OTHER).reverseOrdinal());
		assertEquals(8, cons(FundingScheme.INTERNATIONAL_UNIVERSITY).reverseOrdinal());
		assertEquals(9, cons(FundingScheme.EU_COMPANY).reverseOrdinal());
		assertEquals(10, cons(FundingScheme.HORIZON_EUROPE).reverseOrdinal());
		assertEquals(11, cons(FundingScheme.H2020).reverseOrdinal());
		assertEquals(12, cons(FundingScheme.LIFE).reverseOrdinal());
		assertEquals(13, cons(FundingScheme.EDIH).reverseOrdinal());
		assertEquals(14, cons(FundingScheme.INTERREG).reverseOrdinal());
		assertEquals(15, cons(FundingScheme.JPIEU).reverseOrdinal());
		assertEquals(16, cons(FundingScheme.COST_ACTION).reverseOrdinal());
		assertEquals(17, cons(FundingScheme.EUROSTAR).reverseOrdinal());
		assertEquals(18, cons(FundingScheme.EUREKA).reverseOrdinal());
		assertEquals(19, cons(FundingScheme.FEDER).reverseOrdinal());
		assertEquals(20, cons(FundingScheme.EU_OTHER).reverseOrdinal());
		assertEquals(21, cons(FundingScheme.EU_UNIVERSITY).reverseOrdinal());
		assertEquals(22, cons(FundingScheme.FRENCH_COMPANY).reverseOrdinal());
		assertEquals(23, cons(FundingScheme.CIFRE).reverseOrdinal());
		assertEquals(24, cons(FundingScheme.FRANCE_2030).reverseOrdinal());
		assertEquals(25, cons(FundingScheme.PIA).reverseOrdinal());
		assertEquals(26, cons(FundingScheme.IDEX).reverseOrdinal());
		assertEquals(27, cons(FundingScheme.ISITE).reverseOrdinal());
		assertEquals(28, cons(FundingScheme.ANR).reverseOrdinal());
		assertEquals(29, cons(FundingScheme.ADEME).reverseOrdinal());
		assertEquals(30, cons(FundingScheme.FUI).reverseOrdinal());
		assertEquals(31, cons(FundingScheme.CPER).reverseOrdinal());
		assertEquals(32, cons(FundingScheme.REGION_BFC).reverseOrdinal());
		assertEquals(33, cons(FundingScheme.CARNOT).reverseOrdinal());
		assertEquals(34, cons(FundingScheme.FRENCH_OTHER).reverseOrdinal());
		assertEquals(35, cons(FundingScheme.FRENCH_UNIVERSITY).reverseOrdinal());
		assertEquals(36, cons(FundingScheme.HOSTING_ORGANIZATION).reverseOrdinal());
		assertEquals(37, cons(FundingScheme.LOCAL_INSTITUTION).reverseOrdinal());
		assertEquals(38, cons(FundingScheme.SELF_FUNDING).reverseOrdinal());
		assertEquals(39, cons(FundingScheme.NOT_FUNDED).reverseOrdinal());
		assertAllTreated();
	}

 }