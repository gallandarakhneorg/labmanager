package fr.utbm.ciad.labmanager.utils.dragdrop;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.dnd.DropEvent;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

import java.util.Optional;

public class DropGrid extends FlexLayout {

    private boolean isDragging = false; // State of the dragging operation

    /**
     * Constructor for DropGrid.
     * Initializes the grid layout by calling the configuration method.
     */
    public DropGrid() {
        configureGridLayout();
    }

    /**
     * Configures the grid layout for the DropGrid.
     * Sets the display properties, grid structure, and creates a number of grid cells.
     */
    private void configureGridLayout() {
        setFlexDirection(FlexDirection.ROW);
        setWidth("100%");
        getStyle().set("height", "100vh")
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fit, minmax(50px, 1fr))")
                .set("grid-auto-rows", "50px")
                .set("gap", "0px")
                .set("background-color", "rgba(255, 255, 255, 0.5)");

        int cellCount = 600;
        for (int i = 0; i < cellCount; i++) {
            add(createGridCell());
        }
    }

    /**
     * Creates a single grid cell.
     * Configures the properties of the cell and sets up drop target functionality.
     *
     * @return a VerticalLayout representing a single grid cell.
     */
    private VerticalLayout createGridCell() {
        VerticalLayout cell = new VerticalLayout();
        cell.setWidth("100%");
        cell.setHeight("100%");
        cell.getStyle().set("border", "1px solid transparent")
                .set("display", "flex")
                .set("align-items", "flex-start")
                .set("justify-content", "flex-start")
                .set("position", "relative");

        DropTarget<VerticalLayout> dropTarget = DropTarget.create(cell);
        dropTarget.addDropListener(event -> handleDrop(event, cell));

        // Event listener for when a draggable component enters the cell
        cell.getElement().addEventListener("dragenter", event -> {
            if (isDragging) {
                cell.getStyle().set("border", "2px solid blue");
            }
        });

        // Event listener for when a draggable component leaves the cell
        cell.getElement().addEventListener("dragleave", event -> {
            if (isDragging && cell.getChildren().findAny().isEmpty()) {
                cell.getStyle().set("border", "1px dashed #bfbfbf");
            }
        });

        return cell;
    }

    /**
     * Handles the drop event when a component is dropped into the cell.
     * Removes any existing component in the cell and adds the newly dropped component.
     *
     * @param event the drop event containing the dragged component.
     * @param cell the target cell where the component is dropped.
     */
    private void handleDrop(DropEvent<VerticalLayout> event, VerticalLayout cell) {
        event.getDragSourceComponent().ifPresent(draggedComponent -> {
            if (cell.getChildren().findAny().isEmpty()) {
                draggedComponent.getParent().ifPresent(parent -> {
                    if (parent instanceof VerticalLayout) {
                        ((HasComponents) parent).remove(draggedComponent);
                    }
                });
                System.out.println("DropGrid");
                System.out.print(draggedComponent);
                cell.add(draggedComponent);
            }
        });
    }

    /**
     * Sets the dragging state of the grid.
     * This method is used to enable or disable the dragging mode.
     *
     * @param dragging true to enable dragging, false to disable.
     */
    public void setDragging(boolean dragging) {
        this.isDragging = dragging;
    }

    /**
     * Finds the first empty cell in the grid.
     * This method is used to identify a cell that does not currently contain any components.
     *
     * @return an Optional containing the first empty cell if found, otherwise empty.
     */
    public Optional<VerticalLayout> findFirstEmptyCell() {
        return getChildren().filter(cell -> cell instanceof VerticalLayout && cell.getChildren().findAny().isEmpty())
                .findFirst()
                .map(cell -> (VerticalLayout) cell);
    }

    /**
     * Displays the grid borders when drag mode is active.
     * This method visually indicates that the grid is ready to receive a dragged component.
     */
    public void showGridBorders() {
        getStyle().set("border", "1px dashed #bfbfbf");
        getChildren().forEach(cell -> {
            if (cell instanceof VerticalLayout) {
                cell.getElement().getStyle().set("border", "1px dashed #bfbfbf");
            }
        });
    }

    /**
     * Hides the grid borders when drag mode is inactive.
     * This method resets the visual appearance of the grid after dragging is done.
     */
    public void hideGridBorders() {
        getStyle().set("border", "none");
        getChildren().forEach(cell -> {
            if (cell instanceof VerticalLayout) {
                cell.getElement().getStyle().set("border", "1px solid transparent");
            }
        });
    }
}
