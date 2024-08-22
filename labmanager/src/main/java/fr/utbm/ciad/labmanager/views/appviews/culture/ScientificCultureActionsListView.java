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

package fr.utbm.ciad.labmanager.views.appviews.culture;

import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import fr.utbm.ciad.labmanager.data.user.UserRole;
import fr.utbm.ciad.labmanager.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.services.conference.ConferenceService;
import fr.utbm.ciad.labmanager.services.journal.JournalService;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import fr.utbm.ciad.labmanager.services.scientificaxis.ScientificAxisService;
import fr.utbm.ciad.labmanager.services.user.UserService;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import fr.utbm.ciad.labmanager.views.appviews.MainLayout;
import fr.utbm.ciad.labmanager.views.components.conferences.editors.ConferenceEditorFactory;
import fr.utbm.ciad.labmanager.views.components.culture.views.StandardCultureDisseminationListView;
import fr.utbm.ciad.labmanager.views.components.journals.editors.JournalEditorFactory;
import fr.utbm.ciad.labmanager.views.components.persons.editors.PersonEditorFactory;
import fr.utbm.ciad.labmanager.views.components.publications.editors.PublicationEditorFactory;
import jakarta.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;

/** List all the actions for scientific culture dissemination.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Route(value = "scientificCultureActions", layout = MainLayout.class)
@RolesAllowed({UserRole.RESPONSIBLE_GRANT, UserRole.ADMIN_GRANT})
public class ScientificCultureActionsListView extends StandardCultureDisseminationListView implements HasDynamicTitle {
	
	private static final long serialVersionUID = 1664385539083649671L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ScientificCultureActionsListView.class);

	/** Constructor.
	 * 
	 * @param fileManager the manager of files that could be uploaded on the server.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (spring layer).
	 * @param publicationService the service for accessing the scientific publications.
     * @param publicationEditorFactory the factory for creating publication editors.
	 * @param personService the service for accessing the JPA entities for persons.
	 * @param personEditorFactory the factory for creating the person editors.
	 * @param userService the service for accessing the JPA entities for users.
	 * @param journalService the service for accessing the JPA entities for journal.
	 * @param journalEditorFactory the factory for creating the journal editors.
	 * @param conferenceService the service for accessing the JPA entities for conferences.
	 * @param conferenceEditorFactory the factory for creating the conference editors.
	 * @param axisService the service for accessing the JPA entities for scientific axes.
	 */
	public ScientificCultureActionsListView(
			@Autowired DownloadableFileManager fileManager,
			@Autowired AuthenticatedUser authenticatedUser,
			@Autowired MessageSourceAccessor messages,
			@Autowired PublicationService publicationService,
			@Autowired PublicationEditorFactory publicationEditorFactory,
			@Autowired PersonService personService,
			@Autowired PersonEditorFactory personEditorFactory,
			@Autowired UserService userService,
			@Autowired JournalService journalService,
			@Autowired JournalEditorFactory journalEditorFactory,
			@Autowired ConferenceService conferenceService,
			@Autowired ConferenceEditorFactory conferenceEditorFactory,
			@Autowired ScientificAxisService axisService) {
		super(fileManager, authenticatedUser, messages, publicationService, publicationEditorFactory, personService, personEditorFactory, userService,
				journalService, journalEditorFactory, conferenceService, conferenceEditorFactory,
				axisService, LOGGER);
	}

	@Override
	public String getPageTitle() {
		return getTranslation("views.scientific_culture_actions.list"); //$NON-NLS-1$
	}

}
