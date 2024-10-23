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
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dnd.DropEvent;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import fr.utbm.ciad.labmanager.views.appviews.MainLayout;
import fr.utbm.ciad.labmanager.views.components.addons.logger.AbstractLoggerComposite;
import fr.utbm.ciad.labmanager.views.components.addons.logger.ContextualLoggerFactory;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.contextmenu.ContextMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Route(value = "", layout = MainLayout.class)
@PermitAll
@Uses(Icon.class)
public class DashboardView extends AbstractLoggerComposite<VerticalLayout> implements HasDynamicTitle {

	private static final long serialVersionUID = -1583805930880620625L;

	private final Select<String> componentSelect = new Select<>();
	private final FlexLayout gridLayout = new FlexLayout();

	private final List<String> availableComponents = new ArrayList<>(List.of("Button 1", "Label 1", "Button ABC"));

	/** Constructor for DashboardView.
	 *
	 * @param loggerFactory the factory to be used for the composite logger.
	 */
	public DashboardView(@Autowired ContextualLoggerFactory loggerFactory) {
		super(loggerFactory);
		configureSelect();
		configureGridLayout();
		createAndAddComponents();
		getContent().add(componentSelect, gridLayout, createShowLogButton());
	}

	/** Configures the Select component for component selection.
	 */
	private void configureSelect() {
		componentSelect.setLabel("Select Component");
		componentSelect.setItems(availableComponents);
		componentSelect.addValueChangeListener(event -> {
			String selectedItem = event.getValue();
			if (selectedItem != null) {
				Component component = createComponent(selectedItem);
				Optional<VerticalLayout> emptyCell = findFirstEmptyCell();
				emptyCell.ifPresent(cell -> {
					cell.add(component);
					makeComponentDraggable(component);
					availableComponents.remove(selectedItem);
					updateSelectItems();
				});
				componentSelect.clear();
			}
		});
	}

	/** Updates the Select items and enables/disables the Select based on the item count.
	 */
	private void updateSelectItems() {
		componentSelect.setItems(availableComponents);
		componentSelect.setEnabled(!availableComponents.isEmpty());
	}

	/** Configures the grid layout, defining the number of columns and rows.
	 */
	private void configureGridLayout() {
		gridLayout.setFlexDirection(FlexLayout.FlexDirection.ROW);
		gridLayout.setWidth("100%");
		int columns = 2;
		int rows = 3;
		gridLayout.getStyle()
				.set("height", "100vh")
				.set("display", "grid")
				.set("grid-template-columns", "repeat(" + columns + ", 1fr)")
				.set("grid-template-rows", "repeat(" + rows + ", 1fr)");

		for (int i = 0; i < columns * rows; i++) {
			gridLayout.add(createGridCell());
		}
	}

	/** Initializes and adds components to the Select.
	 *
	 */
	private void createAndAddComponents() {
		componentSelect.setItems(availableComponents);
		updateSelectItems();
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
	 *
	 * @param name The name of the component to create.
	 * @return The created component.
	 */
	private Component createComponent(String name) {
        return switch (name) {
            case "Button 1" -> createDraggableComponent(new Button("Button 1"));
            case "Label 1" -> createDraggableComponent(new Span("Label 1"));
            case "Button ABC" -> createDraggableComponent(new Button("Button ABC"));
            default -> null;
        };
	}

	/** Creates a draggable component and applies necessary configurations.
	 *
	 * @param component The component to be made draggable.
	 * @return The configured draggable component.
	 */
	private Component createDraggableComponent(Component component) {
		makeComponentDraggable(component);
		addContextMenuToComponent(component);
		addHoverEffectToComponent(component);
		return component;
	}

	/** Adds a context menu to a given component with options to delete it.
	 *
	 * @param component The component to which the context menu is added.
	 */
	private void addContextMenuToComponent(Component component) {
		ContextMenu contextMenu = new ContextMenu();
		contextMenu.setTarget(component);
		contextMenu.addItem("Delete", event -> removeComponent(component));
	}

	/** Removes a component from its parent layout.
	 *
	 * @param component The component to be removed.
	 */
	private void removeComponent(Component component) {
		component.getParent().ifPresent(parent -> {
			((HasComponents) parent).remove(component);
			String componentName = component.getElement().getText();
			availableComponents.add(componentName);
			updateSelectItems();
		});
	}

	/** Creates a cell in the grid layout.
	 *
	 * @return The created vertical layout cell.
	 */
	private VerticalLayout createGridCell() {
		VerticalLayout cell = new VerticalLayout();
		cell.setWidth("100%");
		cell.setHeight("50vh");
		cell.getStyle().set("border", "1px solid #9E9E9E");
		cell.getStyle().set("display", "flex");
		cell.getStyle().set("align-items", "center");
		cell.getStyle().set("justify-content", "center");

		DropTarget<VerticalLayout> dropTarget = DropTarget.create(cell);
		dropTarget.addDropListener(event -> handleDrop(event, cell));
		return cell;
	}

	/** Finds the first empty cell in the grid layout.
	 *
	 * @return An optional containing the first empty cell, if found.
	 */
	private Optional<VerticalLayout> findFirstEmptyCell() {
		return gridLayout.getChildren()
				.filter(cell -> cell instanceof VerticalLayout && cell.getChildren().findAny().isEmpty())
				.findFirst()
				.map(cell -> (VerticalLayout) cell);
	}

	/** Makes a component draggable.
	 *
	 * @param component The component to be made draggable.
	 */
	private void makeComponentDraggable(Component component) {
		DragSource.create(component).setDraggable(true);
	}

	/** Handles the drop event when a component is dropped into a grid cell.
	 *
	 * @param event The drop event containing information about the dragged component.
	 * @param cell The cell where the component is dropped.
	 */
	private void handleDrop(DropEvent<VerticalLayout> event, VerticalLayout cell) {
		event.getDragSourceComponent().ifPresent(draggedComponent -> {
			cell.getChildren().findAny().ifPresent(existingComponent -> {
				cell.remove(existingComponent);
				String componentName = existingComponent.getElement().getText();
				availableComponents.add(componentName);
				updateSelectItems();
			});

			draggedComponent.getParent().ifPresent(parent -> ((HasComponents) parent).remove(draggedComponent));
			cell.add(draggedComponent);
			makeComponentDraggable(draggedComponent);
		});
	}

	/** Adds a hover effect to a component to change its border on mouse events.
	 *
	 * @param component The component to which the hover effect is applied.
	 */
	private void addHoverEffectToComponent(Component component) {
		component.getElement().addEventListener("mouseover", event -> component.getStyle().set("border", "2px solid blue"));
		component.getElement().addEventListener("mouseout", event -> component.getStyle().set("border", "none"));
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
