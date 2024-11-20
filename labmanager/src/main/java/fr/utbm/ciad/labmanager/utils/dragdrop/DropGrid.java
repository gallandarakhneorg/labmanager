package fr.utbm.ciad.labmanager.utils.dragdrop;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dnd.DropEvent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;

import java.util.ArrayList;
import java.util.List;

public class DropGrid extends FlexLayout {

    private final int numRows;
    private final int numCols;
    private int cellSize;
    List<DropCell> cells = new ArrayList<>();
    private DraggableComponent draggedComponent;
    private final String borderColor;
    private final String borderColorWhenDrag;
    private final String backgroundColorWhenEmpty;
    private final String backgroundColorWhenFull;

    /**
     * Default constructor for DropGrid.
     */
    public DropGrid() {
        this(30, 30, "#bfbfbf", "#f2f2f2", "#d2ffc4", "#ffc4c4");
    }

    /**
     * Constructor for DropGrid.
     */
    public DropGrid(int rows, int columns,
                    String borderColor,
                    String borderColorWhenDrag,
                    String backgroundColorWhenEmpty,
                    String backgroundColorWhenFull) {
        this.borderColorWhenDrag = borderColor;
        this.borderColor = borderColorWhenDrag;
        this.backgroundColorWhenEmpty = backgroundColorWhenEmpty;
        this.backgroundColorWhenFull = backgroundColorWhenFull;

        this.numRows = rows;
        this.numCols = columns;
        setFlexDirection(FlexDirection.ROW);
        UI.getCurrent().getPage().executeJs("return window.innerWidth;")
                .then(width -> {
                    getStyle()
                            .setWidth(width.asNumber() - 350 + "px")
                            .set("display", "grid")
                            .set("grid-template-columns", "repeat(" + columns + ", 1fr)")
                            .set("grid-template-rows", "repeat(" + rows + ", 1fr)")
                            .set("gap", "0px")
                            .set("background-color", "rgba(255, 255, 255, 0.5)");
                });


        UI.getCurrent().getPage().addBrowserWindowResizeListener(event -> {
            setGridSize(event.getWidth());
        });

        createCells();
    }

    /**
     * Creates the grid cells based on the number of rows and columns.
     */
    private void createCells() {
        int cellCount = numRows * numCols;
        cells.clear();
        removeAll();

        for (int i = 0; i < cellCount; i++) {
            DropCell cell = new DropCell(i, borderColor, borderColorWhenDrag, backgroundColorWhenEmpty, backgroundColorWhenFull);

            cell.getDropTarget().addDropListener(event -> handleDrop(event, cell));

            UI.getCurrent().getPage().executeJs("return window.innerWidth;")
                    .then(width -> {
                        cell.getStyle()
                                .set("box-sizing", "border-box")
                                .setMargin("0")
                                .setPadding("0")
                                .setDisplay(Style.Display.FLEX);
                        setGridSize((int) Math.round(width.asNumber()));
                        addCell(cell);
                    });
        }
    }

    /**
     * Handles the drop event when a component is dropped into the cell.
     *
     * @param event the drop event containing the dragged component.
     * @param cell the target DropCell where the component is dropped.
     */
    private void handleDrop(DropEvent<VerticalLayout> event, DropCell cell) {
        event.getDragSourceComponent().ifPresent(draggedComponent -> {

            List<DropCell> targetCellList = getCoveredCells(cell);

            if (canBePlaced((DraggableComponent) draggedComponent, cell)) {
                cell.addComponent(draggedComponent);
                draggedComponent.getParent().ifPresent(parent -> {
                    if (parent instanceof DropCell) {
                        ((DropCell) parent).emptyCell();
                    }
                });
                for (DropCell dropCell : targetCellList) {
                    dropCell.setEmpty(false);
                }
            } else {
                draggedComponent.getParent().ifPresent(parent -> {
                    for (DropCell dropCell : getCoveredCells((DropCell) parent)) {
                        dropCell.setEmpty(false);
                    }
                });
            }
        });
    }

    /**
     * Adds a new cell to the grid and updates the internal list of cells.
     *
     * @param cell the DropCell instance to be added to the grid.
     */
    private void addCell(DropCell cell) {

        cell.getElement().addEventListener("dragenter", event -> {
            event.getSource().getComponent().ifPresent(component -> {
                int componentWidthInCells = calculateComponentSizeInCells(draggedComponent, "width");
                boolean overTheEdge = isOverTheEdge(cell, componentWidthInCells);
                List<DropCell> coveredCells = getCoveredCells(cell, draggedComponent);

                for (DropCell dropCell : cells) {
                    dropCell.colorCell(coveredCells.contains(dropCell), overTheEdge);
                }
            });
        });

        add(cell);
        cells.add(cell);
    }

    /**
     * Displays the grid borders depending on the event calling the method (triggering edit mode or dragging a component)
     *
     * @param dragging true if the event is dragging, false otherwise
     */
    public void showBorders(boolean dragging) {
        if (dragging) {
            changeToBorderColorWhenDrag();
        } else {
            changeToBorderColor();
        }
        cells.forEach(cell -> {
            if (cell != null) {
                cell.showBorders(dragging);
            }
        });
    }

