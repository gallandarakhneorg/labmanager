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

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryNightingaleRoseChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryPieChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.layout.PublicationCategoryLayout;
import fr.utbm.ciad.labmanager.views.components.charts.publicationcategory.PublicationCategoryBarChart;
import fr.utbm.ciad.labmanager.views.components.charts.publicationcategory.PublicationCategoryNightingaleRoseChart;
import fr.utbm.ciad.labmanager.views.components.charts.publicationcategory.PublicationCategoryPieChart;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import fr.utbm.ciad.labmanager.utils.dragdrop.DraggableComponent;
import fr.utbm.ciad.labmanager.utils.dragdrop.DropGrid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import org.jetbrains.annotations.NotNull;

@Route(value = "", layout = MainLayout.class)
@PermitAll
@Uses(Icon.class)
public class DashboardView extends AbstractLoggerComposite<VerticalLayout> implements HasDynamicTitle {

	private static final long serialVersionUID = -1583805930880620625L;

	private boolean editionMode = false;
	private final Select<String> componentSelect = new Select<>();
	private final Button editButton = createButton("Edit", event -> editModeButtonPress());
	private final DropGrid dropGrid = new DropGrid();

	private final List<String> availableComponents = new ArrayList<>(List.of("Button 1", "Horizontal Layout", "BarChart","PieChart","NightingaleRoseChart"));

	private final PublicationCategoryChartFactory<PublicationCategoryBarChart> barChartFactory;
	private final PublicationCategoryChartFactory<PublicationCategoryPieChart> pieChartFactory;
	private final PublicationCategoryChartFactory<PublicationCategoryNightingaleRoseChart> nightingaleChartFactory;

	/**
	 * Constructor for DashboardView.
	 *
	 * @param loggerFactory the factory to be used for the composite logger.
	 * @param publicationService the service used to manage publications.
	 */
	public DashboardView(@Autowired ContextualLoggerFactory loggerFactory, @Autowired PublicationService publicationService) {
		super(loggerFactory);
		barChartFactory = new PublicationCategoryBarChartFactory();
		pieChartFactory = new PublicationCategoryPieChartFactory();
		nightingaleChartFactory = new PublicationCategoryNightingaleRoseChartFactory();

		updateSelectItems();
		Button showLogButton = createButton("Show Log", event -> getLogger().info("Test logger / User name should appear"));

		createSelect(publicationService);

		getContent().add(createHeader(), dropGrid, showLogButton);
	}

	/**
	 * Creates and configures the header layout for the dashboard.
	 * Sets up layout style and alignment, and adds UI components
	 *
	 * @return HorizontalLayout the configured header layout.
	 */
	private HorizontalLayout createHeader(){
		HorizontalLayout header = new HorizontalLayout();
		header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
		header.getStyle().set("width", "100%");

		editButton.getStyle().set("width", "20vh");
		if (dropGrid.isEmpty()) {
			editButton.setEnabled(false);
		}

		header.add(componentSelect, editButton);
		return header;
	}

	/**
	 * Create the Select component used for selecting components.
	 * Sets the label, populates items, and defines behavior on component selection.
	 *
	 * @param publicationService the service used to manage publications.
	 */
	private void createSelect(PublicationService publicationService) {
		componentSelect.setLabel("Select Component");
		componentSelect.setItems(availableComponents);

		componentSelect.addValueChangeListener(event -> {
			String selectedItem = event.getValue();
			if (selectedItem != null) {
				editButton.setEnabled(true);
				this.setEditionMode(true);

				DraggableComponent draggableComponent = getDraggableComponent(publicationService, selectedItem);
				addContextMenuToComponent(draggableComponent);

				draggableComponent.getElement().setAttribute("data-custom-id", selectedItem);

				dropGrid.addComponentInFirstEmptyCell(draggableComponent);

				availableComponents.remove(selectedItem);
				updateSelectItems();
				componentSelect.clear();
			}
		});
	}

