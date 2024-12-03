package fr.utbm.ciad.labmanager.utils.cell;

import com.vaadin.flow.component.Component;

/**
 * Defines the contract for a cell-like component in a layout (a grid for example).
 *
 * @author $Author: sgalland$
 * @author $Author: pschneiderlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public interface InterfaceCell {

    /**
     * Applies the style settings to the cell.
     * Implementations should define specific styling logic.
     */
    void setCellStyle();

    /**
     * Adds a child component to the cell, replacing the existing one if present.
     *
     * @param component the component to add to the cell.
     */
    void addComponent(Component component);

    /**
     * Empties the cell by removing the child component
     */
    void emptyCell();

    /**
     * Checks if the cell is empty (contains no child component).
     *
     * @return true if the cell is empty; false otherwise.
     */
    boolean isEmpty();

}