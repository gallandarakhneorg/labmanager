package fr.utbm.ciad.labmanager.utils.grid;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import elemental.json.JsonValue;
import fr.utbm.ciad.labmanager.utils.cell.AdaptiveCell;
import fr.utbm.ciad.labmanager.utils.cell.DropCell;
import fr.utbm.ciad.labmanager.utils.container.ComponentContainer;

/**
 * An adaptive implementation of AbstractGrid that adjusts its layout dynamically based on the browser window size.
 * Provides additional functionality for resizing components and responding to browser events.
 *
 * @author $Author: sgalland$
 * @author $Author: pschneiderlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public abstract class AdaptiveGrid extends AbstractGrid {

    private boolean isResizing = false;

    /**
     * Default Constructor
     */
    public AdaptiveGrid(){
        super();
    }

    /**
     * Constructor
     *
     * @param rows        the number of rows in the grid.
     * @param columns     the number of columns in the grid.
     * @param borderColor the border color of the cells.
     */
    public AdaptiveGrid(int rows,
                        int columns,
                        String borderColor) {
        super(rows, columns, borderColor);
    }

    @Override
    public void initializeGrid(){
        super.initializeGrid();

        UI.getCurrent().getPage().executeJs("return window.innerWidth;")
                .then(this::setGridSize);

        setupListeners();
    }

    @Override
    public void addComponentInFirstEmptyCell(Component component) {
        if(component instanceof ComponentContainer){
            setupComponentListeners((ComponentContainer) component);
        }
        super.addComponentInFirstEmptyCell(component);
    }

    /**
     * Sets the grid size based on a given width.
     *
     * @param width the width to which the grid will be resized as a JsonValue.
     */
    protected void setGridSize(JsonValue width){
        double doubleWidth =  width.asNumber();
        setGridSize((int) doubleWidth);
    }

    /**
     * Sets the grid size based on a given width.
     *
     * @param width the width to which the grid will be resized in pixels.
     */
    protected void setGridSize(int width){
        setCellSize((double) width / getNumCols());
        getStyle().setWidth(width*getNumCols() + "px");
        getStyle().setWidth(width*getNumRows() + "px");
        for (AdaptiveCell cell : getCells()) {
            cell.resizeComponent(getCellSize());
            cell.getStyle()
                    .setWidth(getCellSize() + "px")
                    .setHeight(getCellSize() + "px");
        }
    }

    /**
     * Sets up listeners to handle browser window resize events.
     */
    private void setupListeners(){
        UI.getCurrent().getPage().addBrowserWindowResizeListener(event -> setGridSize(event.getWidth()));
    }

    /**
     * Sets up listeners for component-specific behaviors.
     *
     * @param component the component container to add listeners to.
     */
    protected void setupComponentListeners(ComponentContainer component){
        addResizeListener(component);
    }

    /**
     * Attaches a resize listener to a container, observing size changes and reacting to them.
     *
     * @param component the component container to observe.
     */
    private void addResizeListener(ComponentContainer component) {
        isResizing = false;
        String jsCode =
                "const resizeObserver = new ResizeObserver(entries => {" +
                        "   for (let entry of entries) {" +
                        "       entry.target.dispatchEvent(new Event('custom-resize')); " +
                        "   }" +
                        "});" +
                        "resizeObserver.observe(this);";

        component.getElement().executeJs(jsCode);

        component.getElement().addEventListener("custom-resize",event ->
                component.adaptComponentSize(() -> afterChangingComponentSize(component)));
    }

    /**
     * Abstract method to handle logic after a container's size has been changed.
     *
     * @param component the component that was resized.
     */
    protected abstract void afterChangingComponentSize(Component component);

    /**
     * Determines if a component can be placed in a given cell based on grid boundaries.
     *
     * @param component the component to check.
     * @param cell      the target cell.
     * @return true if the component can fit in the cell, false otherwise.
     */
    protected boolean canBePlaced(ComponentContainer component, DropCell cell) {
        return !isOverTheEdge(cell, component);
    }

    /**
     * Checks if placing a component in a cell would cause it to exceed the grid's boundaries.
     *
     * @param targetCell      the target cell.
     * @param component the component to check.
     * @return true if the component would exceed the grid's boundaries in this cell, false otherwise.
     */
    protected boolean isOverTheEdge(DropCell targetCell, ComponentContainer component) {
        return targetCell.getIndex() % getNumCols() + calculateComponentSizeInCells(component, "width") > getNumCols();
    }

    /**
     * Calculates the size of a component in terms of grid cells.
     *
     * @param component the component whose size is being calculated.
     * @param side      the dimension to calculate ("width" or "height").
     * @return the size of the component in grid cells.
     */
    protected int calculateComponentSizeInCells(ComponentContainer component, String side) {
        if (component != null) {
            int componentSide = Integer.parseInt(component.getComponent().getStyle().get(side).replace("px", ""));
            return Math.round((float) componentSide / (float) getCellSize());
        }
        return 0;
    }

    /**
     * Checks if component are currently being resized in the grid.
     *
     * @return true if resizing is in progress, false otherwise.
     */
    public boolean isResizing(){
        return isResizing;
    }

    /**
     * Sets the resizing state of the grid.
     *
     * @param isResizing true to indicate resizing is in progress, false otherwise.
     */
    public void setResizing(boolean isResizing){
        this.isResizing = isResizing;
    }
}