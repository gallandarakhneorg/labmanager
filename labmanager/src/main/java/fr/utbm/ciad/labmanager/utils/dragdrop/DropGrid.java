package fr.utbm.ciad.labmanager.utils.dragdrop;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

import java.util.ArrayList;
import java.util.List;

public class DropGrid extends FlexLayout {

    private static final int CELL_SIZE = 50;
    List<DropCell> cells = new ArrayList<>();

    private boolean isDragging = false; // State of the dragging operation

    /**
     * Constructor for DropGrid.
     */
    public DropGrid() {
        setFlexDirection(FlexDirection.ROW);
        setWidth("100%");
        getStyle().set("height", "100vh")
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fit, minmax(50px, 1fr))")
                .set("grid-auto-rows", CELL_SIZE + "px")
                .set("gap", "0px")
                .set("background-color", "rgba(255, 255, 255, 0.5)");

        createCells();
    }

    /**
     * Create the cells of the grid based on the grid size
     */
    private void createCells() {
        int cellCount = 600; // Static cell count for demonstration purposes

        for (int i = 0; i < cellCount; i++) {
            addCell(new DropCell(this, i));
        }
    }

    /**
     * Adds a cell to the grid and updates the internal list of cells.
     *
     * @param cell the DropCell instance to add to the grid layout.
     */
    private void addCell(DropCell cell) {
        add(cell);
        cells.add(cell);
    }

    /**
     * Sets the dragging state of the grid.
     *
     * @param dragging true to enable dragging, false to disable.
     */
    public void setDragging(boolean dragging) {
        this.isDragging = dragging;
    }

    /**
     * Displays the grid borders
     */
    public void showBorders() {
        getStyle().set("border", "1px dashed #bfbfbf");
        cells.forEach(cell -> {
            if (cell != null) {
                cell.showBorders();
            }
        });
    }

    /**
     * Hides the grid borders
     */
    public void hideBorders() {
        getStyle().set("border", "none");
        cells.forEach(cell -> {
            if (cell != null) {
                cell.hideBorders();
            }
        });
    }

    /**
     * Checks if the grid is empty.
     * The grid is considered empty if none of its cells contain components.
     *
     * @return true if the grid is empty, false otherwise.
     */
    public boolean isEmpty() {
        return getChildren()
                .filter(cell -> cell instanceof VerticalLayout)
                .allMatch(cell -> cell.getChildren().findAny().isEmpty());
    }

    /**
     * Finds the first empty cell in the grid.
     * This method identifies the first cell that does not currently contain any components.
     *
     * @return a DropCell representing the first empty cell, or null if no empty cells are found.
     */
    public DropCell findFirstEmptyCell() {
        for (DropCell cell : cells) {
            if (cell.isEmpty()) {
                return cell;
            }
        }
        return null;
    }

    /**
     * Adds a component to the first empty cell in the grid.
     *
     * @param component the component to be added to an empty cell.
     */
    public void addComponentInFirstEmptyCell(Component component) {
        DropCell cell = findFirstEmptyCell();
        if (cell != null) {
            cell.addComponent(component);
        }
    }

    /**
     * Retrieves a list of cells that contain at least one component.
     *
     * @return a list of DropCell objects containing components.
     */
    public List<DropCell> getCellsContainingComponents() {
        List<DropCell> cellsList = new ArrayList<>();
        for (DropCell cell : cells) {
            if (!cell.isEmpty()) {
                cellsList.add(cell);
            }
        }
        return cellsList;
    }

    /**
     * Toggles the edition mode
     */
    public void changeEditionMode() {
        getCellsContainingComponents().forEach(DropCell::changeEditionMode);
    }

    /**
     * Gets the current dragging state.
     *
     * @return true if dragging is active, false otherwise.
     */
    public boolean isDragging() {
        return isDragging;
    }
}

