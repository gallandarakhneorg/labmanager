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

package fr.utbm.ciad.labmanager.views.tl.dashboard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.member.Membership;
import fr.utbm.ciad.labmanager.data.organization.OrganizationAddress;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.data.scientificaxis.ScientificAxis;
import fr.utbm.ciad.labmanager.services.conference.ConferenceService;
import fr.utbm.ciad.labmanager.services.member.MembershipStatService;
import fr.utbm.ciad.labmanager.services.organization.ResearchOrganizationService;
import fr.utbm.ciad.labmanager.views.tl.AbstractViewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/** REST Controller for the dashboard views.
 * 
 * @author $Author: sgalland$
 * @author $Author: pgoubet$
 * @author $Author: anoubli$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see ConferenceService
 * @since 3.6
 */
@RestController
@CrossOrigin
public class MembershipDashboardViewController extends AbstractViewController {

	private ResearchOrganizationService organizationService;

	private MembershipStatService membershipStatService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param organizationService the service for accessing to the research organizations.
	 * @param membershipStatService the service for computing the membership stats.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public MembershipDashboardViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ResearchOrganizationService organizationService,
			@Autowired MembershipStatService membershipStatService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.organizationService = organizationService;
		this.membershipStatService = membershipStatService;
	}

	/** Replies the model-view component for displaying the dashboard of the memberships of a specific research organization.
	 *
	 * @param organization the identifier or the name of the organization.
	 * @param startYear the first year of the publications. If it is provided, the {@code age} is ignored.
	 * @param endYear the last year of the publications. If it is provided, the {@code age} is ignored.
	 * @param age the age of the publications. If it is not provided, the default age is {@code 6}.
	 * @param username the name of the logged-in user.
	 * @return the model-view component.
	 */
	@GetMapping("/membershipDashboard")
	public ModelAndView membershipDashboard(
			@RequestParam(required = true) String organization,
			@RequestParam(required = false) Integer startYear,
			@RequestParam(required = false) Integer endYear,
			@RequestParam(required = false, defaultValue = "6") int age,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		readCredentials(username, "membershipDashboard", organization); //$NON-NLS-1$
		//
		final ResearchOrganization organizationObj = getOrganizationWith(organization, this.organizationService);
		if (organizationObj == null) {
			throw new IllegalArgumentException("Organization not found for: " + organization); //$NON-NLS-1$
		}
		//
		final Set<Membership> membershipSet = organizationObj.getMemberships();
		final List<Membership> memberships;
		final int minYear;
		final int maxYear;
		if (startYear != null && endYear != null) {
			final LocalDate sy;
			final LocalDate ey;
			if (startYear.intValue() > endYear.intValue()) {
				sy = LocalDate.of(endYear.intValue(), 1, 1);
				ey = LocalDate.of(startYear.intValue(), 12, 31);
			} else {
				sy = LocalDate.of(startYear.intValue(), 1, 1);
				ey = LocalDate.of(endYear.intValue(), 12, 31);
			}
			memberships = membershipSet.stream()
					.filter(it -> it.isActiveIn(sy, ey))
					.collect(Collectors.toList());
			minYear = sy.getYear();
			maxYear = ey.getYear();
		} else if (startYear != null) {
			memberships = membershipSet.stream()
					.filter(it -> it.getMemberToWhen() == null || it.getMemberToWhen().getYear() >= startYear.intValue())
					.collect(Collectors.toList());
			minYear = startYear.intValue();
			maxYear = LocalDate.now().getYear();
		} else if (endYear != null) {
			memberships = membershipSet.stream()
					.filter(it -> it.getMemberSinceWhen() == null || it.getMemberSinceWhen().getYear() <= endYear.intValue())
					.collect(Collectors.toList());				
			minYear = endYear.intValue() - age;
			maxYear = endYear.intValue();
		} else {
			maxYear = LocalDate.now().getYear();
			minYear = maxYear - age;
			memberships = membershipSet.stream()
					.filter(it -> (it.getMemberToWhen() == null || it.getMemberToWhen().getYear() >= minYear)
							&& (it.getMemberSinceWhen() == null || it.getMemberSinceWhen().getYear() <= maxYear))
					.collect(Collectors.toList());
		}
		//
		final ModelAndView modelAndView = new ModelAndView("membershipDashboard"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, false);
		modelAndView.addObject("membersPerYear", this.membershipStatService.getNumberOfMembersPerYear(memberships, minYear, maxYear)); //$NON-NLS-1$
		//
		final List<OrganizationAddress> addresses = organizationObj.getAddresses().stream().collect(Collectors.toList());
		modelAndView.addObject("membersPerAddress", this.membershipStatService.getNumberOfMembersPerAddress(memberships, //$NON-NLS-1$ 
				minYear, maxYear, organizationObj, addresses));
		modelAndView.addObject("addresses", addresses.stream().map(it -> it.getName()).collect(Collectors.toList())); //$NON-NLS-1$
		//
		final List<ScientificAxis> axes = new ArrayList<>();
		modelAndView.addObject("membersPerScientificAxis", this.membershipStatService.getNumberOfMembersPerScientificAxis(memberships, //$NON-NLS-1$ 
				minYear, maxYear, organizationObj, axes));
		modelAndView.addObject("scientificAxes", axes.stream() //$NON-NLS-1$
				.map(it -> it.getAcronym() + " - " + it.getName()) //$NON-NLS-1$$
				.collect(Collectors.toList()));
		//
		return modelAndView;
	}

}
