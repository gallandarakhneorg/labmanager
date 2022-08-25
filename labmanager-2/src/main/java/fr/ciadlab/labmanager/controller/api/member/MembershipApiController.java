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

package fr.ciadlab.labmanager.controller.api.member;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.AbstractComponent;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.member.MemberStatus;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.service.member.MembershipService;
import fr.ciadlab.labmanager.utils.bap.FrenchBap;
import fr.ciadlab.labmanager.utils.cnu.CnuSection;
import fr.ciadlab.labmanager.utils.conrs.ConrsSection;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** REST Controller for memberships.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see AuthorController
 */
@RestController
@CrossOrigin
public class MembershipApiController extends AbstractComponent {

	private MembershipService membershipService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param membershipComparator the comparator of memberships to use for building the views with
	 *     a chronological point of view.
	 * @param membershipService the service for managing the memberships.
	 */
	public MembershipApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired MembershipService membershipService) {
		super(messages);
		this.membershipService = membershipService;
	}

	/** Saving information of a membership. 
	 *
	 * @param person the identifier of the person. If the identifier is not provided, this endpoint is supposed to create
	 *     a person in the database.
	 * @param membership the identifier of the membership. If the identifier is not provided, this endpoint is supposed to create
	 *     a membership in the database.
	 * @param organization the identifier of the organization related to the membership.
	 * @param status the status of the person related to the membership. If {@code null} or empty, the status does not change in
	 *     the existing membership.
	 * @param memberSinceWhen the start date of the membership, or {@code null} to set it as unknown or "since always".
	 * @param memberToWhen the end date of the membership, or {@code null} to set it as unknown or "for the rest of the time".
	 * @param cnuSection the number of the CNU section to which this membership belongs to. If {@code null} or empty, the
	 *     CNU section does not change in the existing membership.
	 * @param conrsSection the number of the CoNRS section to which this membership belongs to. If {@code null} or empty, the
	 *     CoNRS section does not change in the existing membership.
	 * @param frenchBap the type of job for a not-researcher.
	 * @param closeActive in the case that an active membership exists for the person and the organization, this flag
	 *     permits to control the behavior of the controller. If it is evaluated to {@code true}, the existing active
	 *     membership is closed to the date given by {@code memberSinceWhen} (if this date is not given, today is considered.
	 *     If argument {@code closeActive} is evaluated to {@code false}, an error is thrown. 
	 * @param username the login of the logged-in person.
	 * @throws Exception if it is impossible to save the membership in the database.
	 */
	@PostMapping(value = "/" + Constants.MEMBERSHIP_SAVING_ENDPOINT)
	public void saveMembership(
			@RequestParam(required = true) Integer person,
			@RequestParam(required = false) Integer membership,
			@RequestParam(required = false) Integer organization,
			@RequestParam(required = false) String status,
			@RequestParam(required = false) String memberSinceWhen,
			@RequestParam(required = false) String memberToWhen,
			@RequestParam(required = false) Integer cnuSection,
			@RequestParam(required = false) Integer conrsSection,
			@RequestParam(required = false) String frenchBap,
			@RequestParam(required = false, defaultValue = "true") boolean closeActive,
			@CurrentSecurityContext(expression="authentication?.name") String username) throws Exception {
		if (isLoggedUser(username).booleanValue()) {
			try {
				// Parse values to Java objects
				LocalDate startDate = Strings.isNullOrEmpty(memberSinceWhen) ? null : LocalDate.parse(memberSinceWhen);
				final LocalDate endDate = Strings.isNullOrEmpty(memberToWhen) ? null : LocalDate.parse(memberToWhen);
				final MemberStatus statusObj = Strings.isNullOrEmpty(status) ? null : MemberStatus.valueOfCaseInsensitive(status);
				final CnuSection cnuSectionObj = cnuSection == null || cnuSection.intValue() == 0 ? null : CnuSection.valueOf(cnuSection);
				final ConrsSection conrsSectionObj = conrsSection == null || conrsSection.intValue() == 0 ? null : ConrsSection.valueOf(conrsSection);
				final FrenchBap frenchBapObj = Strings.isNullOrEmpty(frenchBap) ? null : FrenchBap.valueOfCaseInsensitive(frenchBap);

				// Check validity of dates
				if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
					throw new IllegalArgumentException("The end date cannot be fore the start date"); //$NON-NLS-1$
				}

				if (membership == null || membership.intValue() == 0) {
					// Create the membership
					if (person == null || person.intValue() == 0) {
						throw new IllegalArgumentException("Person identifier is missed"); //$NON-NLS-1$
					}
					if (organization == null || organization.intValue() == 0) {
						throw new IllegalArgumentException("Organization identifier is missed"); //$NON-NLS-1$
					}
					if (statusObj == null) {
						throw new IllegalArgumentException("Member status is missed"); //$NON-NLS-1$
					}
					boolean continueCreation;
					do {
						continueCreation = false;
						final Pair<Membership, Boolean> result = this.membershipService.addMembership(
								organization.intValue(),
								person.intValue(),
								startDate, endDate, statusObj,
								cnuSectionObj, conrsSectionObj,
								frenchBapObj, false);
						if (!result.getRight().booleanValue()) {
							// The membership already exists, start the dedicated behavior
							if (closeActive) {
								final Membership otherMembership = result.getLeft();
								if (startDate == null) {
									startDate = LocalDate.now();
								}
								final LocalDate pEnd = startDate.minus(1, ChronoUnit.DAYS);
								LocalDate pStart = otherMembership.getMemberSinceWhen();
								if (pStart != null && pEnd.isBefore(pStart)) {
									pStart = pEnd;
								}
								this.membershipService.updateMembershipById(
										otherMembership.getId(),
										Integer.valueOf(otherMembership.getResearchOrganization().getId()),
										pStart, pEnd,
										otherMembership.getMemberStatus(),
										otherMembership.getCnuSection(),
										otherMembership.getConrsSection(),
										otherMembership.getFrenchBap());
								continueCreation = true;
							} else {
								throw new IllegalStateException("A membership is already active. It is not allowed to create multiple active memberships for the same organization"); //$NON-NLS-1$
							}
						}
					} while (continueCreation);
				} else {
					// Update an existing membership.
					this.membershipService.updateMembershipById(
							membership.intValue(),
							organization, startDate, endDate, statusObj,
							cnuSectionObj, conrsSectionObj, frenchBapObj);
				}
			} catch (Throwable ex) {
				getLogger().error(ex.getLocalizedMessage(), ex);
				throw ex;
			}
		} else {
			throw new IllegalAccessException(getMessage("all.notLogged")); //$NON-NLS-1$
		}
	}

	/** Delete a membership from the database.
	 *
	 * @param id the identifier of the membership.
	 * @param username the login of the logged-in person.
	 * @return the HTTP response.
	 */
	@DeleteMapping("/" + Constants.MEMBERSHIP_DELETION_ENDPOINT)
	public ResponseEntity<String> deleteMembership(
			@RequestParam Integer id,
			@CurrentSecurityContext(expression="authentication?.name") String username) {
		if (isLoggedUser(username).booleanValue()) {
			try {
				if (id == null || id.intValue() == 0) {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				final HttpStatus status = this.membershipService.removeMembership(id.intValue());
				return new ResponseEntity<>(status);
			} catch (Exception ex) {
				return new ResponseEntity<>(ex.getLocalizedMessage(),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

}