    /**
     * Hide the cell borders depending on the event calling the method (triggering edit mode or dragging a component)
     *
     * @param dragging true if the event is dragging, false otherwise
     */
    public void hideBorders(boolean dragging) {
        if (dragging) {
            changeToBorderColor();
        } else {
            getStyle().set("border", "none");
        }
        cells.forEach(cell -> {
            if (cell != null) {
                cell.hideBorders(dragging);
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
        return cells.stream().allMatch(DropCell::isEmpty);
    }

    /**
     * Finds the first available location for a component in the grid.
     * This method searches for the first empty cell that can accommodate the component,
     * ensuring that the cell has sufficient space to fit the component's size.
     *
     * @param component the component to be placed in the grid.
     * @return a DropCell representing the first empty cell that can contain the component,
     *         or null if no suitable empty cells are available.
     */
    public DropCell findFirstEmptyPlace(DraggableComponent component) {
        for (DropCell cell : cells) {
            if (canBePlaced(component, cell)) {
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
    public void addComponentInFirstEmptyCell(DraggableComponent component) {

        addDragStartListener(component);
        addDragEndListener(component);
        addResizeListener(component);

        DropCell cell = findFirstEmptyPlace(component);
        if (cell != null) {
            addComponent(cell, component);
        }
    }

    /**
     * Adds a component to the specified cell and marks the covered cells as not empty.
     *
     * @param cell the target DropCell where the component should be added.
     * @param component the component to be added to the cell.
     */
    private void addComponent(DropCell cell, DraggableComponent component) {
        cell.addComponent(component);
        for (DropCell dropCell : getCoveredCells(cell)) {
            dropCell.setEmpty(false);
        }
    }

    /**
     * Adds a resize listener to the component that dispatches a custom resize event
     * whenever the size of the component changes.
     */
    private void addResizeListener(DraggableComponent component) {
        String jsCode =
                "const resizeObserver = new ResizeObserver(entries => {" +
                        "   for (let entry of entries) {" +
                        "       this.dispatchEvent(new Event('custom-resize')); " +
                        "   }" +
                        "});" +
                        "resizeObserver.observe(this);";

        component.getElement().executeJs(jsCode);

        component.getElement().addEventListener("custom-resize", event -> adaptComponentSize(component));
    }

    /**
     * Adjusts the size of a component when its size changes.
     * This method ensures the component fits within the cells in the grid.
     *
     * @param component the DraggableComponent to adapt.
     */
    private void adaptComponentSize(DraggableComponent component) {
        component.getParent().ifPresent(parent -> {
            /*DropCell cell = (DropCell) parent;
            String startWidth = component.getComponent().getStyle().get("width");
            String startHeight = component.getComponent().getStyle().get("height");

            for (DropCell dropCell : getCoveredCells(cell, component)) {
                dropCell.setEmpty(true);
            }*/
            component.adaptComponentSize(() -> {

                /*if(!canBePlaced(component, cell)){
                    component.getStyle().setWidth(startWidth).setHeight(startHeight);
                    component.adaptComponentSize();
                }
                for (DropCell dropCell : getCoveredCells(cell, component)) {
                    dropCell.setEmpty(false);
                }*/
            });

        });
    }

    /**
     * Adds a drag start listener to the component, which triggers the showBorders method.
     *
     * @param component the DraggableComponent to attach the listener to.
     */
    private void addDragStartListener(DraggableComponent component) {
        component.getDragSource().addDragStartListener(event -> {
            draggedComponent = component;

            showBorders(true);
            for (DropCell dropCell : cells) {
                dropCell.getChild().ifPresent(cellComponent -> cellComponent.getStyle().setOpacity("0.5").setZIndex(0));
            }
            draggedComponent.getParent().ifPresent(sourceCell -> {
                if (sourceCell instanceof DropCell) {
                    for (DropCell dropCell : getCoveredCells((DropCell) sourceCell)) {
                        dropCell.setEmpty(true);
                    }
                }
            });
        });
    }

    /**
     * Adds a drag end listener to the component, which triggers the hideBorders method.
     *
     * @param component the DraggableComponent to attach the listener to.
     */
    private void addDragEndListener(DraggableComponent component) {
        component.getDragSource().addDragEndListener(event -> {
            hideBorders(true);

            for (DropCell dropCell : cells) {
                dropCell.colorCell(false, true);
                dropCell.getChild().ifPresent(cellComponent -> cellComponent.getStyle().setOpacity("1").setZIndex(2));
            }
        });
        draggedComponent = null;
    }

    /**
     * Checks whether a component can be placed in a specific cell.
     *
     * @param component the component to be placed.
     * @param cell the DropCell where the component will be placed.
     * @return true if the component can be placed, false otherwise.
     */
    private boolean canBePlaced(DraggableComponent component, DropCell cell) {
        return !goOverTheEdge(component, cell) &&
                getCoveredCells(cell, component)
                        .stream()
                        .allMatch(DropCell::isEmpty);
    }


    private boolean goOverTheEdge(DraggableComponent component, DropCell cell) {
        return numCols - cell.getIndex() % numCols < calculateComponentSizeInCells(component, "width");
    }

    /**
     * Checks if a component, based on its width (column span), would overflow
     * beyond the right edge of the grid when placed starting from a specific cell.
     *
     * @param startingCell the cell where the component is intended to start.
     * @param colSpan the number of columns the component spans horizontally.
     * @return true if the component would overflow the grid's right edge, false otherwise.
     */
    private boolean isOverTheEdge(DropCell startingCell, int colSpan) {
        int startingColumn = startingCell.getIndex() % numCols;
        return startingColumn + colSpan > numCols;
    }

    /**
     * Retrieves a list of cells that contain at least one component.
     *
     * @return a list of DropCell objects containing components.
     */
    public List<DropCell> getCellsContainingComponents() {
        List<DropCell> cellsList = new ArrayList<>();
        for (DropCell cell : cells) {
            if (cell.containsComponent()) {
                cellsList.add(cell);
            }
        }
        return cellsList;
    }

    /**
     * Toggles the edition mode
     *
     * @param editionMode the new edition mode
     */
    public void changeEditionMode(boolean editionMode) {
        if (editionMode) {
            showBorders(false);
        } else {
            hideBorders(false);
        }
        getCellsContainingComponents().forEach(cell -> {
            cell.getChild().ifPresent(component -> {
                cell.emptyCell();
                if (component instanceof DraggableComponent) {
                    hideBorders(false);
                    Component newComponent = ((DraggableComponent) component).getComponent();
                    cell.addComponent(newComponent);
                } else {
                    showBorders(false);
                    DraggableComponent newComponent = new DraggableComponent(component);
                    addDragStartListener(newComponent);
                    addDragEndListener(newComponent);
                    addResizeListener(newComponent);
                    cell.addComponent(newComponent);
                }
            });
        });
    }

    /**
     * Retrieves all cells that are covered by the currently dragged component.
     *
     * @param startingCell the starting DropCell to check for covered cells.
     * @return a list of DropCells that are covered by the currently dragged component.
     */
    private List<DropCell> getCoveredCells(DropCell startingCell) {
        return getCoveredCells(startingCell, draggedComponent);
    }

    /**
     * Retrieves all cells that are covered by a given component.
     *
     * @param startingCell the starting DropCell to check for covered cells.
     * @param component the DraggableComponent being placed.
     * @return a list of DropCells that are covered by the given component.
     */
    private List<DropCell> getCoveredCells(DropCell startingCell, DraggableComponent component) {
        int componentWidthInCells = calculateComponentSizeInCells(component, "width");
        int componentHeightInCells = calculateComponentSizeInCells(component, "height");

        int startCellIndex = startingCell.getIndex();
        int startingColumn = startCellIndex % numCols;

        List<DropCell> coveredCells = new ArrayList<>();
        for (int row = 0; row < componentHeightInCells; row++) {
            for (int col = 0; col < componentWidthInCells; col++) {
                int cellIndex = startCellIndex + row * numCols + col;

                if ((startingColumn + col) < numCols && cellIndex < cells.size()) {
                    coveredCells.add(cells.get(cellIndex));
                }
            }
        }
        return coveredCells;
    }

    /**
     * Calculates the size of a component in cells.
     *
     * @param component the DraggableComponent.
     * @param side the side ("width" or "height") for the calculation.
     * @return the size of the component in cells.
     */
    private int calculateComponentSizeInCells(DraggableComponent component, String side) {
        if (component != null) {
            int componentSide = Integer.parseInt(component.getComponent().getStyle().get(side).replace("px", ""));
            return Math.round((float) componentSide / cellSize);
        }
        return 0;
    }

    /**
     * Sets the size of the grid cells based on the available width of the container.
     *
     * @param windowWidth the available width for the grid.
     */
    public void setGridSize(int windowWidth) {
        int header = 50;
        if (windowWidth > 800) {
            header = 350;
        }
        int width = windowWidth - header;
        getStyle().setWidth(width + "px");

        cellSize = width / numCols;

        for (DropCell cell : cells) {

            cell.resizeComponent(cellSize);
            cell.getStyle()
                    .setWidth(cellSize + "px")
                    .setHeight(cellSize + "px");
        }
    }

    /**
     * Changes the border color of the grid to its default color.
     */
    private void changeToBorderColor() {
        getStyle().set("border", "1px dashed " + borderColor);
    }

    /**
     * Changes the border color of the grid to the color when a drag event is active.
     */
    private void changeToBorderColorWhenDrag() {
        getStyle().set("border", "1px dashed " + borderColorWhenDrag);
    }

    /**
     * Removes a component from the specified cell and marks all covered cells as empty.
     * This method empties the given cell and updates any cells that the component
     * spans over to ensure they are marked as empty.
     *
     * @param cell the DropCell from which the component is being removed.
     * @param component the DraggableComponent that is being removed from the grid.
     */
    public void removeComponent(DropCell cell, DraggableComponent component) {
        cell.emptyCell();
        for (DropCell dropCell : getCoveredCells(cell, component)) {
            dropCell.setEmpty(true);
        }
    }

}

