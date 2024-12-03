package fr.utbm.ciad.labmanager.utils.cell;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.dom.Style;

import fr.utbm.ciad.labmanager.utils.container.DraggableComponent;
import fr.utbm.ciad.labmanager.views.components.charts.layout.PublicationCategoryLayout;

/**
 * A specialized cell that supports drag-and-drop functionality by handling drag events.
 *
 * @author $Author: sgalland$
 * @author $Author: pschneiderlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class DropCell extends AdaptiveCell {

    private static String borderColorWhenDrag = "#bfbfbf";
    private static String backgroundColorWhenEmpty = "#d2ffc4";
    private static String backgroundColorWhenFull = "#ffc4c4";

    private boolean isEmpty = true;
    private final DropTarget<DropCell> dropTarget;

    /**
     * Default Constructor
     */
    public DropCell() {
        this(0);
    }

    /**
     * Constructor with defaults colors
     *
     * @param index the index of the cell.
     */
    public DropCell(Integer index) {
        super(index);
        dropTarget = DropTarget.create(this);
    }

    /**
     * Constructor
     *
     * @param index                   the index of the cell.
     * @param borderColor             the default border color.
     * @param borderColorWhenDrag     the border color during drag events.
     * @param backgroundColorWhenEmpty the background color when the cell is empty.
     * @param backgroundColorWhenFull the background color when the cell is full.
     */
    public DropCell(Integer index,
                    String borderColor,
                    String borderColorWhenDrag,
                    String backgroundColorWhenEmpty,
                    String backgroundColorWhenFull) {
        super(index, borderColor);
        DropCell.borderColorWhenDrag = borderColorWhenDrag;
        DropCell.backgroundColorWhenEmpty = backgroundColorWhenEmpty;
        DropCell.backgroundColorWhenFull = backgroundColorWhenFull;

        setCellStyle();

        dropTarget = DropTarget.create(this);
    }

    @Override
    public void setCellStyle(){
        setWidth("100%");
        setHeight("100%");
        getStyle().setBorder("1px solid transparent")
                .setDisplay(Style.Display.FLEX)
                .setAlignItems(Style.AlignItems.FLEX_START)
                .setJustifyContent(Style.JustifyContent.FLEX_START)
                .setPosition(Style.Position.RELATIVE)
                .setPadding("0")
                .setBoxSizing(Style.BoxSizing.BORDER_BOX)
                .setMargin("0")
                .setPadding("0")
                .setDisplay(Style.Display.FLEX);
    }

    @Override
    public void addComponent(Component component){
        if(component instanceof  DraggableComponent){
            addComponent((DraggableComponent) component);
        }else{
            super.addComponent(component);
        }
    }

    /**
     * Adds a draggable component to the cell, with specific handling for PublicationCategoryLayout components.
     *
     * @param component the DraggableComponent to add.
     */
    public void addComponent(DraggableComponent component) {
        super.addComponent(component);
        if (component.getComponent() instanceof PublicationCategoryLayout) {
            ((PublicationCategoryLayout<?>) component.getComponent()).refreshChart();
        }
    }

    @Override
    public void emptyCell() {
        super.emptyCell();
        if (!isEmpty) {
            isEmpty = true;
        }
    }

    /**
     * Checks if the cell is currently empty (does not contain any component or is not covered by any component).
     */
    @Override
    public boolean isEmpty() {
        return isEmpty;
    }

    /**
     * Sets the emptiness state of the cell.
     *
     * @param isEmpty true to mark the cell as empty; false otherwise.
     */
    public void setEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    /**
     * Retrieves the drop target associated with this cell.
     *
     * @return the DropTarget for the cell.
     */
    public DropTarget<DropCell> getDropTarget() {
        return dropTarget;
    }

    /**
     * Displays the cell borders, using a different style for drag events.
     *
     * @param isDragging true if a drag event is active; false otherwise.
     */
    public void showBorders(boolean isDragging) {
        if (isDragging) {
            changeToBorderColorWhenDrag();
        } else {
            changeToBorderColor();
        }
    }

    /**
     * Hides the cell borders, using a different style for drag events.
     *
     * @param isDragging true if a drag event is active; false otherwise.
     */
    public void hideBorders(boolean isDragging) {
        if (isDragging) {
            changeToBorderColorWhenDrag();
        } else {
            getStyle().set("border", "none");
        }
    }

    /**
     * Colors the cell depending on the provided conditions.
     *
     * @param color true to color the cell when a component is dragged on it,
     *              false to reset its background.
     * @param error true if an error state should be applied (color with the backgroundColorWhenFull color),
     *              false otherwise.
     */
    public void colorCell(boolean color, boolean error) {
        if (color) {
            if (isEmpty && !error) {
                changeToBackgroundColorWhenEmpty();
            } else {
                changeToBackgroundColorWhenFull();
            }
        } else {
            getStyle().setBackgroundColor("transparent");
        }
    }

    /**
     * Changes the border color of the cell to its default color.
     */
    private void changeToBorderColor() {
        getStyle().set("border", "1px dashed " + getBorderColor());
    }

    /**
     * Changes the border color of the cell to the color when a drag event is active.
     */
    private void changeToBorderColorWhenDrag() {
        getStyle().set("border", "1px dashed " + borderColorWhenDrag);
    }

    /**
     * Changes the border color of the empty cell to the corresponding color when a component is dragged on it
     */
    private void changeToBackgroundColorWhenEmpty() {
        getStyle().setBackgroundColor(backgroundColorWhenEmpty);
    }

    /**
     * Changes the border color of the full cell to the corresponding color when a component is dragged on it
     */
    private void changeToBackgroundColorWhenFull() {
        getStyle().setBackgroundColor(backgroundColorWhenFull);
    }
}