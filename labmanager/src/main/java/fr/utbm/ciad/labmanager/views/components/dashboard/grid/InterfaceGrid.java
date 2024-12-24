package fr.utbm.ciad.labmanager.views.components.dashboard.grid;

import fr.utbm.ciad.labmanager.views.components.dashboard.cell.AbstractCell;

/**
 * Interface defining the structure and behavior of a grid layout system.
 * Provides methods for creating, initializing, and styling grid cells.
 *
 * @author $Author: sgalland$
 * @author $Author: pschneiderlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public interface InterfaceGrid {

    /**
     * Initializes the grid by configuring its layout, style and listeners.
     */
    void initializeGrid();

    /**
     * Applies the styling configuration for the grid.
     * This includes properties like size, spacing, borders, and alignment.
     */
    void setGridStyle();

    /**
     * Creates all cells for the grid.
     * This method handle the generation and placement of cells within the grid.
     */
    void createCells();

    /**
     * Creates a new cell to be placed in the grid.
     *
     * @param index the index of the cell to be created.
     * @return an instance of AbstractCell representing the created cell.
     */
    AbstractCell createCell(int index);
}