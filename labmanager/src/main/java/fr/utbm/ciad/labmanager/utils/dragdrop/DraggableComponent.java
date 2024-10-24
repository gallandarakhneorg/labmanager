package fr.utbm.ciad.labmanager.utils.dragdrop;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.html.Div;


public class DraggableComponent extends Div {

    private final Component component;

    /**
     * Constructor for DraggableComponent.
     * Initializes the draggable behavior for the given component
     * within the specified DropGrid.
     *
     * @param component the component to make draggable.
     * @param dropGrid the DropGrid that will handle the drag-and-drop functionality.
     */
    public DraggableComponent(Component component, DropGrid dropGrid) {
        this.component = component;
        this.component.getStyle().set("width","100%");
        this.component.getStyle().set("height","100%");

        DragSource<Component> dragSource = DragSource.create(this);
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

        component.getElement().addEventListener("mouseenter", event ->
                getStyle().set("border", "2px solid blue"));

        component.getElement().addEventListener("mouseleave", event ->
                getStyle().set("border", "none"));

        add(component);
    }

    /**
     * Retrieves the current UI component.
     *
     * @return the current Component.
     */
    public Component getComponent() {
        return component;
    }
}
