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

package fr.utbm.ciad.labmanager.views.appviews.persons;

import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.member.ChronoMembershipComparator;
import fr.utbm.ciad.labmanager.services.member.MembershipService;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.services.user.UserService;
import fr.utbm.ciad.labmanager.views.components.MainLayout;
import fr.utbm.ciad.labmanager.views.components.persons.AbstractPersonListView;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;

/** List all the persons.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Route(value = "persons", layout = MainLayout.class)
@PermitAll
public class PersonsView extends AbstractPersonListView implements HasDynamicTitle {

	private static final long serialVersionUID = 1616874715478139507L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PersonsView.class);

	/** Constructor.
	 *
	 * @param personService the service for accessing the person JPA.
	 * @param userService the service for accessing the user JPA.
	 * @param membershipService the service for accessing the membership JPA.
	 * @param membershipComparator the comparator that must be used for comparing the memberships. It is assumed that
	 *     the memberships are sorted in reverse chronological order first.
	 * @param messages the accessor to the localized messages (spring layer).
	 * @param authenticatedUser the connected user.
	 */
    public PersonsView(@Autowired PersonService personService, @Autowired UserService userService,
    		@Autowired MembershipService membershipService, @Autowired ChronoMembershipComparator membershipComparator,
    		@Autowired AuthenticatedUser authenticatedUser, @Autowired MessageSourceAccessor messages) {
    	super(personService, userService, membershipService, membershipComparator,
    			(ps, query, filters) -> ps.getAllPersons(query, filters),
    			authenticatedUser, messages, LOGGER);
    }

	@Override
	public String getPageTitle() {
		return getTranslation("views.persons.list_title.all"); //$NON-NLS-1$
	}

}
