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

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
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

/** Dashboard with drag-and-drop grid for the lab manager application.
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

	/** Constructor.
	 *
	 * @param loggerFactory the factory to be used for the composite logger.
	 */
	public DashboardView(@Autowired ContextualLoggerFactory loggerFactory) {
		super(loggerFactory);

		// Create a Flex layout for the grid
		FlexLayout gridLayout = new FlexLayout();
		gridLayout.setFlexDirection(FlexLayout.FlexDirection.COLUMN);
		gridLayout.setWidth("100%");
		gridLayout.setHeight("100%");
		gridLayout.getStyle().set("display", "grid");
		gridLayout.getStyle().set("grid-template-columns", "repeat(4, 1fr)"); // 4 columns
		gridLayout.getStyle().set("grid-template-rows", "repeat(4, 1fr)"); // 4 rows
		gridLayout.getStyle().set("gap", "10px"); // Gap between grid items

		// Create draggable buttons
		Button button1 = createDraggableButton("Button 1");
		Button button2 = createDraggableButton("Button 2");
		Button button3 = createDraggableButton("Button 3");

		// Add grid cells that accept drops
		for (int i = 0; i < 16; i++) { // 4x4 grid
			gridLayout.add(createGridCell());
		}

		// Add the draggable buttons initially outside the grid
		getContent().add(button1, button2, button3);
		getContent().add(gridLayout);
	}

	// Helper to create draggable buttons
	private Button createDraggableButton(String label) {
		Button button = new Button(label);
		DragSource<Button> dragSource = DragSource.create(button);
		dragSource.setDraggable(true); // Make the button draggable
		return button;
	}

	// Helper to create grid cells that accept drop
	private VerticalLayout createGridCell() {
		VerticalLayout cell = new VerticalLayout();
		cell.setWidth("100%");
		cell.setHeight("100%");
		cell.getStyle().set("border", "1px solid #9E9E9E");
		cell.getStyle().set("min-height", "100px");
		cell.getStyle().set("display", "flex");
		cell.getStyle().set("align-items", "center");
		cell.getStyle().set("justify-content", "center");

		// Make cell a drop target
		DropTarget<VerticalLayout> dropTarget = DropTarget.create(cell);
		dropTarget.addDropListener(event -> {
			event.getDragSourceComponent().ifPresent(draggedComponent -> {
				// Add the dragged component into the cell if it has no children
				if (cell.getChildren().count() == 0) {
					cell.add(draggedComponent);
					getLogger().info("Component dropped in grid cell.");
				}
			});
		});
		return cell;
	}

	@Override
	public String getPageTitle() {
		return getTranslation("views.dashboard.title"); //$NON-NLS-1$
	}
}