package fr.utbm.ciad.labmanager.utils.grid;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dnd.DropEvent;

import elemental.json.JsonValue;

import fr.utbm.ciad.labmanager.utils.container.ComponentContainer;
import fr.utbm.ciad.labmanager.utils.container.DraggableComponent;
import fr.utbm.ciad.labmanager.utils.cell.DropCell;

import java.util.ArrayList;
import java.util.List;

/**
 * DropGrid is an extension of AdaptiveGrid that supports drag-and-drop functionality.
 * It allows components to be dynamically placed, resized, and moved within the grid.
 *
 * @author $Author: sgalland$
 * @author $Author: pschneiderlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class DropGrid extends AdaptiveGrid {
    private static String borderColorWhenDrag = "#f2f2f2";
    private static String backgroundColorWhenEmpty = "#d2ffc4";
    private static String backgroundColorWhenFull =  "#ffc4c4";

    private DraggableComponent draggedComponent;

    private String startWidth = "";
    private String startHeight = "";

    /**
     * Default Constructor
     */
    public DropGrid() {
        super();
    }

    /**
     * Constructor
     *
     * @param rows                     number of rows in the grid.
     * @param columns                  number of columns in the grid.
     * @param borderColor              default border color.
     * @param borderColorWhenDrag      border color when a component is being dragged.
     * @param backgroundColorWhenEmpty background color when a marked cell is empty.
     * @param backgroundColorWhenFull  background color when a marked cell is full.
     */
    public DropGrid(int rows, int columns,
                    String borderColor,
                    String borderColorWhenDrag,
                    String backgroundColorWhenEmpty,
                    String backgroundColorWhenFull) {
        super(rows, columns, borderColor);
        DropGrid.borderColorWhenDrag = borderColorWhenDrag;
        DropGrid.backgroundColorWhenEmpty = backgroundColorWhenEmpty;
        DropGrid.backgroundColorWhenFull = backgroundColorWhenFull;
    }

    /**
     * Sets the grid size based on a given width (given as a JsonValue) with an adjustment
     * for additional spacing, like a header.
     */
    @Override
    protected void setGridSize(JsonValue width){
        super.setGridSize(width);
        getStyle().setWidth(width.asNumber() - 350 + "px");
    }

    /**
     * Sets the grid size based on a given width (in pixels) with an adjustment
     * for additional spacing, like a header.
     */
    @Override
    protected void setGridSize(int width) {
        int header = 50;
        if (width > 800) {
            header = 350;
        }
        int intWidth = width - header;

        super.setGridSize(intWidth);
    }

    @Override
    protected void setupComponentListeners(ComponentContainer component){
        super.setupComponentListeners(component);
        if(component instanceof DraggableComponent draggableComponent){
            addDragStartListener(draggableComponent);
            addDragEndListener(draggableComponent);
            mouseUp(draggableComponent);
            mouseDown(draggableComponent);
        }
    }

    /**
     * Adds a listener to handle the start of a drag operation for a draggable component.
     *
     * @param component The draggable component to which the drag-start listener is attached.
     */
    private void addDragStartListener(DraggableComponent component) {
        component.getDragSource().addDragStartListener(event -> {
            if(component.isDraggable()){
                draggedComponent = component;
                component.getParent().ifPresent(cell -> emptyComponentCells((DropCell) cell, component));
                showBorders(true);
                for (DropCell dropCell : getCells()) {
                    dropCell.getChild().ifPresent(cellComponent -> cellComponent.getStyle().setOpacity("0.5").setZIndex(0));
                }
            }
        });
    }

    /**
     * Adds a listener to handle the end of a drag operation for a draggable component.
     *
     * @param component The draggable component to which the drag-end listener is attached.
     */
    private void addDragEndListener(DraggableComponent component) {
        component.getDragSource().addDragEndListener(event -> {
            if(component.isDraggable()){
                hideBorders(true);

                removeCellsColor();

                draggedComponent = null;
            }
        });
    }

    /**
     * Adds a listener to handle mouse up events for resizing components.
     *
     * @param component the draggable component.
     */
    private void mouseUp(DraggableComponent component){
        setResizing(false);
        component.getElement().addEventListener("mouseup", mouseUpEvent -> component.getParent().ifPresent(parent -> {
            DropCell cell = (DropCell) parent;
            if (!canBePlaced(component, cell)) {
                component.getStyle()
                        .setWidth(startWidth)
                        .setHeight(startHeight);
                component.adaptComponentSize(() -> {});
            }
            for (DropCell dropCell : getCoveredCells(cell, component)) {
                dropCell.setEmpty(false);
            }
            removeCellsColor();
        }));
    }

    /**
     * Adds a listener to handle mouse down events for resizing components.
     *
     * @param component the draggable component.
     */
    private void mouseDown(DraggableComponent component){
        component.getElement().addEventListener("mousedown", mouseUpEvent -> {
            startWidth = component.getComponent().getStyle().get("width");
            startHeight = component.getComponent().getStyle().get("height");

            component.getParent().ifPresent(cell -> {
                emptyComponentCells((DropCell) cell, component);
                setResizing(true);
            });
        });
    }

    /**
     * Abstract method to handle logic after a container's size has been changed,
     * including the coloration of the cells when "covered" by a dragged component
     */
    @Override
    protected void afterChangingComponentSize(Component component){
        if(component instanceof DraggableComponent draggableComponent){
            if(isResizing()){
                component.getParent().ifPresent(parent -> {
                    DropCell cell = (DropCell) parent;
                    List<DropCell> coveredCells = getCoveredCells(cell, draggableComponent);
                    boolean isOverTheEdge = isOverTheEdge(cell, draggedComponent);
                    for (DropCell dropCell : getCells()) {
                        dropCell.colorCell(coveredCells.contains(dropCell), isOverTheEdge);
                        dropCell.getChild().ifPresent(cellComponent -> cellComponent.getStyle().setOpacity("0.5"));
                    }
                });
            }
        }
    }

    /**
     * Create a DropCell and add listeners to it to handle drop event.
     */
    @Override
    public DropCell createCell(int index){
        DropCell cell = new DropCell(index, getBorderColor(), borderColorWhenDrag, backgroundColorWhenEmpty, backgroundColorWhenFull);
        setupCellListeners(cell);
        return cell;
    }

    @Override
    protected void addComponentToCell(DropCell cell, Component component) {
        super.addComponentToCell(cell, component);
        for (DropCell dropCell : getCoveredCells(cell)) {
            dropCell.setEmpty(false);
        }
    }

    @Override
    public void removeComponentFromCell(DropCell cell, Component component) {
        super.removeComponentFromCell(cell, component);
        if(component instanceof DraggableComponent draggableComponent){
            emptyComponentCells(cell, draggableComponent);
        }
    }

    /**
     * Marks all cells occupied by a given draggable component as empty. This is typically
     * called during drag operations to free up grid space.
     *
     * @param startingCell The starting cell of the draggable component.
     * @param component The draggable component being removed from the cells.
     */
    private void emptyComponentCells(DropCell startingCell, DraggableComponent component){
        for (DropCell dropCell : getCoveredCells(startingCell, component)) {
            dropCell.setEmpty(true);
        }
    }

    @Override
    public DropCell findFirstEmptyCell(Component component) {
        if(component instanceof ComponentContainer componentContainer){
            for (DropCell cell : getCells()) {
                if (canBePlaced(componentContainer, cell)) {
                    return cell;
                }
            }
        }
        return null;
    }

    /**
     * Configures the listeners for a DropCell, including drop and drag-enter events.
     *
     * @param cell the DropCell to set up listeners for.
     */
    protected void setupCellListeners(DropCell cell) {

        cell.getDropTarget().addDropListener(event -> handleDrop(event, cell));

        cell.getElement().addEventListener("dragenter", event -> event.getSource().getComponent().ifPresent(component -> {
            boolean isOverTheEdge = isOverTheEdge(cell, draggedComponent);

            List<DropCell> coveredCells = getCoveredCells(cell, draggedComponent);
            for (DropCell dropCell : getCells()) {
                dropCell.colorCell(coveredCells.contains(dropCell), isOverTheEdge);
            }
        }));
    }

    /**
     * Handles drop events to place a component in a DropCell if valid.
     *
     * @param event the drop event.
     * @param cell  the cell where the drop is occurring.
     */
    private void handleDrop(DropEvent<DropCell> event, DropCell cell) {
        event.getDragSourceComponent().ifPresent(component -> {

            List<DropCell> targetCellList = getCoveredCells(cell);
            if (canBePlaced((DraggableComponent) component, cell)) {
                cell.addComponent(component);
                component.getParent().ifPresent(parent -> {
                    if (parent instanceof DropCell dropCell) {
                        dropCell.emptyCell();
                    }
                });
                for (DropCell dropCell : targetCellList) {
                    dropCell.setEmpty(false);
                }
            }else{
                component.getParent().ifPresent(parent -> {
                    for (DropCell dropCell : getCoveredCells((DropCell) parent)) {
                        dropCell.setEmpty(false);
                    }
                });
            }
        });
    }

    /**
     * Determines if a component can be placed in a given cell. This checks
     * whether the component overlaps with any other elements or extends beyond
     * the grid's boundaries.
     */
    @Override
    protected boolean canBePlaced(ComponentContainer component, DropCell cell) {
        boolean overlapsElements = true;
        if(component instanceof DraggableComponent draggableComponent){
            overlapsElements = getCoveredCells(cell, draggableComponent)
                    .stream()
                    .allMatch(DropCell::isEmpty);
        }
        return super.canBePlaced(component, cell) && overlapsElements;

    }

    /**
     * Retrieves a list of all grid cells that currently contain components.
     *
     * @return A list of grid cells containing components.
     */
    public List<DropCell> getCellsContainingComponents() {
        List<DropCell> cellsList = new ArrayList<>();
        for (DropCell cell : getCells()) {
            if (cell.containsComponent()) {
                cellsList.add(cell);
            }
        }
        return cellsList;
    }

    /**
     * Retrieves the list of grid cells covered by the currently dragged component.
     *
     * @param startingCell The starting cell of the dragged component.
     * @return A list of cells covered by the dragged component.
     */
    private List<DropCell> getCoveredCells(DropCell startingCell) {
        return getCoveredCells(startingCell, draggedComponent);
    }

    /**
     * Retrieves the list of grid cells covered by a given component
     *
     * @param startingCell The starting cell of the component.
     * @param component The draggable component covering cells
     * @return A list of cells covered by the component.
     */
    private List<DropCell> getCoveredCells(DropCell startingCell, DraggableComponent component) {
        int componentWidthInCells = calculateComponentSizeInCells(component, "width");
        int componentHeightInCells = calculateComponentSizeInCells(component, "height");

        int startCellIndex = startingCell.getIndex();
        int startingColumn = startCellIndex % getNumCols();

        List<DropCell> coveredCells = new ArrayList<>();
        for (int row = 0; row < componentHeightInCells; row++) {
            for (int col = 0; col < componentWidthInCells; col++) {
                int cellIndex = startCellIndex + row * getNumCols() + col;

                if ((startingColumn + col) < getNumCols() && cellIndex < getCells().size()) {
                    coveredCells.add(getCells().get(cellIndex));
                }
            }
        }
        if(!coveredCells.isEmpty()){
            coveredCells.add(startingCell);
        }
        return coveredCells;
    }

    /**
     * Toggles the grid's edition mode. In edition mode, components can be dragged
     * and edited, and the grid's borders are hidden or shown accordingly.
     *
     * @param editionMode true to enable edition mode; false to disable it.
     */
    public void changeEditionMode(boolean editionMode) {
        if (editionMode) {
            showBorders(false);
        } else {
            hideBorders(false);
        }
        getCellsContainingComponents().forEach(cell -> cell.getChild().ifPresent(component -> ((DraggableComponent) component).setDraggable(editionMode)));
    }

    /**
     * Resets the visual effects applied to grid cells (e.g., colors) after
     * a drag or resize operation is completed. Restores cells to their default visual state.
     */
    private void removeCellsColor(){
        for (DropCell dropCell : getCells()) {
            dropCell.getChild().ifPresent(cellComponent -> cellComponent.getStyle().setOpacity("1").setZIndex(2));
            dropCell.colorCell(false, true);
        }
    }

    /**
     * Displays the grid and its cells' borders, using a different style for drag events.
     *
     * @param isDragging true if a drag event is active; false otherwise.
     */
    public void showBorders(boolean isDragging) {
        if (isDragging) {
            changeToBorderColorWhenDrag();
        } else {
            changeToBorderColor();
        }
        getCells().forEach(cell -> {
            if (cell != null) {
                cell.showBorders(isDragging);
            }
        });
    }

    /**
     * Hides the grid and its cells' borders, using a different style for drag events.
     *
     * @param isDragging true if a drag event is active; false otherwise.
     */
    public void hideBorders(boolean isDragging) {
        if (isDragging) {
            changeToBorderColor();
        } else {
            getStyle().set("border", "none");
        }
        getCells().forEach(cell -> {
            if (cell != null) {
                cell.hideBorders(isDragging);
            }
        });
    }

    /**
     * Changes the grid's border color to the color stored into "borderColor"
     */
    private void changeToBorderColor() {
        getStyle().setBorder("1px dashed " + getBorderColor());
    }

    /**
     * Changes the grid's border color to the color stored into "borderColorWhenDrag"
     * for when a drag operation is in progress.
     */
    private void changeToBorderColorWhenDrag() {
        getStyle().setBorder("1px dashed " + borderColorWhenDrag);
    }
}