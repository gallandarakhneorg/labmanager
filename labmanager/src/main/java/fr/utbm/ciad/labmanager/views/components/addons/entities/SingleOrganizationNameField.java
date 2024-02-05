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

package fr.utbm.ciad.labmanager.views.components.addons.entities;

import java.util.Optional;
import java.util.function.Consumer;

import com.google.common.base.Strings;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.services.organization.OrganizationAddressService;
import fr.utbm.ciad.labmanager.services.organization.ResearchOrganizationService;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.organizations.EmbeddedOrganizationEditor;
import org.slf4j.Logger;
import org.springframework.data.jpa.domain.Specification;

/** Implementation of a field for entering the name of an organization, with auto-completion from the person JPA entities.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class SingleOrganizationNameField extends AbstractSingleEntityNameField<ResearchOrganization> {

	private static final long serialVersionUID = -4319317824778208340L;

	/** Constructor.
	 *
	 * @param organizationService the service for accessing the organization JPA entities.
	 * @param creationWithUiCallback a lambda that is invoked for creating a new person using an UI, e.g., an editor. The first argument is the new person entity.
	 *      The second argument is a lambda that must be invoked to inject the new person in the {@code SinglePersonNameField}.
	 *      This second lambda takes the created person.
	 * @param creationWithoutUiCallback a lambda that is invoked for creating a new person without using an UI. The first argument is the new person entity.
	 *      The second argument is a lambda that must be invoked to inject the new person in the {@code SinglePersonNameField}.
	 *      This second lambda takes the created person.
	 * @param entityInitializer the callback function for initializing the properties of each loaded research organization.
	 */
	public SingleOrganizationNameField(ResearchOrganizationService organizationService, SerializableBiConsumer<ResearchOrganization, Consumer<ResearchOrganization>> creationWithUiCallback,
			SerializableBiConsumer<ResearchOrganization, Consumer<ResearchOrganization>> creationWithoutUiCallback, Consumer<ResearchOrganization> entityInitializer) {
		super(
				combo -> {
					combo.setRenderer(new ComponentRenderer<>(ComponentFactory::newOrganizationAvatar));
					combo.setItemLabelGenerator(it -> it.getAcronymAndName());
				},
				combo -> {
					combo.setItems(query -> organizationService.getAllResearchOrganizations(
							VaadinSpringDataHelpers.toSpringPageRequest(query),
							createOrganizationFilter(query.getFilter()),
							entityInitializer).stream());
				},
				creationWithUiCallback, creationWithoutUiCallback);
	}

	/** Constructor.
	 *
	 * @param organizationService the service for accessing the research organization JPA entities.
	 * @param addressService the service for accessing the organization address JPA entities.
	 * @param authenticatedUser the user that is currently authenticated.
	 * @param creationTitle the title of the dialog box for creating the person.
	 * @param logger the logger for abnormal messages to the lab manager administrator.
	 * @param entityInitializer the callback function for initializing the properties of each loaded research organization.
	 */
	public SingleOrganizationNameField(ResearchOrganizationService organizationService, OrganizationAddressService addressService, AuthenticatedUser authenticatedUser,
			String creationTitle, Logger logger, Consumer<ResearchOrganization> entityInitializer) {
		this(organizationService,
				(newOrganization, saver) -> {
					final var organizationContext = organizationService.startEditing(newOrganization);
					final var editor = new EmbeddedOrganizationEditor(
							organizationContext,
							organizationService.getFileManager(),
							authenticatedUser, organizationService.getMessageSourceAccessor(),
							organizationService, addressService);
					ComponentFactory.openEditionModalDialog(creationTitle, editor, true,
							(dialog, changedOrganization) -> saver.accept(changedOrganization),
							null);
				},
				(newOrganization, saver) -> {
					try {
						final var creationContext = organizationService.startEditing(newOrganization);
						creationContext.save();
						saver.accept(creationContext.getEntity());
					} catch (Throwable ex) {
						logger.warn("Error when creating an organization by " + AuthenticatedUser.getUserName(authenticatedUser) //$NON-NLS-1$
							+ ": " + ex.getLocalizedMessage() + "\n-> " + ex.getLocalizedMessage(), ex); //$NON-NLS-1$ //$NON-NLS-2$
						ComponentFactory.showErrorNotification(organizationService.getMessageSourceAccessor().getMessage("views.organizations.creation_error", new Object[] { ex.getLocalizedMessage() })); //$NON-NLS-1$
					}
				},
				entityInitializer);
	}

	private static Specification<ResearchOrganization> createOrganizationFilter(Optional<String> filter) {
		if (filter.isPresent()) {
			return (root, query, criteriaBuilder) -> 
			ComponentFactory.newPredicateContainsOneOf(filter.get(), root, query, criteriaBuilder,
					(keyword, predicates, root0, criteriaBuilder0) -> {
						predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("acronym")), keyword)); //$NON-NLS-1$
						predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), keyword)); //$NON-NLS-1$
					});
		}
		return null;
	}

	@Override
	protected ResearchOrganization createNewEntity(String customName) {
		final var newOrganization = new ResearchOrganization();
		
		if (!Strings.isNullOrEmpty(customName)) {
			final var parts = customName.split("\\s*" + ResearchOrganization.ACRONYM_NAME_SEPARATOR + "\\s*", 2); //$NON-NLS-1$ //$NON-NLS-2$
			if (parts.length > 1) {
				newOrganization.setAcronym(parts[0]);
				newOrganization.setName(parts[1]);
			} else {
				newOrganization.setName(customName);
			}
		}

		return newOrganization;
	}

}
