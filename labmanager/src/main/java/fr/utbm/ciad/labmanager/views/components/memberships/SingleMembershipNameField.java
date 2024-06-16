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

package fr.utbm.ciad.labmanager.views.components.memberships;

import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.member.Membership;
import fr.utbm.ciad.labmanager.services.member.MembershipService;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.services.organization.OrganizationAddressService;
import fr.utbm.ciad.labmanager.services.organization.ResearchOrganizationService;
import fr.utbm.ciad.labmanager.services.scientificaxis.ScientificAxisService;
import fr.utbm.ciad.labmanager.services.user.UserService;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractSingleEntityNameField;
import org.slf4j.Logger;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/** Implementation of a field for entering an organization membership, with auto-completion from the membership JPA entities.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class SingleMembershipNameField extends AbstractSingleEntityNameField<Membership> {

	private static final long serialVersionUID = 874082766593597949L;

	/** Constructor.
	 *
	 * @param membershipService the service for accessing the membership JPA entities.
	 * @param locale the locale used of showing the messages.
	 * @param creationWithUiCallback a lambda that is invoked for creating a new membership using an UI, e.g., an editor. The first argument is the new membership entity.
	 *      The second argument is a lambda that must be invoked to inject the new membership in the {@code SingleMembershipNameField}.
	 *      This second lambda takes the created membership.
	 * @param creationWithoutUiCallback a lambda that is invoked for creating a new membership without using an UI. The first argument is the new membership entity.
	 *      The second argument is a lambda that must be invoked to inject the new membership in the {@code SingleMembershipNameField}.
	 *      This second lambda takes the created membership.
	 * @param entityInitializer the callback function for initializing the properties of each loaded membership.
	 */
	public SingleMembershipNameField(MembershipService membershipService, Supplier<Locale> locale, SerializableBiConsumer<Membership, Consumer<Membership>> creationWithUiCallback,
			SerializableBiConsumer<Membership, Consumer<Membership>> creationWithoutUiCallback, Consumer<Membership> entityInitializer) {
		super(
				combo -> {
					combo.setRenderer(new ComponentRenderer<>(it -> ComponentFactory.newMembershipAvatar(it, membershipService.getMessageSourceAccessor(), locale.get())));
					combo.setItemLabelGenerator(it -> ComponentFactory.newMembershipLabel(it, membershipService.getMessageSourceAccessor(), locale.get()));
				},
				combo -> {
					combo.setItems(query -> membershipService.getSupervisableMemberships(
							VaadinSpringDataHelpers.toSpringPageRequest(query),
							createMembershipFilter(query.getFilter()),
							entityInitializer).stream());
				},
				creationWithUiCallback, creationWithoutUiCallback);
	}

	/** Constructor.
	 *
	 * @param membershipService the service for accessing the membership JPA entities.
	 * @param personService the service for accessing the person JPA entities.
	 * @param userService the service for accessing the connected user JPA entities.
	 * @param organizationService the service for accessing the research organization JPA entities.
	 * @param addressService the service for accessing the organization address JPA entities.
	 * @param axisService the service for accessing the scientific axis JPA entities.
	 * @param authenticatedUser the user that is currently authenticated.
	 * @param creationTitle the title of the dialog box for creating the person.
	 * @param logger the logger for abnormal messages to the lab manager administrator.
	 * @param locale the locale used of showing the messages.
	 * @param entityInitializer the callback function for initializing the properties of each loaded research organization.
	 */
	public SingleMembershipNameField(MembershipService membershipService, PersonService personService, UserService userService,
			ResearchOrganizationService organizationService, OrganizationAddressService addressService,
			ScientificAxisService axisService, AuthenticatedUser authenticatedUser,
			String creationTitle, Logger logger, Supplier<Locale> locale, Consumer<Membership> entityInitializer) {
		this(membershipService, locale,
				(newMembership, saver) -> {
					final var membershipContext = membershipService.startEditing(newMembership);
					final var editor = new EmbeddedMembershipEditor(
							membershipContext,
							true,
							personService, userService, organizationService, addressService, axisService,
							authenticatedUser, membershipService.getMessageSourceAccessor());
					ComponentFactory.openEditionModalDialog(creationTitle, editor, true,
							(dialog, changedOrganization) -> saver.accept(changedOrganization),
							null);
				},
				(newMembership, saver) -> {
					try {
						final var creationContext = membershipService.startEditing(newMembership);
						creationContext.save();
						saver.accept(creationContext.getEntity());
					} catch (Throwable ex) {
						logger.warn("Error when creating a membership by " + AuthenticatedUser.getUserName(authenticatedUser) //$NON-NLS-1$
							+ ": " + ex.getLocalizedMessage() + "\n-> " + ex.getLocalizedMessage(), ex); //$NON-NLS-1$ //$NON-NLS-2$
						ComponentFactory.showErrorNotification(membershipService.getMessageSourceAccessor().getMessage("views.membership.creation_error", new Object[] { ex.getLocalizedMessage() })); //$NON-NLS-1$
					}
				},
				entityInitializer);
	}

	private static Specification<Membership> createMembershipFilter(Optional<String> filter) {
		if (filter.isPresent()) {
			return (root, query, criteriaBuilder) ->  {
				return ComponentFactory.newPredicateContainsOneOf(filter.get(), root, query, criteriaBuilder,
						(keyword, predicates, root0, criteriaBuilder0) -> {
							predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("person").get("firstName")), keyword)); //$NON-NLS-1$ //$NON-NLS-2$
							predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("person").get("lastName")), keyword)); //$NON-NLS-1$ //$NON-NLS-2$
						});
			};
		}
		return null;
	}

	@Override
	protected Membership createNewEntity(String customName) {
		final var newMembership = new Membership();
		return newMembership;
	}

}
