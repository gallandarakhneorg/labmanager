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
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import fr.utbm.ciad.labmanager.views.appviews.MainLayout;
import fr.utbm.ciad.labmanager.views.components.addons.logger.AbstractLoggerComposite;
import fr.utbm.ciad.labmanager.views.components.addons.logger.ContextualLoggerFactory;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "", layout = MainLayout.class)
@PermitAll
@Uses(Icon.class)
public class DashboardView extends AbstractLoggerComposite<VerticalLayout> implements HasDynamicTitle {

	private static final long serialVersionUID = -1583805930880620625L;

	private VerticalLayout dropZone;

	/** Constructor.
	 *
	 * @param loggerFactory the factory to be used for the composite logger.
	 */
	public DashboardView(@Autowired ContextualLoggerFactory loggerFactory) {
		super(loggerFactory);

		// Creation of the dropZone
		dropZone = new VerticalLayout();
		dropZone.setWidth("100%");
		dropZone.getStyle().set("border", "2px dashed #FF0000");
		dropZone.getStyle().set("margin-bottom", "20px");
		dropZone.getStyle().set("text-align", "center");

		// Create a Flex layout for the grid
		FlexLayout gridLayout = new FlexLayout();
		gridLayout.setFlexDirection(FlexLayout.FlexDirection.COLUMN);
		gridLayout.setWidth("100%");
		gridLayout.setHeight("100%");
		gridLayout.getStyle().set("display", "grid");
		gridLayout.getStyle().set("grid-template-columns", "repeat(4, 1fr)");
		gridLayout.getStyle().set("grid-template-rows", "repeat(4, 1fr)");
		gridLayout.getStyle().set("gap", "10px");

		// Create and add components to the drop zone
		createAndAddComponents();

		// Add drop zone to the layout
		getContent().add(dropZone);

		// Add grid cells that accept drops
		for (int i = 0; i < 16; i++) { // 4x4 Grid
			gridLayout.add(createGridCell());
		}

		// Add grid to the layout
		getContent().add(gridLayout);

		final var bt = new Button();
		bt.setText("Show log");
		bt.addClickListener(event -> {
			getLogger().info("Test logger / User name should appear");
		});
		getContent().add(bt);
	}

	// Helper to create a draggable component
	private Component createDraggableComponent(Component component) {
		makeComponentDraggable(component); // Make the component draggable initially
		addContextMenuToComponent(component);
		return component;
	}

	// Make a component draggable
	/** Adds a context menu to a given component with options to delete or store it.
	 *
	 * @param component The component to which the context menu is added.
	 */
	private void addContextMenuToComponent(Component component) {
		ContextMenu contextMenu = new ContextMenu();
		contextMenu.setTarget(component);
		contextMenu.addItem("Delete", event -> removeComponent(component));
		contextMenu.addItem("Store", event -> storeComponent(component));
	}

	/** Removes a component from its parent.
	 *
	 * @param component The component to be removed.
	 */
	private void removeComponent(Component component) {
		component.getParent().ifPresent(parent -> ((HasComponents) parent).remove(component));
	}

	/** Stores a component in the drop zone as a label.
	 *
	 * @param component The component to be stored.
	 */
	private void storeComponent(Component component) {
		removeComponent(component);
		dropZone.add(createLabelForComponent(component));
		makeComponentDraggable(component);
	}
	private void makeComponentDraggable(Component component) {
		DragSource<Component> dragSource = DragSource.create(component);
		dragSource.setDraggable(true);
	}

	// Example of creating various components
	private void createAndAddComponents() {
		Button button1 = (Button) createDraggableComponent(new Button("Button 1"));
		Span label1 = (Span) createDraggableComponent(new Span("Label 1"));
		Button button2 = (Button) createDraggableComponent(new Button("Button ABC"));

		// Add components to the drop zone
		dropZone.add(button1, label1, button2);
	}

	// Helper to create grid cells that accept drops
	private VerticalLayout createGridCell() {
		VerticalLayout cell = new VerticalLayout();
		cell.setWidth("100%");
		cell.getStyle().set("border", "1px solid #9E9E9E");
		cell.getStyle().set("min-height", "100px");
		cell.getStyle().set("display", "flex");
		cell.getStyle().set("align-items", "center");
		cell.getStyle().set("justify-content", "center");

		// Make the cell a drop target
		DropTarget<VerticalLayout> dropTarget = DropTarget.create(cell);
		dropTarget.addDropListener(event -> {
			event.getDragSourceComponent().ifPresent(draggedComponent -> {
				// Check if the cell already contains a component
				if (cell.getChildren().findAny().isPresent()) {
					// If yes, move this component back to the dropZone
					Component existingComponent = cell.getChildren().findFirst().get();
					cell.remove(existingComponent);
					dropZone.add(existingComponent);
					makeComponentDraggable(existingComponent);
					getLogger().info("Existing component moved back to the drop zone.");
				}

				// Remove the dragged component from its current parent
				if (draggedComponent.getParent().isPresent()) {
					((HasComponents) draggedComponent.getParent().get()).remove(draggedComponent);
				}

				// Add the new component to the cell
				cell.add(draggedComponent);
				updateDropZoneSize();
				makeComponentDraggable(draggedComponent);
				getLogger().info("Component dropped in the grid cell.");
			});
		});
		return cell;
	}

	private void updateDropZoneSize() {
		dropZone.setHeight(null); // Reset height to auto
		dropZone.getChildren().forEach(component -> {
			if (component instanceof Component) {
				dropZone.setHeight("auto"); // Set height to auto to adjust based on content
			}
		});
	}

	@Override
	public String getPageTitle() {
		return getTranslation("views.dashboard.title"); //$NON-NLS-1$
	}
}

