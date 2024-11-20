package fr.utbm.ciad.labmanager.utils.dragdrop;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import fr.utbm.ciad.labmanager.views.components.charts.layout.PublicationCategoryLayout;

import java.util.Optional;

public class DropCell extends VerticalLayout {

    private final Integer index;
    private boolean isEmpty = true;
    private Component component;
    private final DropTarget<VerticalLayout> dropTarget;
    private final String borderColor;
    private final String borderColorWhenDrag;
    private final String backgroundColorWhenEmpty;
    private final String backgroundColorWhenFull;

    public DropCell() {
        this(0);
    }

    public DropCell(Integer index) {
        this(index, "#f2f2f2", "#bfbfbf", "#d2ffc4", "#ffc4c4");
    }

    /**
     * Constructor for DropCell with specified index.
     *
     * @param index the unique index of the cell within the grid.
     */
    public DropCell(Integer index,
                    String borderColor,
                    String borderColorWhenDrag,
                    String backgroundColorWhenEmpty,
                    String backgroundColorWhenFull) {
        this.component = null;
        this.borderColor = borderColor;
        this.borderColorWhenDrag = borderColorWhenDrag;
        this.backgroundColorWhenEmpty = backgroundColorWhenEmpty;
        this.backgroundColorWhenFull = backgroundColorWhenFull;

        this.index = index;
        setWidth("100%");
        setHeight("100%");
        getStyle().set("border", "1px solid transparent")
                .set("display", "flex")
                .set("align-items", "flex-start")
                .set("justify-content", "flex-start")
                .set("position", "relative")
                .setPadding("0");

        dropTarget = DropTarget.create(this);
    }

    /**
     * Retrieves the drop target associated with this cell.
     *
     * @return the DropTarget for the cell.
     */
    DropTarget<VerticalLayout> getDropTarget() {
        return dropTarget;
    }

    /**
     * Checks if the cell is currently empty (does not contain any component or is not covered by any component).
     *
     * @return true if the cell has no components, false otherwise.
     */
    public boolean isEmpty() {
        return isEmpty;
    }

    /**
     * Displays the cell borders depending on the event calling the method (triggering edit mode or dragging a component)
     *
     * @param dragging true if the event is dragging, false otherwise
     */
    public void showBorders(boolean dragging) {
        if (dragging) {
            changeToBorderColorWhenDrag();
        } else {
            changeToBorderColor();
        }
    }

    /**
     * Hide the cell borders depending on the event calling the method (triggering edit mode or dragging a component)
     *
     * @param dragging true if the event is dragging, false otherwise
     */
    public void hideBorders(boolean dragging) {
        if (dragging) {
            changeToBorderColorWhenDrag();
        } else {
            getStyle().set("border", "none");
        }
    }

    /**
     * Retrieves the first component in the cell, if present.
     *
     * @return an Optional containing the first component in the cell, or empty if none exists.
     */
    public Optional<Component> getChild() {
        return getChildren().findFirst();
    }

    /**
     * Adds a component to the cell.
     *
     * @param component the component to add to the cell.
     */
    public void addComponent(Component component) {
        add(component);
        this.component = component;
        if (component instanceof PublicationCategoryLayout) {
            ((PublicationCategoryLayout<?>) component).refreshChart();
        } else if (component instanceof DraggableComponent &&
                ((DraggableComponent) component).getComponent() instanceof PublicationCategoryLayout) {
            ((PublicationCategoryLayout<?>) ((DraggableComponent) component).getComponent()).refreshChart();
        }
    }

    /**
     * Clears all components from the cell
     */
    public void emptyCell() {
        if (!isEmpty) {
            removeAll();
            isEmpty = true;
            component = null;
        }
    }

    /**
     * Retrieves the unique index of the cell within the grid.
     *
     * @return the cell's index as an Integer.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Resizes the component in the cell according to the specified side.
     *
     * @param side the given side.
     */
    public void resizeComponent(double side) {
        if (!isEmpty) {
            getChild().ifPresent(component -> {
                long newWidth = Math.round(getComponentSide(component, "width") * side / getCellSide());
                long newHeight = Math.round(getComponentSide(component, "height") * side / getCellSide());

                component.getElement().getStyle().setWidth(newWidth + "px");
                component.getElement().getStyle().setHeight(newHeight + "px");
            });
        }
    }

    /**
     * Retrieves the size of a specific side of the component (either width or height).
     *
     * @param component the component whose side size is to be retrieved.
     * @param side the side of the component ("width" or "height").
     * @return the size of the specified side of the component as an integer.
     */
    private int getComponentSide(Component component, String side) {
        return Integer.parseInt(component.getStyle().get(side).replace("px", ""));
    }

    /**
     * Retrieves the size of the cell's width.
     *
     * @return the width of the cell as an integer.
     */
    private int getCellSide() {
        return getComponentSide(this, "width");
    }

    /**
     * Sets the emptiness state of the cell.
     *
     * @param empty true if the cell should be marked as empty, false otherwise.
     */
    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    /**
     * Colors the cell depending on the provided conditions.
     *
     * @param color true to color the cell, false to reset its background.
     * @param error true if an error state should be applied, false otherwise.
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
        getStyle().set("border", "1px dashed " + borderColor);
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

    /**
     * Checks if the specified component is the same as the component in the cell.
     *
     * @param component the component to check.
     * @return true if the cell contains the specified component, false otherwise.
     */
    public boolean contains(Component component) {
        return this.component != null && this.component.equals(component);
    }

    /**
     * Checks if the cell contains a component.
     *
     * @return true if the cell contains a component, false otherwise.
     */
    public boolean containsComponent() {
        return !(this.component == null);
    }
}
