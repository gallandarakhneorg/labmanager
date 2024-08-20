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

package fr.utbm.ciad.labmanager.views.components.projects;

import com.google.common.base.Strings;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import fr.utbm.ciad.labmanager.data.EntityUtils;
import fr.utbm.ciad.labmanager.data.project.Project;
import fr.utbm.ciad.labmanager.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.services.organization.OrganizationAddressService;
import fr.utbm.ciad.labmanager.services.organization.ResearchOrganizationService;
import fr.utbm.ciad.labmanager.services.project.ProjectService;
import fr.utbm.ciad.labmanager.services.scientificaxis.ScientificAxisService;
import fr.utbm.ciad.labmanager.services.user.UserService;
import fr.utbm.ciad.labmanager.utils.io.filemanager.FileManager;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractMultiEntityNameField;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.function.Consumer;

/** Implementation of a field for entering the names of projects, with auto-completion from the person JPA entities.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class MultiProjectNameField extends AbstractMultiEntityNameField<Project> {

	private static final long serialVersionUID = -2882111972203080259L;

	/** Constructor.
	 *
	 * @param projectService the service for accessing the project JPA entities.
	 * @param creationWithUiCallback a lambda that is invoked for creating a new project using an UI, e.g., an editor. The first argument is the new project entity.
	 *      The second argument is a lambda that must be invoked to inject the new project in the {@code MultiProjectNameField}.
	 *      This second lambda takes the created project.
	 * @param creationWithoutUiCallback a lambda that is invoked for creating a new project without using an UI. The first argument is the new project entity.
	 *      The second argument is a lambda that must be invoked to inject the new project in the {@code MultiProjectNameField}.
	 *      This second lambda takes the created project.
	 * @param initializer the initializer of the loaded projects. It may be {@code null}.
	 */
	public MultiProjectNameField(ProjectService projectService,
			SerializableBiConsumer<Project, Consumer<Project>> creationWithUiCallback,
			SerializableBiConsumer<Project, Consumer<Project>> creationWithoutUiCallback,
			Consumer<Project> initializer) {
		super(
				combo -> {
					combo.setRenderer(new ComponentRenderer<>(createProjectRender(projectService.getFileManager())));
					combo.setItemLabelGenerator(it -> it.getAcronym());
				},
				combo -> {
					if (initializer == null) {
						combo.setItems(query -> projectService.getAllProjects(
								VaadinSpringDataHelpers.toSpringPageRequest(query),
								createProjectFilter(query.getFilter())).stream());
					} else {
						combo.setItems(query -> projectService.getAllProjects(
								VaadinSpringDataHelpers.toSpringPageRequest(query),
								createProjectFilter(query.getFilter()), initializer).stream());
					}
				},
				creationWithUiCallback, creationWithoutUiCallback);
	}

	/** Constructor.
	 *
	 * @param projectService the service for accessing the project JPA entities.
	 * @param creationWithUiCallback a lambda that is invoked for creating a new project using an UI, e.g., an editor. The first argument is the new project entity.
	 *      The second argument is a lambda that must be invoked to inject the new project in the {@code MultiProjectNameField}.
	 *      This second lambda takes the created project.
	 * @param creationWithoutUiCallback a lambda that is invoked for creating a new project without using an UI. The first argument is the new project entity.
	 *      The second argument is a lambda that must be invoked to inject the new project in the {@code MultiProjectNameField}.
	 *      This second lambda takes the created project.
	 */
	public MultiProjectNameField(ProjectService projectService,
			SerializableBiConsumer<Project, Consumer<Project>> creationWithUiCallback,
			SerializableBiConsumer<Project, Consumer<Project>> creationWithoutUiCallback) {
		this(projectService, creationWithUiCallback, creationWithoutUiCallback, null);
	}

	/** Constructor.
	 *
	 * @param projectService the service for accessing the project JPA entities.
	 * @param organizationService the service for accessing the organization JPA entities.
	 * @param addressService the service for accessing the address JPA entities.
	 * @param personService the service for accessing the person JPA entities.
	 * @param userService the service for accessing the user JPA entities.
	 * @param authenticatedUser the user that is currently authenticated.
	 * @param axisService the service for accessing the scientific axis JPA entities.
	 * @param creationTitle the title of the dialog box for creating the project.
	 * @param messages the accessor to the localized messages.
	 * @param logger the logger for abnormal messages to the lab manager administrator.
	 * @param initializer the initializer of the loaded projects. It may be {@code null}.
	 */
	public MultiProjectNameField(ProjectService projectService,
			ResearchOrganizationService organizationService, OrganizationAddressService addressService,
			PersonService personService, UserService userService, AuthenticatedUser authenticatedUser,
			ScientificAxisService axisService, String creationTitle,
			MessageSourceAccessor messages, Logger logger, Consumer<Project> initializer) {
		this(projectService,
				(newProject, saver) -> {
					final var projectContext = projectService.startEditing(newProject);
					final var editor = new EmbeddedProjectEditor(
							projectContext, organizationService, addressService,
							personService, userService, axisService,
							projectService.getFileManager(),
							authenticatedUser, messages);
					ComponentFactory.openEditionModalDialog(creationTitle, editor, true,
							(dialog, changedOrganization) -> saver.accept(changedOrganization),
							null);
				},
				(newProject, saver) -> {
					try {
						final var creationContext = projectService.startEditing(newProject);
						creationContext.save();
						saver.accept(creationContext.getEntity());
					} catch (Throwable ex) {
						logger.warn("Error when creating a project by " + AuthenticatedUser.getUserName(authenticatedUser) //$NON-NLS-1$
							+ ": " + ex.getLocalizedMessage() + "\n-> " + ex.getLocalizedMessage(), ex); //$NON-NLS-1$ //$NON-NLS-2$
						ComponentFactory.showErrorNotification(projectService.getMessageSourceAccessor().getMessage("views.projects.creation_error", new Object[] { ex.getLocalizedMessage() })); //$NON-NLS-1$
					}
				},
				initializer);
	}

	/** Constructor.
	 *
	 * @param projectService the service for accessing the project JPA entities.
	 * @param organizationService the service for accessing the organization JPA entities.
	 * @param addressService the service for accessing the address JPA entities.
	 * @param personService the service for accessing the person JPA entities.
	 * @param userService the service for accessing the user JPA entities.
	 * @param authenticatedUser the user that is currently authenticated.
	 * @param axisService the service for accessing the scientific axis JPA entities.
	 * @param creationTitle the title of the dialog box for creating the project.
	 * @param messages the accessor to the localized messages.
	 * @param logger the logger for abnormal messages to the lab manager administrator.
	 */
	public MultiProjectNameField(ProjectService projectService,
			ResearchOrganizationService organizationService, OrganizationAddressService addressService,
			PersonService personService, UserService userService, AuthenticatedUser authenticatedUser,
			ScientificAxisService axisService, String creationTitle,
			MessageSourceAccessor messages, Logger logger) {
		this(projectService, organizationService, addressService, personService, userService,
				authenticatedUser, axisService, creationTitle, messages, logger, null);
	}

	private static SerializableFunction<Project, Component> createProjectRender(FileManager fileManager) {
		if (fileManager == null) {
			return ComponentFactory::newProjectAvatar;
		}
		return project -> ComponentFactory.newProjectAvatar(project, fileManager);
	}

	private static Specification<Project> createProjectFilter(Optional<String> filter) {
		if (filter.isPresent()) {
			return (root, query, criteriaBuilder) -> 
			ComponentFactory.newPredicateContainsOneOf(filter.get(), root, query, criteriaBuilder,
					(keyword, predicates, root0, criteriaBuilder0) -> {
						predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("acronym")), keyword)); //$NON-NLS-1$
						predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("scientificTitle")), keyword)); //$NON-NLS-1$
					});
		}
		return null;
	}

	@Override
	protected Project createNewEntity(String customName) {
		final var newProject = new Project();
		
		if (!Strings.isNullOrEmpty(customName)) {
			final var parts = customName.split("\\s*" + EntityUtils.ACRONYM_NAME_SEPARATOR + "\\s*", 2); //$NON-NLS-1$ //$NON-NLS-2$
			if (parts.length > 1) {
				newProject.setAcronym(parts[0]);
				newProject.setScientificTitle(parts[1]);
			} else {
				newProject.setAcronym(customName);
			}
		}

		return newProject;
	}

}
