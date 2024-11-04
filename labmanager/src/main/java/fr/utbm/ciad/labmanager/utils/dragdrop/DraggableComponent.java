package fr.utbm.ciad.labmanager.utils.dragdrop;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.html.Div;

public class DraggableComponent extends Div {

    private Component component;

    /**
     * Constructor for DraggableComponent.
     *
     * @param component the component to make draggable.
     * @param dropGrid the DropGrid that will handle the drag-and-drop functionality.
     */
    public DraggableComponent(Component component, DropGrid dropGrid) {
        setComponent(component);
        getStyle().set("z-index", "2");
        getStyle().set("resize", "both");
        getStyle().set("overflow", "hidden");
        getStyle().set("position", "absolute");

        DragSource<Component> dragSource = DragSource.create(this);
        dragSource.setDraggable(true);

        addResizeListener();

        // Listener for when dragging starts
        dragSource.addDragStartListener(event -> {
            dropGrid.setDragging(true);
            dropGrid.showBorders(true);
        });

        // Listener for when dragging ends
        dragSource.addDragEndListener(event -> {
            dropGrid.setDragging(false);
            dropGrid.hideBorders(true);
        });

        component.getElement().addEventListener("mouseenter", event ->
                getStyle().set("border", "2px solid blue"));

        component.getElement().addEventListener("mouseleave", event ->
                getStyle().set("border", "none"));
    }

    /**
     * Adds a resize listener to the component that dispatches a custom resize event
     * whenever the size of the component changes.
     */
    private void addResizeListener(){
        String jsCode =
                "const resizeObserver = new ResizeObserver(entries => {" +
                        "   for (let entry of entries) {" +
                        "       this.dispatchEvent(new Event('custom-resize')); " +
                        "   }" +
                        "});" +
                        "resizeObserver.observe(this);";

        getElement().executeJs(jsCode);

        getElement().addEventListener("custom-resize", event -> adaptComponentSize());
    }

    /**
     * Adapts the size of the wrapped component based on the size of the DraggableComponent.
     * This method retrieves the current width and height of the component and updates
     * the dimensions of the wrapped component accordingly.
     */
    private void adaptComponentSize() {
        getElement().executeJs("return this.offsetWidth;")
                .then(width -> {
                    component.getStyle()
                            .set("width", width.asString() + "px");
                    getElement().executeJs("return this.offsetHeight;")
                            .then(height -> component.getStyle()
                                    .set("height", height.asString() + "px"));
                });
    }

    /**
     * Retrieves the current UI component.
     *
     * @return the current Component.
     */
    public Component getComponent() {
        Component component = this.component;

        component.getElement().setAttribute("data-custom-id", getElement().getAttribute("data-custom-id"));
        return component;
    }

    /**
     * Sets the component to be made draggable.
     * This method initializes the component's style properties
     * and adds it to the DraggableComponent.
     *
     * @param component the component to set.
     */
    private void setComponent(Component component){
        if(this.component == null){
            component.getStyle().set("z-index", "1");
            String componentName = component.getElement().getAttribute("data-custom-id");
            if(componentName != null){
                getElement().setAttribute("data-custom-id", componentName);
            }
            getStyle().setWidth(component.getStyle().get("width"))
                    .setHeight(component.getStyle().get("height"));

            this.component = component;
            add(this.component);
        }
    }
}