	/**
	 * Creates and returns a draggable UI component based on the selected item.
	 *
	 * @param publicationService the service for accessing publication data.
	 * @param selectedItem the type of component to create.
	 * @return DraggableComponent a draggable component based on the selected item.
	 */
	@NotNull
	private DraggableComponent getDraggableComponent(PublicationService publicationService, String selectedItem) {
		Component component = switch (selectedItem) {
			case "Button 1" -> {
				Button button = new Button("Button 1");
				button.setWidth("40vh");
				button.setHeight("20vh");
				yield button;
			}
			case "Horizontal Layout" -> {
				HorizontalLayout horizontalLayout = new HorizontalLayout();
				Button innerButton = new Button("Inner Button");
				horizontalLayout.add(innerButton);
				yield horizontalLayout;
			}
			case "BarChart" -> {
				PublicationCategoryLayout<PublicationCategoryBarChart> barChart = new PublicationCategoryLayout<>(publicationService, barChartFactory);
				barChart.setWidth("70vh");
				barChart.setHeight("50vh");
				yield new Div(barChart);
			}
			case "PieChart" -> {
				PublicationCategoryLayout<PublicationCategoryPieChart> pieChart = new PublicationCategoryLayout<>(publicationService, pieChartFactory);
				pieChart.setWidth("70vh");
				pieChart.setHeight("50vh");
				yield new Div(pieChart);
			}
			case "NightingaleRoseChart" -> {
				PublicationCategoryLayout<PublicationCategoryNightingaleRoseChart> nightingaleRoseChart = new PublicationCategoryLayout<>(publicationService, nightingaleChartFactory);
				nightingaleRoseChart.setWidth("70vh");
				nightingaleRoseChart.setHeight("50vh");
				yield new Div(nightingaleRoseChart);
			}
			default -> null;
		};
		return new DraggableComponent(Objects.requireNonNullElseGet(component, Div::new), dropGrid);
	}

	/**
	 * Updates the items in the component select dropdown.
	 * Enables or disables the dropdown based on the availability of components.
	 */
	private void updateSelectItems() {
		componentSelect.setItems(availableComponents);
		componentSelect.setEnabled(!availableComponents.isEmpty());
	}

	/**
	 * Toggles the editing mode
	 */
	private void editModeButtonPress() {
		setEditionMode(!editionMode);
	}

	/**
	 * Sets the editing mode and updates the UI to reflect the change.
	 *
	 * @param editionMode the new editing mode state.
	 */
	private void setEditionMode(boolean editionMode) {
		if (this.editionMode != editionMode) {
			this.editionMode = editionMode;
			dropGrid.changeEditionMode();
		}
		if (editionMode) {
			dropGrid.getCellsContainingComponents().forEach(cell -> cell.getChild().ifPresent(this::addContextMenuToComponent));
			editButton.setText("Stop Editing");
		} else {
			editButton.setText("Edit");
		}
	}

	/**
	 * Creates a button with a specified name and action.
	 *
	 * @param name the name displayed on the button.
	 * @param clickAction the action executed on button click.
	 * @return Button the created Button component.
	 */
	private Button createButton(String name, ComponentEventListener<ClickEvent<Button>> clickAction) {
		Button button = new Button(name);
		button.addClickListener(clickAction);
		return button;
	}

	/**
	 * Adds a context menu to a component to deletion option.
	 *
	 * @param component The component to which the context menu is added.
	 */
	private void addContextMenuToComponent(Component component) {
		ContextMenu contextMenu = new ContextMenu();
		contextMenu.setTarget(component);
		contextMenu.addItem("Delete", event -> removeComponent(component));
	}

	/**
	 * Removes a specified component from the parent layout and updates the dropdown.
	 *
	 * @param component The component to be removed.
	 */
	private void removeComponent(Component component) {
		component.getParent().ifPresent(parent -> {
			((HasComponents) parent).remove(component);
			String componentId = component.getElement().getAttribute("data-custom-id");
			availableComponents.add(Objects.requireNonNullElse(componentId, "Unknown Component"));
			if (dropGrid.isEmpty()) {
				editButton.setEnabled(false);
			}
			updateSelectItems();
		});
	}

	/**
	 * Returns the page title for the dashboard view.
	 *
	 * @return String the page title.
	 */
	@Override
	public String getPageTitle() {
		return getTranslation("views.dashboard.title"); //$NON-NLS-1$
	}
}
