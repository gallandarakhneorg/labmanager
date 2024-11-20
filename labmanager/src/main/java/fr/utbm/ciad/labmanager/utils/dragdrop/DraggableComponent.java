package fr.utbm.ciad.labmanager.utils.dragdrop;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.html.Div;
import fr.utbm.ciad.labmanager.views.components.charts.layout.PublicationCategoryLayout;

public class DraggableComponent extends Div {

    private Component component;
    private final DragSource<Component> dragSource;

    /**
     * Constructor for DraggableComponent.
     *
     * @param component the component to make draggable.
     */
    public DraggableComponent(Component component) {
        setComponent(component);
        getStyle().set("z-index", "2");
        getStyle().set("resize", "both");
        getStyle().setBoxShadow("0 4px 8px #00000033");
        getStyle().setBorderRadius("8px");
        getStyle().set("overflow", "hidden");
        getStyle().set("position", "absolute");

        dragSource = DragSource.create(this);
        dragSource.setDraggable(true);

        getElement().addEventListener("mouseenter", event ->
                component.getStyle().set("border", "2px solid #c4c5ff"));

        getElement().addEventListener("mouseleave", event ->
                component.getStyle().set("border", "none"));
    }

    /**
     * Adapts the size of the wrapped component based on the size of the DraggableComponent.
     * This method retrieves the current width and height of the component and updates
     * the dimensions of the wrapped component accordingly.
     */
    public void adaptComponentSize(Runnable callback) {
        getElement().executeJs("return this.offsetWidth;")
                .then(width -> getElement().executeJs("return this.offsetHeight;")
                        .then(height -> {
                            setComponentSize(width.asNumber(), height.asNumber());
                            if (isChart()) {
                                ((PublicationCategoryLayout<?>) component).setSize(
                                        Math.round(width.asNumber() * 0.9) + "px",
                                        Math.round(height.asNumber() * 0.9) + "px");
                            }
                            if (callback != null) {
                                callback.run();
                            }
                        }));
    }

    /**
     * Retrieves the current UI component.
     *
     * @return the current Component.
     */
    public Component getComponent() {
        Component component = this.component;
        component.getElement().setAttribute("data-custom-id", getElement().getAttribute("data-custom-id"));
        component.getStyle().setBoxShadow("0 4px 8px #00000033");
        component.getStyle().setBorderRadius("8px");
        return component;
    }

    /**
     * Sets the component to be made draggable.
     * This method initializes the component's style properties
     * and adds it to the DraggableComponent.
     *
     * @param component the component to set.
     */
    private void setComponent(Component component) {
        if (this.component == null) {
            component.getStyle().set("z-index", "1");
            String componentName = component.getElement().getAttribute("data-custom-id");
            if (componentName != null) {
                getElement().setAttribute("data-custom-id", componentName);
            }
            getStyle().setWidth(component.getStyle().get("width"))
                    .setHeight(component.getStyle().get("height"));

            this.component = component;
            add(this.component);
            if (isChart()) {
                getStyle().setMinHeight("100px");
                getStyle().setMinWidth("200px");
            }
        }
    }

    /**
     * Checks if the current component is an instance of PublicationCategoryLayout.
     *
     * @return true if the component is a chart layout; false otherwise.
     */
    public boolean isChart() {
        return component instanceof PublicationCategoryLayout<?>;
    }

    /**
     * Retrieves the drag source associated with the component.
     *
     * @return the drag source for the component.
     */
    public DragSource<Component> getDragSource() {
        return dragSource;
    }

    /**
     * Sets the size of the component by specifying its width and height.
     *
     * @param width the new width of the component.
     * @param height the new height of the component.
     */
    public void setComponentSize(double width, double height) {
        component.getStyle()
                .setWidth(Math.round(width) + "px")
                .setHeight(Math.round(height) + "px");
    }
}


