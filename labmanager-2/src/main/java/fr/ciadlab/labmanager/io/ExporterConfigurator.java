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

package fr.ciadlab.labmanager.io;

import java.time.LocalDate;
import java.util.function.Predicate;

import fr.ciadlab.labmanager.entities.member.MemberStatus;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;

/** Configurator for an exporter.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public class ExporterConfigurator {

	private Predicate<Person> personSelector;

	private Predicate<ResearchOrganization> organizationSelector;

	private boolean enableSelectedPersonFormat = true;

	private boolean enableResearcherFormat = true;

	private boolean enablePhDStudentFormat = true;

	private boolean enablePostdocEngineerFormat = true;

	private boolean enableTitleColor = true;

	/** Constructor.
	 */
	public ExporterConfigurator() {
		//
	}

	/** Replies if the titles could be drawn with color font.
	 *
	 * @return {@code true} for color font.
	 */
	public boolean isColoredTitle() {
		return this.enableTitleColor;
	}

	/** Disable the coloring of the titles.
	 *
	 * @return this.
	 */
	public ExporterConfigurator disableTitleColor() {
		this.enableTitleColor = false;
		return this;
	}

	/** Replies if the names of the organization Postdoc/engineer should be formated.
	 *
	 * @return {@code true} for formatting the names.
	 */
	public boolean isPostdocEngineerNameFormatted() {
		return this.enablePostdocEngineerFormat;
	}

	/** Disable the format of the names of the organization Postdoc/engineer.
	 *
	 * @return this.
	 */
	public ExporterConfigurator disablePostdocEngineerFormat() {
		this.enablePostdocEngineerFormat = false;
		return this;
	}

	/** Replies if the names of the organization PhD students should be formated.
	 *
	 * @return {@code true} for formatting the names.
	 */
	public boolean isPhDStudentNameFormatted() {
		return this.enablePhDStudentFormat;
	}

	/** Disable the format of the names of the organization PhD students.
	 *
	 * @return this.
	 */
	public ExporterConfigurator disablePhDStudentFormat() {
		this.enablePhDStudentFormat = false;
		return this;
	}

	/** Replies if the names of the organization researchers should be formated.
	 *
	 * @return {@code true} for formatting the names.
	 */
	public boolean isResearcherNameFormatted() {
		return this.enableResearcherFormat;
	}

	/** Disable the format of the names of the organization researchers.
	 *
	 * @return this.
	 */
	public ExporterConfigurator disableResearcherFormat() {
		this.enableResearcherFormat = false;
		return this;
	}

	/** Replies if the names of the selected authors should be formated.
	 *
	 * @return {@code true} for formatting the names.
	 */
	public boolean isSelectedPersonNameFormatted() {
		return this.enableSelectedPersonFormat;
	}

	/** Disable the format of the names of the selected authors.
	 *
	 * @return this.
	 */
	public ExporterConfigurator disableSelectedPersonFormat() {
		this.enableSelectedPersonFormat = false;
		return this;
	}

	/** Change the selector for the persons.
	 *
	 * @param selector the selector or {@code null}.
	 * @return this.
	 */
	public ExporterConfigurator selectPerson(Predicate<Person> selector) {
		this.personSelector = selector;
		return this;
	}

	/** Replies if a person selector is defined
	 *
	 * @return {@code true} if a person selector is defined.
	 */
	public boolean hasPersonSelector() {
		return this.personSelector != null;
	}

	/** Change the selector for the organization.
	 *
	 * @param selector the selector or {@code null}.
	 * @return this.
	 */
	public ExporterConfigurator selectOrganization(Predicate<ResearchOrganization> selector) {
		this.organizationSelector = selector;
		return this;
	}

	/** Replies if an organization selector is defined
	 *
	 * @return {@code true} if an organization selector is defined.
	 */
	public boolean hasOrganizationSelector() {
		return this.organizationSelector != null;
	}

	private static boolean isValidPeriod(Membership membership, int year) {
		final LocalDate start = LocalDate.of(year, 1, 1);
		final LocalDate end = LocalDate.of(year, 12, 31);
		return membership.isActiveIn(start, end);
	}

	private static boolean isResearcher(Membership membership) {
		final MemberStatus it = membership.getMemberStatus();
		return it.isResearcher() && it != MemberStatus.POSTDOC && it != MemberStatus.PHD_STUDENT
				&& it != MemberStatus.ASSOCIATED_MEMBER;
	}

	private static boolean isPhDStudent(Membership membership) {
		final MemberStatus it = membership.getMemberStatus();
		return it == MemberStatus.PHD_STUDENT;
	}

	private static boolean isPostdocEngineer(Membership membership) {
		final MemberStatus it = membership.getMemberStatus();
		return it.isTechnicalStaff() || it == MemberStatus.POSTDOC;
	}

	private static ExportedAuthorStatus max(ExportedAuthorStatus status0, ExportedAuthorStatus status1) {
		if (status0.ordinal() <= status1.ordinal()) {
			return status0;
		}
		return status1;
	}

	/** Replies the status of an exported author. If there is multiple memberships active at the same time,
	 * the highest membership position is considered. 
	 *
	 * @param person the person to test.
	 * @param year the current year.
	 * @return the status, never {@code null}.
	 */
	public ExportedAuthorStatus getExportedAuthorStatusFor(Person person, int year) {
		if (person != null) {
			if (this.personSelector != null && this.personSelector.test(person)) {
				return ExportedAuthorStatus.SELECTED_PERSON;
			}
			ExportedAuthorStatus status = ExportedAuthorStatus.OTHER;
			for (final Membership membership : person.getResearchOrganizations()) {
				if (isValidPeriod(membership, year)
						&& (this.organizationSelector == null || this.organizationSelector.test(membership.getResearchOrganization()))) {
					if (isResearcher(membership)) {
						status = max(status, ExportedAuthorStatus.RESEARCHER);
					} else if (isPhDStudent(membership)) {
						status = max(status, ExportedAuthorStatus.PHD_STUDENT);
					} else if (isPostdocEngineer(membership)) {
						status = max(status, ExportedAuthorStatus.POSTDOC_ENGINEER);
					}
				}
			}
			return status;
		}
		return ExportedAuthorStatus.OTHER;
	}

}
