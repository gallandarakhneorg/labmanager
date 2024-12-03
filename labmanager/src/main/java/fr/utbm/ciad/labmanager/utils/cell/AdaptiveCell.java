package fr.utbm.ciad.labmanager.utils.cell;

import com.vaadin.flow.component.Component;

/**
 * An abstract extension of AbstractCell that supports resizing of contained components.
 * Provides additional functionality for adjusting the size of child components proportionally
 * to the size of the cell.
 *
 * @author $Author: sgalland$
 * @author $Author: pschneiderlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public abstract class AdaptiveCell extends AbstractCell {

    /**
     * Default Constructor
     */
    public AdaptiveCell(){
        super();
    }

    /**
     * Constructor with a default cell's border
     *
     * @param index the index of the cell.
     */
    public AdaptiveCell(int index){
        super(index);
    }

    /**
     * Constructor
     *
     * @param index       the index of the cell.
     * @param borderColor the color of the cell's border.
     */
    public AdaptiveCell(int index, String borderColor){
        super(index, borderColor);
    }

    /**
     * Retrieves the width of the cell as a reference dimension for resizing.
     *
     * @return the width of the cell in pixels.
     */
    private int getCellSide() {
        return getComponentSide(this, "width");
    }

    /**
     * Retrieves the size of a specified side (width or height) of the given component.
     *
     * @param component the component whose size is being queried.
     * @param side      the side to measure ("width" or "height").
     * @return the size of the specified side in pixels.
     */
    private int getComponentSide(Component component, String side) {
        return Integer.parseInt(component.getStyle().get(side).replace("px", ""));
    }

    /**
     * Resizes the contained component proportionally to the given scaling factor based on the cell's dimensions.
     *
     * @param side the scaling factor to apply to the component's dimensions.
     */
    public void resizeComponent(double side) {
        if (!isEmpty()) {
            getChild().ifPresent(component -> {
                long newWidth = Math.round(getComponentSide(component, "width") * side / getCellSide());
                long newHeight = Math.round(getComponentSide(component, "height") * side / getCellSide());

                component.getElement().getStyle().setWidth(newWidth + "px");
                component.getElement().getStyle().setHeight(newHeight + "px");
            });
        }
    }
}