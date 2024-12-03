package fr.utbm.ciad.labmanager.utils.container;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import elemental.json.JsonValue;

/**
 * An abstract implementation of a specialized container to manage a single child component.
 *
 * @author $Author: sgalland$
 * @author $Author: pschneiderlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public abstract class ComponentContainer extends FlexLayout {

    private Component component;

    /**
     * Default Constructor
     */
    public ComponentContainer(){
        this(new FlexLayout());
    }

    /**
     * Constructor.
     *
     * @param component the component to be contained
     */
    public ComponentContainer(Component component) {
        setComponent(component);
        setStyle();
    }

    /**
     * Sets the specified component as the contained component of this container.
     * Applies initial styling.
     *
     * @param component the component to set and manage within this container.
     */
    public void setComponent(Component component) {
        if (isEmpty()) {
            this.component = component;
            setComponentStyle();
            add(this.component);
        }
    }

    /**
     * Initialize the component style and layout properties.
     */
    protected void setComponentStyle(){
        component.getStyle().setZIndex(1);
        getStyle().setWidth(component.getStyle().get("width"))
                .setHeight(component.getStyle().get("height"));
    }

    /**
     * Initialize the container style and layout properties.
     */
    protected void setStyle(){
        getStyle().setZIndex(2);
    }

    /**
     * Adapts the size of the contained component to match its parent's dimensions,
     * executing the provided callback once the adjustment is complete.
     *
     * @param callback a Runnable to execute after resizing the component; can be null.
     */
    public void adaptComponentSize(Runnable callback) {
        getElement().executeJs("return this.offsetWidth;")
                .then(width -> getElement().executeJs("return this.offsetHeight;")
                        .then(height -> {
                            setComponentSize(width, height);
                            if (callback != null) {
                                callback.run();
                            }
                        }));
    }

    /**
     * Sets the size of the contained component to the specified width and height.
     *
     * @param width  the new width for the component, in pixels.
     * @param height the new height for the component, in pixels.
     */
    public void setComponentSize(double width, double height) {
        component.getStyle()
                .setWidth(Math.round(width) + "px")
                .setHeight(Math.round(height) + "px");
    }

    /**
     * Sets the size of the contained component using JsonValue objects representing
     * the width and height.
     *
     * @param width  a JsonValue representing the new width, in pixels.
     * @param height a JsonValue representing the new height, in pixels.
     */
    public void setComponentSize(JsonValue width, JsonValue height) {
        double widthDouble = width.asNumber();
        double heightDouble = height.asNumber();
        setComponentSize(widthDouble, heightDouble);
    }

    /**
     * Retrieves the contained component.
     *
     * @return the currently contained component, or null if the container is empty.
     */
    public Component getComponent() {
        return component;
    }

    /**
     * Checks whether the container is empty (i.e., no component is currently contained).
     *
     * @return true if the container is empty; false otherwise.
     */
    public boolean isEmpty(){
        return component == null;
    }
}