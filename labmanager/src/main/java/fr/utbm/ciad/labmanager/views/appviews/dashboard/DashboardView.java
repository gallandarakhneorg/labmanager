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

package fr.utbm.ciad.labmanager.views.appviews.dashboard;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import fr.utbm.ciad.labmanager.data.publication.AuthorshipRepository;
import fr.utbm.ciad.labmanager.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.views.appviews.MainLayout;
import fr.utbm.ciad.labmanager.views.components.addons.logger.AbstractLoggerComposite;
import fr.utbm.ciad.labmanager.views.components.addons.logger.ContextualLoggerFactory;
import fr.utbm.ciad.labmanager.views.components.dashboard.Reporting;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

/** Dashboard for the lab manager application.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Route(value = "", layout = MainLayout.class)
@PermitAll
@Uses(Icon.class)
public class DashboardView extends AbstractLoggerComposite<VerticalLayout> implements HasDynamicTitle {

    private static final long serialVersionUID = -1583805930880620625L;


	private final PersonService personService;
	private final AuthorshipRepository authorshipRepository;
	private final AuthenticatedUser authenticatedUser;

	/** Constructor.
	 *
	 * @param loggerFactory the factory to be used for the composite logger.
	 */
	public DashboardView(@Autowired PersonService personService,
						 @Autowired AuthorshipRepository authorshipRepository,
						 @Autowired AuthenticatedUser authenticatedUser,
						 @Autowired ContextualLoggerFactory loggerFactory) {
		super(loggerFactory);
		this.personService = personService;
		this.authorshipRepository = authorshipRepository;
		this.authenticatedUser = authenticatedUser;


		getContent().setAlignItems(FlexComponent.Alignment.END);
		getContent().setFlexGrow(1.0);
		final var bt = new Button();
		bt.setText("Show log");
		bt.addClickListener(event -> {
			getLogger().info("Test logger / User name should appear");
		});
		getContent().add(bt);
		addComponent();
	}

	@Override
	public String getPageTitle() {
		return getTranslation("views.dashboard.title"); //$NON-NLS-1$
	}

	public void addComponent() {
		getContent().add(new Reporting(personService, authorshipRepository, authenticatedUser));
		HorizontalLayout layout = new HorizontalLayout();
		layout.add(new Button("Button 1"));
		layout.add(new Button("Button 2"));
		layout.setAlignItems(FlexComponent.Alignment.END);
		getContent().add(layout);
	}
}
