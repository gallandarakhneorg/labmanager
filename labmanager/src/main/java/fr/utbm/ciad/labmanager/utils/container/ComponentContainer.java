package fr.utbm.ciad.labmanager.utils.container;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import elemental.json.JsonValue;

import java.util.function.Consumer;

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
    private Consumer<ComponentContainer> afterResizingInstructions;

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
        addResizeListener();
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

    private void addResizeListener() {
        String jsCode =
                "const resizeObserver = new ResizeObserver(entries => {" +
                        "   for (let entry of entries) {" +
                        "       entry.target.dispatchEvent(new Event('custom-resize')); " +
                        "   }" +
                        "});" +
                        "resizeObserver.observe(this);";

        getElement().executeJs(jsCode);

        getElement().addEventListener("custom-resize",event ->
                adaptComponentSize());
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
        setToTheFore(true);
    }

    public void setAfterResizingInstructions(Consumer<ComponentContainer> afterResizingInstructions) {
        this.afterResizingInstructions = afterResizingInstructions;
    }

    public Consumer<ComponentContainer> getAfterResizingInstructions() {
        return afterResizingInstructions;
    }

    /**
     * Adapts the size of the contained component to match its parent's dimensions,
     * executing the provided callback once the adjustment is complete.
     *
     */
    public void adaptComponentSize() {
        getElement().executeJs("return this.offsetWidth;")
                .then(width -> getElement().executeJs("return this.offsetHeight;")
                        .then(height -> {
                            setComponentSize(width, height);
                            afterResizingInstructionsCall();
                        }));
    }

    protected void afterResizingInstructionsCall(){
        if(afterResizingInstructions != null){
            afterResizingInstructions.accept(this);
        }
    }

    /**
     * Sets the size of the contained component to the specified width and height.
     *
     * @param width  the new width for the component, in pixels.
     * @param height the new height for the component, in pixels.
     */
    public void setSize(String width, String height) {
        getStyle()
                .setWidth(width)
                .setHeight(height);
        setComponentSize(width, height);
    }

    /**
     * Sets the size of the contained component to the specified width and height.
     *
     * @param width  the new width for the component, in pixels.
     * @param height the new height for the component, in pixels.
     */
    public void setSize(long width, long height) {
        setSize(width + "px", height + "px");
    }


    /**
     * Sets the size of the contained component to the specified width and height.
     *
     * @param width  the new width for the component, in pixels.
     * @param height the new height for the component, in pixels.
     */
    public void setComponentSize(String width, String height) {
        component.getStyle()
                .setWidth(width)
                .setHeight(height);
    }


    /**
     * Sets the size of the contained component to the specified width and height.
     *
     * @param width  the new width for the component, in pixels.
     * @param height the new height for the component, in pixels.
     */
    public void setComponentSize(long width, long height) {
        setComponentSize(width + "px", height + "px");
    }

    /**
     * Sets the size of the contained component using JsonValue objects representing
     * the width and height.
     *
     * @param width  a JsonValue representing the new width, in pixels.
     * @param height a JsonValue representing the new height, in pixels.
     */
    public void setComponentSize(JsonValue width, JsonValue height) {
        long widthDouble = Math.round(width.asNumber());
        long heightDouble = Math.round(height.asNumber());
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

    public void setTransparency(boolean transparent){
        if(transparent){
            getStyle().setOpacity("0.5");
        }else{
            getStyle().setOpacity("1");
        }
    }

    public void setToTheFore(boolean toTheFore){
        if(toTheFore){
            getStyle().setZIndex(2);
        }else{
            getStyle().setZIndex(0);
        }
    }
}