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

package fr.ciadlab.labmanager.indicators.members.fte;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.member.MemberStatus;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.indicators.AbstractAnnualIndicator;
import fr.ciadlab.labmanager.utils.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/** Count the number of PhD students per year.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Component
public class PhdStudentFteIndicator extends AbstractAnnualIndicator {

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 */
	public PhdStudentFteIndicator(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants) {
		super(messages, constants, AbstractAnnualIndicator::average);
	}

	@Override
	public String getName() {
		return getMessage("phdStudentFteIndicator.name"); //$NON-NLS-1$
	}

	@Override
	public String getLabel(Unit unit) {
		return getLabelWithoutYears("phdStudentFteIndicator.label"); //$NON-NLS-1$
	}

	/** Replies if the given membership is for a PhD student.
	 *
	 * @param membership the membership to test.
	 * @param startDate the start date of the active period.
	 * @param endDate the end date of the active period.
	 * @return {@code true} if the membership is for a PhD student.
	 */
	public static boolean isPhdStudent(Membership membership, LocalDate startDate, LocalDate endDate) {
		if (membership != null && membership.isActiveIn(startDate, endDate)) {
			final MemberStatus status = membership.getMemberStatus();
			return !status.isExternalPosition() && status == MemberStatus.PHD_STUDENT;
		}
		return false;
	}

	@Override
	public Map<Integer, Number> getValuesPerYear(ResearchOrganization organization, int startYear, int endYear) {
		final LocalDate startDate = LocalDate.of(startYear, 1, 1);
		final LocalDate endDate = LocalDate.of(endYear, 12, 31);
		final Map<Integer, Number> values = new ConcurrentHashMap<>();
		organization.getMemberships()
			.parallelStream()
			.filter(it -> isPhdStudent(it, startDate, endDate))
			.forEach(it -> {
				updateValues(values, it, startYear, endYear);
			});
		//
		setComputationDetails(values);
		return values;
	}

	private static void updateValues(Map<Integer, Number> values, Membership membership, int startYear, int endYear) {
		final float fte = membership.getMemberStatus().getUsualResearchFullTimeEquivalent();
		for (int year = startYear; year <= endYear; ++year) {
			final int dayCount = membership.daysInYear(year);
			float annualFte = dayCount / LocalDate.of(year, 1, 1).lengthOfYear();
			annualFte *= fte;
			values.merge(Integer.valueOf(year), Float.valueOf(annualFte), 
					(k0, k1) -> {
						final float sum = k0.floatValue() + k1.floatValue();
						return Float.valueOf(sum);
					});
		}
	}

}
