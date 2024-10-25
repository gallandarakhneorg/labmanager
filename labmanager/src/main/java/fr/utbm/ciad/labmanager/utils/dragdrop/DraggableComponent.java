package fr.utbm.ciad.labmanager.utils.dragdrop;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.html.Div;


public class DraggableComponent extends Div {

    private Component component;

    /** Constructor for DraggableComponent.
     *
     * @param component the component to make draggable.
     * @param dropGrid the DropGrid that will handle the drag-and-drop functionality.
     */
    public DraggableComponent(Component component, DropGrid dropGrid) {
        setComponent(component);
        getStyle().set("z-index", "999");
        getStyle().set("resize", "both");
        getStyle().set("overflow", "hidden");
        getStyle().set("position", "absolute");

        DragSource<Component> dragSource = DragSource.create(this);
        dragSource.setDraggable(true);

        // Listener for when dragging starts
        dragSource.addDragStartListener(event -> {
            dropGrid.setDragging(true);
            dropGrid.showBorders();
        });

        // Listener for when dragging ends
        dragSource.addDragEndListener(event -> {
            dropGrid.setDragging(false);
            dropGrid.hideBorders();
        });

        component.getElement().addEventListener("mouseenter", event ->
                getStyle().set("border", "2px solid blue"));

        component.getElement().addEventListener("mouseleave", event ->
                getStyle().set("border", "none"));
    }

    /** Retrieves the current UI component.
     * This method resets certain style properties before returning
     * the component, ensuring it has the correct dimensions and layering.
     *
     * @return the current Component.
     */
    public Component getComponent() {
        Component component = this.component;
        component.getStyle().remove("z-index");
        component.getStyle().set("width", getStyle().get("width"));
        component.getStyle().set("height", getStyle().get("height"));
        component.getElement().setAttribute("data-custom-id", getElement().getAttribute("data-custom-id"));
        return component;
    }

    private void setComponent(Component component){
        if(this.component == null){
            if(component.getElement().getAttribute("data-custom-id") != null){
                getElement().setAttribute("data-custom-id", component.getElement().getAttribute("data-custom-id"));
            }
            getStyle().setWidth(component.getStyle().get("width")).setHeight(component.getStyle().get("height"));
            component.getStyle().set("width", "100%");
            component.getStyle().set("height", "100%");
            component.getStyle().set("z-index", "0");
            this.component = component;
            add(this.component);
        }
    }
}
