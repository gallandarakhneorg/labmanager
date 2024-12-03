package fr.utbm.ciad.labmanager.utils.grid;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.dom.Style;
import fr.utbm.ciad.labmanager.utils.cell.DropCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract implementation of a grid layout with a fixed number of rows and columns,
 * where each cell is represented as a DropCell.
 *
 * @author $Author: sgalland$
 * @author $Author: pschneiderlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public abstract class AbstractGrid extends FlexLayout implements InterfaceGrid {

    private static String borderColor;

    private final int numRows;
    private final int numCols;
    private double cellSize = 50 ;

    private final List<DropCell> cells = new ArrayList<>();

    /**
     * Default Constructor
     */
    public AbstractGrid(){
        this(30,30, "#bfbfbf");
    }

    /**
     * Constructor
     *
     * @param rows        the number of rows in the grid.
     * @param columns     the number of columns in the grid.
     * @param borderColor the default border color for the grid and its cells.
     */
    public AbstractGrid(int rows, int columns, String borderColor) {
        this.numRows = rows;
        this.numCols = columns;
        AbstractGrid.borderColor = borderColor;
        initializeGrid();
    }

    @Override
    public void initializeGrid(){
        setGridStyle();
        createCells();
    }

    public void setGridStyle() {
        setFlexDirection(FlexDirection.ROW);
        getStyle().setBackgroundColor("rgba(255, 255, 255, 0.5)")
                .setDisplay(Style.Display.GRID)
                .set("grid-template-columns", "repeat(" + numCols + ", 1fr)")
                .set("grid-template-rows", "repeat(" + numRows + ", 1fr)")
                .set("gap", "0px");
    }

    @Override
    public void createCells(){
        int cellCount = numRows * numCols;
        cells.clear();
        removeAll();
        for (int i = 0; i < cellCount; i++) {
            DropCell cell = createCell(i);
            cells.add(cell);
            add(cell);
        }
    }

    /**
     * Create a DropCell.
     *
     * @param index the index of the cell to be created.
     * @return a new instance of DropCell.
     */
    @Override
    public abstract DropCell createCell(int index);

    /**
     * Adds a component to a specific cell.
     *
     * @param cell      the cell to which the component should be added.
     * @param component the component to add.
     */
    protected void addComponentToCell(DropCell cell, Component component) {
        cell.addComponent(component);
    }

    /**
     * Removes a specified component from a given grid cell.
     *
     * @param cell The cell from which the component is being removed.
     * @param component The draggable component to remove.
     */
    public void removeComponentFromCell(DropCell cell, Component component) {
        if(cell.contains(component)){
            cell.emptyCell();
        }
    }

    /**
     * Removes a given draggable component from its parent cell, updating
     * the state of the affected cells to be empty.
     *
     * @param component The draggable component to remove.
     */
    public void removeComponent(Component component){
        component.getParent().ifPresent(parent -> removeComponentFromCell((DropCell) parent, component));
    }

    /**
     * Adds a component to the first available empty cell in the grid.
     *
     * @param component the component to add.
     */
    public void addComponentInFirstEmptyCell(Component component) {
        DropCell cell = findFirstEmptyCell(component);
        if (cell != null) {
            addComponentToCell(cell, component);
            cell.setEmpty(false);
        }
    }

    /**
     * Finds the first empty cell in the grid.
     *
     * @param component the component that is to be placed in the grid (optional for future use).
     * @return the first empty DropCell, or null if all cells are occupied.
     */
    public DropCell findFirstEmptyCell(Component component) {
        for (DropCell cell : getCells()) {
            if (cell.isEmpty()) {
                return cell;
            }
        }
        return null;
    }
    /**
     * Checks if all cells in the grid are empty.
     *
     * @return true if all cells are empty, false otherwise.
     */

    public boolean isEmpty() {
        return getCells().stream().allMatch(DropCell::isEmpty);
    }

    /**
     * Retrieves the list of all cells in the grid.
     *
     * @return a list of DropCell objects.
     */
    public List<DropCell> getCells() {
        return cells;
    }

    /**
     * Retrieves the number of columns in the grid.
     *
     * @return the number of columns.
     */
    public int getNumCols() {
        return numCols;
    }

    /**
     * Retrieves the number of rows in the grid.
     *
     * @return the number of rows.
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * Retrieves the current border color for the grid.
     *
     * @return the border color as a String.
     */
    public String getBorderColor() {
        return borderColor;
    }

    /**
     * Retrieves the current size of the cells.
     *
     * @return the size of the cells.
     */
    public double getCellSize() {
        return cellSize;
    }

    /**
     * Sets the size of the cells in the grid.
     *
     * @param cellSize the new size for the cells.
     */
    public void setCellSize(double cellSize){
        this.cellSize = cellSize;
    }
}