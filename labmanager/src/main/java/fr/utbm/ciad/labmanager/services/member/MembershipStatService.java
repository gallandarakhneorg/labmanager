/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.utbm.ciad.labmanager.services.member;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.EntityUtils;
import fr.utbm.ciad.labmanager.data.member.MemberStatus;
import fr.utbm.ciad.labmanager.data.member.Membership;
import fr.utbm.ciad.labmanager.data.member.MembershipRepository;
import fr.utbm.ciad.labmanager.data.organization.OrganizationAddress;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.data.scientificaxis.ScientificAxis;
import fr.utbm.ciad.labmanager.services.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for computing the stats related to the memberships to research organizations.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Service
public class MembershipStatService extends AbstractService {

	private MembershipRepository membershipRepository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param membershipRepository the membership repository.
	 */
	public MembershipStatService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired MembershipRepository membershipRepository) {
		super(messages, constants);
		this.membershipRepository = membershipRepository;
	}

	/** Replies the numbers of members per year for the given set of memberships.
	 *
	 * @param memberships the memberships to analyze.
	 * @param minYear the minimal year to consider.
	 * @param maxYear the minimal year to consider.
	 * @return rows with: year, PU, DR MCF-HDR, MCF, postdoc, phd student, engineer, other.
	 */
	@SuppressWarnings("static-method")
	public List<List<Number>> getNumberOfMembersPerYear(List<Membership> memberships, int minYear, int maxYear) {
		final var membersPerYear = new HashMap<Integer, Map<MemberStatus, Integer>>();
		final var etpsPerYear = new HashMap<Integer, Float>();
		memberships.stream()
				.forEach(it -> {
					for (var y = minYear; y <= maxYear; ++y) {
						final var startDate = LocalDate.of(y, 1, 1);
						final var endDate = LocalDate.of(y, 12, 31);
						if (it.isActiveIn(startDate, endDate)) {
							final var yobj = Integer.valueOf(y);
							final var counts = membersPerYear.computeIfAbsent(yobj, it0 -> new HashMap<>());
							final var oldValue = counts.get(it.getMemberStatus());
							if (oldValue == null) {
								counts.put(it.getMemberStatus(), Integer.valueOf(1));
							} else {
								counts.put(it.getMemberStatus(), Integer.valueOf(oldValue.intValue() + 1));
							}
							if (it.isPermanentPosition()) {
								final var etpRatio = it.getMemberStatus().getUsualResearchFullTimeEquivalent();
								final var membershipDays = it.daysInYear(y);
								final var timeRatio = (float) membershipDays / (float) startDate.lengthOfYear();
								final var etp = etpRatio * timeRatio;
								final var oldFValue = etpsPerYear.get(yobj);
								if (oldFValue == null) {
									etpsPerYear.put(yobj, Float.valueOf(etp));
								} else {
									etpsPerYear.put(yobj, Float.valueOf(oldFValue.floatValue() + etp));
								}
							}
						}
					}
				});
		return membersPerYear.entrySet().stream()
			.sorted((a, b) -> a.getKey().compareTo(b.getKey()))
			.map(it -> {
				final var columns = new ArrayList<Number>(7);
				columns.add(it.getKey());
				// PU
				columns.add(getInteger(it.getValue(),
						MemberStatus.EMERITUS_FULL_PROFESSOR, MemberStatus.FULL_PROFESSOR, MemberStatus.RESEARCH_DIRECTOR));
				// DR
				columns.add(getInteger(it.getValue(),
						MemberStatus.RESEARCH_DIRECTOR));
				// MCF-HDR
				columns.add(getInteger(it.getValue(),
						MemberStatus.ASSOCIATE_PROFESSOR_HDR, MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR));
				// MCF
				columns.add(getInteger(it.getValue(),
						MemberStatus.ASSOCIATE_PROFESSOR, MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER,
						MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD, MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR,
						MemberStatus.RESEARCHER_PHD));
				// Postdocs
				columns.add(getInteger(it.getValue(),
						MemberStatus.POSTDOC));
				// PhD students
				columns.add(getInteger(it.getValue(),
						MemberStatus.PHD_STUDENT));
				// Engineer
				columns.add(getInteger(it.getValue(),
						MemberStatus.ENGINEER, MemberStatus.ENGINEER_PHD, MemberStatus.RESEARCH_ENGINEER,
						MemberStatus.RESEARCH_ENGINEER_PHD));
				// Other
				columns.add(getInteger(it.getValue(),
						MemberStatus.ADMIN, MemberStatus.RESEARCHER, MemberStatus.TEACHER, MemberStatus.TEACHER_PHD));
				// ETP
				var etp = etpsPerYear.get(it.getKey());
				if (etp == null) {
					etp = Float.valueOf(0f);
				}
				columns.add(etp);
				return columns;
			})
			.collect(Collectors.toList());
	}

	private static Integer getInteger(Map<MemberStatus, Integer> values, MemberStatus... statuses) {
		var count = 0;
		for (final var status : statuses) {
			final var value = values.get(status);
			if (value != null) {
				count += value.intValue();
			}
		}
		return Integer.valueOf(count);
	}

	/** Replies the numbers of members per geographical address.
	 *
	 * @param memberships the memberships to analyze.
	 * @param minYear the minimal year to consider.
	 * @param maxYear the minimal year to consider.
	 * @param referenceOrganization the organization that is used as the reference.
	 * @param addresses the addresses of the reference organization in the order that they should appear in the columns.
	 * @return rows with: year, adr1, ..., adrn, other.
	 */
	@SuppressWarnings("static-method")
	public List<List<Object>> getNumberOfMembersPerAddress(List<Membership> memberships, int minYear, int maxYear,
			ResearchOrganization referenceOrganization, List<OrganizationAddress> addresses) {
		final var membersPerAddress = new HashMap<Integer, Map<OrganizationAddress, Integer>>();
		final var noAddress = new HashMap<Integer, Integer>();
		memberships.stream()
				.filter(it -> it.isMainPosition() && it.getResearchOrganization().getId() == referenceOrganization.getId())
				.forEach(it -> {
					for (var y = minYear; y <= maxYear; ++y) {
						final var startDate = LocalDate.of(y, 1, 1);
						final var endDate = LocalDate.of(y, 12, 31);
						if (it.isActiveIn(startDate, endDate) && !it.getMemberStatus().isExternalPosition()) {
							final var yobj = Integer.valueOf(y);
							final var counts = membersPerAddress.computeIfAbsent(yobj, it0 -> new HashMap<>());
							final var adr = it.getOrganizationAddress();
							if (adr != null) {
								final var oldValue = counts.get(adr);
								if (oldValue == null) {
									counts.put(adr, Integer.valueOf(1));
								} else {
									counts.put(adr, Integer.valueOf(oldValue.intValue() + 1));
								}
							} else {
								final var oldValue = noAddress.get(yobj);
								if (oldValue == null) {
									noAddress.put(yobj, Integer.valueOf(1));
								} else {
									noAddress.put(yobj, Integer.valueOf(oldValue.intValue() + 1));
								}
							}
						}
					}
				});
		return membersPerAddress.entrySet().stream()
			.sorted((a, b) -> a.getKey().compareTo(b.getKey()))
			.map(it -> {
				final var columns = new ArrayList<>(2 + addresses.size());
				columns.add(it.getKey());
				final var counts = it.getValue();
				for (final var adr : addresses) {
					var count = counts.get(adr);
					if (count == null) {
						count = Integer.valueOf(0);
					}
					columns.add(count);
				}
				var none = noAddress.get(it.getKey());
				if (none == null) {
					none = Integer.valueOf(0);
				}
				columns.add(none);
				return columns;
			})
			.collect(Collectors.toList());
	}

	/** Replies the numbers of members per scientific axis.
	 *
	 * @param memberships the memberships to analyze.
	 * @param minYear the minimal year to consider.
	 * @param maxYear the minimal year to consider.
	 * @param referenceOrganization the organization that is used as the reference.
	 * @param axes output argument that is filled up with the scientific axes added into the columns.
	 * @return rows with: year, axis1, ..., axisn, other.
	 */
	@SuppressWarnings("static-method")
	public List<List<Object>> getNumberOfMembersPerScientificAxis(List<Membership> memberships, int minYear, int maxYear,
			ResearchOrganization referenceOrganization, List<ScientificAxis> axes) {
		final var headers = new TreeSet<>(EntityUtils.getPreferredScientificAxisComparator());
		final var membersPerAxis = new HashMap<Integer, Map<ScientificAxis, Integer>>();
		final var noAxis = new HashMap<Integer, Integer>();
		memberships.stream()
				.filter(it -> it.isMainPosition() && it.getMemberStatus().isResearcher()
						&& !it.getMemberStatus().isExternalPosition()
						&& it.getResearchOrganization().getId() == referenceOrganization.getId())
				.forEach(it -> {
					for (var y = minYear; y <= maxYear; ++y) {
						final var startDate = LocalDate.of(y, 1, 1);
						final var endDate = LocalDate.of(y, 12, 31);
						if (it.isActiveIn(startDate, endDate)) {
							final var yobj = Integer.valueOf(y);
							final var counts = membersPerAxis.computeIfAbsent(yobj, it0 -> new HashMap<>());
							if (!it.getScientificAxes().isEmpty()) {
								for (final var axis : it.getScientificAxes()) {
									headers.add(axis);
									final var oldValue = counts.get(axis);
									if (oldValue == null) {
										counts.put(axis, Integer.valueOf(1));
									} else {
										counts.put(axis, Integer.valueOf(oldValue.intValue() + 1));
									}
								}
							} else {
								final var oldValue = noAxis.get(yobj);
								if (oldValue == null) {
									noAxis.put(yobj, Integer.valueOf(1));
								} else {
									noAxis.put(yobj, Integer.valueOf(oldValue.intValue() + 1));
								}
							}
						}
					}
				});
		axes.clear();
		axes.addAll(headers);
		return membersPerAxis.entrySet().stream()
			.sorted((a, b) -> a.getKey().compareTo(b.getKey()))
			.map(it -> {
				final var columns = new ArrayList<>(2);
				columns.add(it.getKey());
				final var counts = it.getValue();
				for (final var axis : axes) {
					var count = counts.get(axis);
					if (count == null) {
						count = Integer.valueOf(0);
					}
					columns.add(count);
				}
				var none = noAxis.get(it.getKey());
				if (none == null) {
					none = Integer.valueOf(0);
				}
				columns.add(none);
				return columns;
			})
			.collect(Collectors.toList());
	}

	/** Replies if the given identifier is the one of a member of an organization.
	 *
	 * @param id the identifier of a person.
	 * @return {@code true} if the person is member of an organization.
	 * @since 3.6
	 */
	public boolean isMember(int id) {
		return !this.membershipRepository.findAllByPersonId(id).isEmpty();
	}

}
