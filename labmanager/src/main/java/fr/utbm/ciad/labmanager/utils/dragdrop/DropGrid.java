package fr.utbm.ciad.labmanager.utils.dragdrop;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import elemental.json.JsonArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DropGrid extends FlexLayout {

    private static final int CELL_SIZE = 50;
    List<DropCell> cells = new ArrayList<>();

    private boolean isDragging = false; // State of the dragging operation
    private final Map<Component, double[]> componentSizes = new HashMap<>();
    private double screenWidth = 0;
    private double screenHeight = 0;
    private double previousWidth = 0;
    private double previousHeight = 0;


    /**
     * Constructor for DropGrid.
     */
    public DropGrid() {
        setFlexDirection(FlexDirection.ROW);
        setWidth("100%");
        getStyle().set("height", "100vh")
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fit, minmax(50px, 1fr))")
                .set("grid-auto-rows", CELL_SIZE + "px")
                .set("gap", "0px")
                .set("background-color", "rgba(255, 255, 255, 0.5)");

        UI.getCurrent().getPage().executeJs("return window.innerWidth;")
                .then(width -> {
                    UI.getCurrent().getPage().executeJs("return window.innerHeight;").then(height -> {
                        previousHeight = height.asNumber();
                        previousWidth = width.asNumber();
                    });
                });

        createCells();

        UI.getCurrent().getPage().executeJs(
                "window.addEventListener('resize', function() {" +
                        "$0.server.onResize();" +
                        "});", getElement()
        );
    }

    @ClientCallable
    public void onResize() {
        UI.getCurrent().getPage().executeJs("return window.innerWidth;")
                        .then(width -> {
                            UI.getCurrent().getPage().executeJs("return window.innerHeight;").then(height -> {
                                double currentWidth = width.asNumber();
                                double currentHeight = height.asNumber();

                                double widthRatio = currentWidth / previousWidth;
                                double heightRatio = currentHeight / previousHeight;

                                resizeAndRepositionComponent(widthRatio, heightRatio);

                                previousHeight = currentHeight;
                                previousWidth = currentWidth;
                            });
                        });
    }

    private void resizeAndRepositionComponent(double widthRatio, double heightRatio) {
        componentSizes.forEach((component, size) -> {
            double originalWidth = size[0];
            double originalHeight = size[1];

            double newWidth = originalWidth * widthRatio;
            double newHeight = originalHeight * heightRatio;

            String currentLeftStr = component.getElement().getStyle().get("left");
            String currentTopStr = component.getElement().getStyle().get("top");

            double currentLeft = (currentLeftStr != null && !currentLeftStr.isEmpty()) ?
                    Double.parseDouble(currentLeftStr.replace("px", "")) : 0;
            double currentTop = (currentTopStr != null && !currentTopStr.isEmpty()) ?
                    Double.parseDouble(currentTopStr.replace("px", "")) : 0;

            double newLeft = currentLeft * widthRatio;
            double newTop = currentTop * heightRatio;

            component.getElement().getStyle().set("width", newWidth + "px");
            component.getElement().getStyle().set("height", newHeight + "px");
            component.getElement().getStyle().set("left", newLeft + "px");
            component.getElement().getStyle().set("top", newTop + "px");
        });
    }
    /**
     * Create the cells of the grid based on the grid size
     */
    private void createCells() {
        UI.getCurrent().getPage().executeJs("return window.innerWidth;").then(width -> {
            UI.getCurrent().getPage().executeJs("return window.innerHeight;").then(height -> {
                screenWidth = width.asNumber();
                screenHeight = height.asNumber();

                int columns = Math.max(1, (int) (screenWidth / CELL_SIZE));
                int rows = Math.max(1, (int) (screenHeight / CELL_SIZE));
                int cellCount = columns * rows;

                cells.clear();
                removeAll();

                for (int i = 0; i < cellCount; i++) {
                    DropCell cell = new DropCell(this, i);
                    addCell(cell);
                }

                getStyle().set("grid-template-columns", "repeat(" + columns + ", 1fr");
            });
        });
    }

    public void addComponentWidthInitialSize(Component component) {
        add(component);

        component.getElement().executeJs(
                "const rect = this.getBoundingClientRect(); return rect.width;"
        ).then(width -> {
            component.getElement().executeJs("const rect = this.getBoundingClientRect(); return rect.height;")
                    .then(height -> {
                       double a = width.asNumber();
                       double b = height.asNumber();
                       componentSizes.put(component, new double[]{a, b});
                    });
        });
    }

    /**
     * Adds a cell to the grid and updates the internal list of cells.
     *
     * @param cell the DropCell instance to add to the grid layout.
     */
    private void addCell(DropCell cell) {
        add(cell);
        cells.add(cell);
    }

    /**
     * Sets the dragging state of the grid.
     *
     * @param dragging true to enable dragging, false to disable.
     */
    public void setDragging(boolean dragging) {
        this.isDragging = dragging;
    }

    /**
     * Displays the grid borders depending on the event calling the method (triggering edit mode or dragging a component)
     *
     * @param dragging true if the event is dragging, false otherwise
     */
    public void showBorders(boolean dragging) {
        if(dragging){
            getStyle().set("border", "1px dashed #bfbfbf");
        }else{
            getStyle().set("border", "1px dashed #f2f2f2");
        }
        cells.forEach(cell -> {
            if (cell != null) {
                cell.showBorders(dragging);
            }
        });
    }

    /**
     * Hide the cell borders depending on the event calling the method (triggering edit mode or dragging a component)
     *
     * @param dragging true if the event is dragging, false otherwise
     */
    public void hideBorders(boolean dragging) {
        if(dragging){
            getStyle().set("border", "1px dashed #f2f2f2");
        }else{
            getStyle().set("border", "none");
        }
        cells.forEach(cell -> {
            if (cell != null) {
                cell.hideBorders(dragging);
            }
        });
    }

    /**
     * Checks if the grid is empty.
     * The grid is considered empty if none of its cells contain components.
     *
     * @return true if the grid is empty, false otherwise.
     */
    public boolean isEmpty() {
        return getChildren()
                .filter(cell -> cell instanceof VerticalLayout)
                .allMatch(cell -> cell.getChildren().findAny().isEmpty());
    }

    /**
     * Finds the first empty cell in the grid.
     * This method identifies the first cell that does not currently contain any components.
     *
     * @return a DropCell representing the first empty cell, or null if no empty cells are found.
     */
    public DropCell findFirstEmptyCell() {
        for (DropCell cell : cells) {
            if (cell.isEmpty()) {
                return cell;
            }
        }
        return null;
    }

    /**
     * Adds a component to the first empty cell in the grid.
     *
     * @param component the component to be added to an empty cell.
     */
    public void addComponentInFirstEmptyCell(Component component) {
        DropCell cell = findFirstEmptyCell();
        if (cell != null) {
            cell.addComponent(component);
        }
    }

    /**
     * Retrieves a list of cells that contain at least one component.
     *
     * @return a list of DropCell objects containing components.
     */
    public List<DropCell> getCellsContainingComponents() {
        List<DropCell> cellsList = new ArrayList<>();
        for (DropCell cell : cells) {
            if (!cell.isEmpty()) {
                cellsList.add(cell);
            }
        }
        return cellsList;
    }

    /**
     * Toggles the edition mode
     *
     * @param editionMode the new edition mode
     */
    public void changeEditionMode(boolean editionMode) {
        if(editionMode){
            showBorders(false);
        }else{
            hideBorders(false);
        }
        getCellsContainingComponents().forEach(cell -> cell.changeEditionMode(editionMode));
    }

    /**
     * Gets the current dragging state.
     *
     * @return true if dragging is active, false otherwise.
     */
    public boolean isDragging() {
        return isDragging;
    }
}

