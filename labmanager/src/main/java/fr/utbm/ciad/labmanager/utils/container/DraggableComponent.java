package fr.utbm.ciad.labmanager.utils.container;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.dom.Style;
import elemental.json.JsonValue;
import fr.utbm.ciad.labmanager.utils.contextMenu.ConditionalManagedContextMenu;
import fr.utbm.ciad.labmanager.utils.contextMenu.ContextMenuFactory;
import fr.utbm.ciad.labmanager.views.components.charts.layout.PublicationCategoryLayout;

/**
 * A component providing the drag-and-drop functionality, with support for conditional context menus.
 *
 * @author $Author: sgalland$
 * @author $Author: pschneiderlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class DraggableComponent extends ComponentContainerWithContextMenu {

    private final DragSource<Component> dragSource;

    /**
     * Default Constructor
     */
    public DraggableComponent(){
        this(new FlexLayout());
    }

    /**
     * Constructor
     *
     * @param component the component to be managed and made draggable.
     */
    public DraggableComponent(Component component) {
        super(component, ContextMenuFactory.MenuType.CONDITIONAL);

        dragSource = DragSource.create(this);
        setDraggable(true);
        setListeners();
    }

    /**
     * Adds a new component to the container, applying additional constraints if the component is a chart.
     *
     * @param component the new component to add.
     */
    @Override
    public void setComponent(Component component) {
        if (isEmpty()) {
            super.setComponent(component);
            if (isChart()) {
                getStyle().setMinHeight("100px");
                getStyle().setMinWidth("200px");
            }
        }
    }

    @Override
    protected void setComponentStyle(){
        super.setComponentStyle();
        getComponent().getStyle().setBoxShadow("0 4px 8px #00000033");
        getComponent().getStyle().setBorderRadius("8px");
    }

    @Override
    protected void setStyle(){
        super.setStyle();
        getStyle().setBorderRadius("8px");
        getStyle().setOverflow(Style.Overflow.HIDDEN);
        getStyle().setPosition(Style.Position.ABSOLUTE);
    }

    /**
     * Sets the size of the contained component and adjusts the size of child elements if the component is a chart.
     *
     * @param width  the new width for the component as a JsonValue.
     * @param height the new height for the component as a JsonValue.
     */
    @Override
    public void setComponentSize(JsonValue width, JsonValue height) {
        super.setComponentSize(width, height);
        if (isChart()) {
            ((PublicationCategoryLayout<?>) getComponent()).setSize(
                    Math.round(width.asNumber() * 0.9) + "px",
                    Math.round(height.asNumber() * 0.9) + "px");
        }
    }

    /**
     * Sets event listeners for the component.
     */
    private void setListeners(){
        getElement().addEventListener("mouseenter", event -> {
            if(isDraggable()){
                getComponent().getStyle().set("border", "2px solid #c4c5ff");
            }
        });
        getElement().addEventListener("mouseleave", event ->
                getComponent().getStyle().set("border", "none"));
    }

    /**
     * Retrieves the drag source associated with this component.
     *
     * @return the DragSource instance managing the drag-and-drop behavior.
     */
    public DragSource<Component> getDragSource() {
        return dragSource;
    }

    /**
     * Sets whether the component is draggable.
     *
     * @param isDraggable true to make the component draggable; false otherwise.
     */
    public void setDraggable(boolean isDraggable){
        dragSource.setDraggable(isDraggable);
        if(isDraggable){
            getStyle().set("resize", "both");
        }else{
            getStyle().set("resize", "none");
        }
    }

    /**
     * Checks whether the component is currently draggable.
     *
     * @return true if the component is draggable; false otherwise.
     */
    public boolean isDraggable(){
        return dragSource.isDraggable();
    }

    /**
     * Determines whether the contained component is a chart.
     *
     * @return true if the component is an instance of PublicationCategoryLayout; false otherwise.
     */
    public boolean isChart() {
        return getComponent() instanceof PublicationCategoryLayout<?>;
    }

    /**
     * Retrieves the context menu associated with this component, ensuring it is a ConditionalManagedContextMenu.
     *
     * @return the associated ConditionalManagedContextMenu instance.
     */
    @Override
    public ConditionalManagedContextMenu getContextMenu() {
        return (ConditionalManagedContextMenu) super.getContextMenu();
    }
}