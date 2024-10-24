package fr.utbm.ciad.labmanager.utils.dragdrop;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dnd.DragSource;

public class DraggableComponent {

    /**
     * Constructor for DraggableComponent.
     * Initializes the draggable behavior for the given component
     * within the specified DropGrid.
     *
     * @param component the component to make draggable.
     * @param dropGrid the DropGrid that will handle the drag-and-drop functionality.
     */
    public DraggableComponent(Component component, DropGrid dropGrid) {
        DragSource<Component> dragSource = DragSource.create(component);
        dragSource.setDraggable(true);

        // Listener for when dragging starts
        dragSource.addDragStartListener(event -> {
            dropGrid.setDragging(true);
            dropGrid.showGridBorders();
        });

        // Listener for when dragging ends
        dragSource.addDragEndListener(event -> {
            dropGrid.setDragging(false);
            dropGrid.hideGridBorders();
        });

        addHoverEffectToComponent(component);
    }

    /**
     * Adds a hover effect to the given component.
     * This method sets up mouseover and mouseout event listeners
     * to change the border style of the component when hovered over.
     *
     * @param component the component to which the hover effect will be added.
     */
    private void addHoverEffectToComponent(Component component) {
        component.getElement().addEventListener("mouseover", event ->
                component.getStyle().set("border", "2px solid blue"));

        component.getElement().addEventListener("mouseout", event ->
                component.getStyle().set("border", "none"));
    }
}
