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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import fr.utbm.ciad.labmanager.views.appviews.MainLayout;
import fr.utbm.ciad.labmanager.views.components.addons.logger.AbstractLoggerComposite;
import fr.utbm.ciad.labmanager.views.components.addons.logger.ContextualLoggerFactory;
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryBarChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.layout.PublicationCategoryLayout;
import fr.utbm.ciad.labmanager.views.components.charts.publicationcategory.PublicationCategoryBarChart;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import fr.utbm.ciad.labmanager.utils.dragdrop.DraggableComponent;
import fr.utbm.ciad.labmanager.utils.dragdrop.DropGrid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Route(value = "", layout = MainLayout.class)
@PermitAll
@Uses(Icon.class)
public class DashboardView extends AbstractLoggerComposite<VerticalLayout> implements HasDynamicTitle {

	private static final long serialVersionUID = -1583805930880620625L;

	private final Select<String> componentSelect = new Select<>();
	private final DropGrid dropGrid = new DropGrid();

	private final List<String> availableComponents = new ArrayList<>(List.of("Button 1", "Label 1", "Button ABC", "Horizontal Layout", "Graph"));

	private final PublicationCategoryChartFactory<PublicationCategoryBarChart> barChartFactory;

	/** Constructor for DashboardView.
	 *
	 * @param loggerFactory the factory to be used for the composite logger.
	 * @param publicationService the service used to manage publications.
	 */
	public DashboardView(@Autowired ContextualLoggerFactory loggerFactory, @Autowired PublicationService publicationService) {
		super(loggerFactory);
		barChartFactory = new PublicationCategoryBarChartFactory();

		configureSelect(publicationService);

		componentSelect.setItems(availableComponents);
		updateSelectItems();

		getContent().add(componentSelect, dropGrid, createShowLogButton());
	}

	/** Configures the Select component for component selection.
	 * Sets the label and items for the selection dropdown and
	 * defines the behavior when a component is selected.
	 *
	 * @param publicationService the service used to manage publications.
	 */
	private void configureSelect(PublicationService publicationService) {
		componentSelect.setLabel("Select Component");
		componentSelect.setItems(availableComponents);
		componentSelect.addValueChangeListener(event -> {
			String selectedItem = event.getValue();
			if (selectedItem != null) {
				Component component = createComponent(publicationService, selectedItem);
				assert component != null;
				component.getStyle().set("z-index", "1000");
				Optional<VerticalLayout> emptyCell = dropGrid.findFirstEmptyCell();
				emptyCell.ifPresent(cell -> {
					cell.add(component);
					new DraggableComponent(component, dropGrid);
					availableComponents.remove(selectedItem);
					updateSelectItems();
				});
				componentSelect.clear();
			}
		});
	}

	/** Updates the Select items and enables/disables the Select
	 * based on the current count of available components.
	 * This method ensures that the dropdown reflects the
	 * current state of available components.
	 */
	private void updateSelectItems() {
		componentSelect.setItems(availableComponents);
		componentSelect.setEnabled(!availableComponents.isEmpty());
	}

	/** Creates a button to show logs and adds a click listener to log a message.
	 *
	 * @return the button for showing logs.
	 */
	private Button createShowLogButton() {
		Button button = new Button("Show log");
		button.addClickListener(event -> getLogger().info("Test logger / User name should appear"));
		return button;
	}

	/** Creates a component based on the selected name.
	 * This method instantiates different types of components
	 * based on the user's selection from the dropdown.
	 *
	 * @param name The name of the component to create.
	 * @return The created component.
	 */
	private Component createComponent(PublicationService publicationService, String name) {
		return switch (name) {
			case "Button 1" -> {
				Button button = new Button("Button 1");
				button.setWidth("40vh");
				button.setHeight("20vh");
				yield button;
			}
			case "Label 1" -> new Span("Label 1");
			case "Button ABC" -> new Button("Button ABC");
			case "Horizontal Layout" -> {
				HorizontalLayout horizontalLayout = new HorizontalLayout();
				Button innerButton = new Button("Inner Button");
				horizontalLayout.add(innerButton);
				yield horizontalLayout;
			}
			case "Graph" -> {
				PublicationCategoryLayout<PublicationCategoryBarChart> barChart = new PublicationCategoryLayout<>(publicationService, barChartFactory);
				barChart.setWidth("70vh");
				barChart.setHeight("50vh");
				yield new Div(barChart);
			}
			default -> null;
		};
	}

	/** Returns the title of the page for the dashboard view.
	 *
	 * @return The page title.
	 */
	@Override
	public String getPageTitle() {
		return getTranslation("views.dashboard.title");
	}
}