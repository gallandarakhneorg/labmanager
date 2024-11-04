package fr.utbm.ciad.labmanager.utils.dragdrop;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.dnd.DropEvent;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import fr.utbm.ciad.labmanager.views.components.charts.layout.AbstractChartLayout;

import java.util.Optional;

//TODO check the remove component functionality that doesn't work as intended
public class DropCell extends VerticalLayout {

    private final Integer index;
    private final DropGrid dropGrid;
    private boolean isEmpty = true;

    /**
     * Constructor for DropCell with specified index.
     *
     * @param dropGrid the parent DropGrid that contains this cell.
     * @param index the unique index of the cell within the grid.
     */
    public DropCell(DropGrid dropGrid, Integer index) {
        this.dropGrid = dropGrid;
        this.index = index;
        setWidth("100%");
        setHeight("100%");
        getStyle().set("border", "1px solid transparent")
                .set("display", "flex")
                .set("align-items", "flex-start")
                .set("justify-content", "flex-start")
                .set("position", "relative");

        DropTarget<VerticalLayout> dropTarget = DropTarget.create(this);
        dropTarget.addDropListener(this::handleDrop);

        // Event listener for when a draggable component enters the cell
        getElement().addEventListener("dragenter", event -> {
            if (dropGrid.isDragging()) {
                getStyle().set("border", "2px solid blue");
            }
        });

        // Event listener for when a draggable component leaves the cell
        getElement().addEventListener("dragleave", event -> {
            if (dropGrid.isDragging() && getChildren().findAny().isEmpty()) {
                getStyle().set("border", "1px dashed #bfbfbf");
            }
        });
    }

    /**
     * Handles the drop event when a component is dropped into the cell.
     *
     * @param event the drop event containing the dragged component.
     */
    private void handleDrop(DropEvent<VerticalLayout> event) {
        event.getDragSourceComponent().ifPresent(draggedComponent -> {
            if (isEmpty) {
                draggedComponent.getParent().ifPresent(parent -> {
                    if (parent instanceof VerticalLayout) {
                        ((HasComponents) parent).remove(draggedComponent);
                    }
                });
                addComponent(draggedComponent);
            }
        });
    }

    /**
     * Checks if the cell is currently empty (does not contain any component).
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
        if(dragging){
            getStyle().set("border", "1px dashed #bfbfbf");
        }else{
            getStyle().set("border", "1px dashed #f2f2f2");
        }
    }

    /**
     * Hide the cell borders depending on the event calling the method (triggering edit mode or dragging a component)
     *
     * @param dragging true if the event is dragging, false otherwise
     */
    public void hideBorders(boolean dragging) {
        if(dragging){
            getStyle().set("border", "1px dashed #f2f2f2");
        }else{
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
        if (isEmpty) {
            add(component);
            if (component instanceof AbstractChartLayout) {
                ((AbstractChartLayout) component).refreshChart();
            } else if (component instanceof DraggableComponent) {
                if (((DraggableComponent) component).getComponent() instanceof AbstractChartLayout) {
                    ((AbstractChartLayout) ((DraggableComponent) component).getComponent()).refreshChart();
                }
            }
            isEmpty = false;
        }
    }

    /**
     * Clears all components from the cell
     */
    public void emptyCell() {
        if (!isEmpty) {
            removeAll();
            isEmpty = true;
        }
    }

    /**
     * Toggle the cell to edition mode.
     */
    public void changeEditionMode(boolean editionMode) {
        getChild().ifPresent(component -> {
            emptyCell();
            if (editionMode) {
                showBorders(false);
                addComponent(new DraggableComponent(component, dropGrid));
            } else {
                hideBorders(false);
                addComponent(((DraggableComponent) component).getComponent());
            }
        });
    }

    /**
     * Retrieves the unique index of the cell within the grid.
     *
     * @return the cell's index as an Integer.
     */
    public Integer getIndex() {
        return index;
    }
}
