package fr.utbm.ciad.labmanager.views.components.dashboard;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.dom.Style;
import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.ChartLocalStorageManager;
import fr.utbm.ciad.labmanager.views.components.dashboard.cell.DropCell;
import fr.utbm.ciad.labmanager.views.components.charts.observer.ChartHandler;
import fr.utbm.ciad.labmanager.views.components.dashboard.grid.DropGrid;
import fr.utbm.ciad.labmanager.utils.button.ToggleButton;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component.ComponentType;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component.DashBoardChartItem;
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryBarChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryNightingaleRoseChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryPieChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.layout.PublicationCategoryLayout;
import fr.utbm.ciad.labmanager.views.components.charts.publicationcategory.PublicationCategoryBarChart;
import fr.utbm.ciad.labmanager.views.components.charts.publicationcategory.PublicationCategoryNightingaleRoseChart;
import fr.utbm.ciad.labmanager.views.components.charts.publicationcategory.PublicationCategoryPieChart;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component.InterfaceComponentEnum;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;

public class DashboardLayout extends VerticalLayout {

    private final Select<String> componentSelect = new Select<>();
    private final ToggleButton editionButton;
    private final DropGrid dropGrid = new DropGrid();
    private final List<String> availableComponents = new ArrayList<>(List.of("BarChart", "PieChart", "NightingaleRoseChart"));
    private final PublicationCategoryChartFactory<PublicationCategoryBarChart> barChartFactory;
    private final PublicationCategoryChartFactory<PublicationCategoryPieChart> pieChartFactory;
    private final PublicationCategoryChartFactory<PublicationCategoryNightingaleRoseChart> nightingaleChartFactory;
    private final ChartLocalStorageManager chartLocalStorageManager = new ChartLocalStorageManager();

    /**
     * Constructor that initializes the dashboard layout with publication service.
     *
     * @param publicationService the service for accessing the scientific publications.
     */
    public DashboardLayout(@Autowired PublicationService publicationService) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        super();
        editionButton = new ToggleButton(
                getTranslation("views.edit"),
                this::setEditionMode,
                getTranslation("views.stop_edit"),
                this::removeEditionMode
        );




        barChartFactory = new PublicationCategoryBarChartFactory();
        pieChartFactory = new PublicationCategoryPieChartFactory();
        nightingaleChartFactory = new PublicationCategoryNightingaleRoseChartFactory();
        /*Idle idle = Idle.track(UI.getCurrent(), 30000);

        // Listener to handle user inactivity and exit edition mode if necessary.
        idle.addUserInactiveListener(event -> {
            if (editionButton.isInTrueMode()) {
                removeEditionMode();
            }
        });*/

        // Initializes the select dropdown with components.
        createSelect(publicationService);

        // Add header and drop grid to layout.
        add(createHeader(), dropGrid);

        Button button = new Button("Clear");
        button.addClickListener(event -> WebStorage.clear());
        add(button);

        getWebStorageComponents(publicationService);
    }

    /**
     * Creates and configures the header layout for the dashboard.
     * Sets up layout style and alignment, and adds UI components.
     *
     * @return HorizontalLayout the configured header layout.
     */
    private HorizontalLayout createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.getStyle().setDisplay(Style.Display.GRID);
        header.getStyle().set("grid-template-columns", "repeat(2, 1fr)");

        header.getStyle().setWidth("100%");

        editionButton.getStyle().setWidth("200px");
        editionButton.getStyle().set("grid-column", "2");
        editionButton.getStyle().setMarginLeft("auto");

        header.add(componentSelect, editionButton);
        return header;
    }

    /**
     * Create the Select component used for selecting components.
     * Sets the label, populates items, and defines behavior on component selection.
     *
     * @param publicationService the service used to manage publications.
     */
    private void createSelect(PublicationService publicationService) {
        componentSelect.setLabel(getTranslation("views.select_component"));
        componentSelect.setItems(availableComponents);

        componentSelect.addValueChangeListener(event -> {
            String selectedItem = event.getValue();
            if (selectedItem != null) {

                DraggableComponent draggableComponent = getComponent(publicationService, selectedItem);

                addComponent(draggableComponent);


                componentSelect.clear();
            }
        });

        componentSelect.setVisible(!dropGrid.isEmpty());
        componentSelect.getStyle().set("grid-column", "1");
        componentSelect.getStyle().setMarginRight("auto");
    }

    //TODO the cases have to be the same as in the availableComponents list
    /**
     * Creates and returns a draggable UI component based on the selected item.
     *
     * @param publicationService the service for accessing publication data.
     * @param selectedItem       the type of component to create.
     * @return DraggableComponent a draggable component based on the selected item.
     */
    private DraggableComponent getComponent(PublicationService publicationService, String selectedItem) {
        ComponentType componentType;
        PublicationCategoryLayout<?> chart;
        switch (selectedItem) {
            case "BarChart" -> {
                chart = new PublicationCategoryLayout<>(publicationService, barChartFactory);
                componentType = ComponentType.BAR_CHART;
            }
            case "PieChart" -> {
                chart = new PublicationCategoryLayout<>(publicationService, pieChartFactory);
                componentType = ComponentType.PIE_CHART;
            }
            case "NightingaleRoseChart" -> {
               chart = new PublicationCategoryLayout<>(publicationService, nightingaleChartFactory);
                componentType = ComponentType.NIGHTINGALE_CHART;
            }
            default -> {
                chart = null;
                componentType = ComponentType.NONE;
            }
        }
        initializeChart(chart);

        return new DraggableComponent(chart, componentType);
    }

    private void initializeChart(PublicationCategoryLayout<?> chart){
        if(chart != null){
            chart.setSize("500px", "200px");
            chart.setChartSize("450px", "180px");
            chart.setFormHidden(true);
        }
    }

    private void getWebStorageComponents(PublicationService publicationService) {
        for (DropCell cell : dropGrid.getCells()) {
            DashBoardChartItem item = new DashBoardChartItem(cell.getIndex());

            chartLocalStorageManager.getItem(item.getId(), dashBoardChartItem -> {
                if (dashBoardChartItem != null) {
                    DraggableComponent component;
                    component = new DraggableComponent(dashBoardChartItem.getComponent(publicationService), dashBoardChartItem.getComponentType());
                    addComponent(component, cell);
                }
            });
        }
    }

    private void setupComponent(DraggableComponent component){
        component.getStyle().setBackgroundColor("#f2f2f2");
        component.getStyle().setBorder("#bfbfbf");
        addObserver(component);
        addContextMenuToDraggableComponent(component);
    }

    private void addComponent(DraggableComponent component, DropCell cell){
        setupComponent(component);
        component.setDraggable(false);
        dropGrid.addNewComponent(cell, component);
    }

    private void addComponent(DraggableComponent component){
        setupComponent(component);
        dropGrid.addComponentInFirstEmptyCell(component);
    }

    private void addObserver(DraggableComponent component){
        if(component.getComponent() instanceof PublicationCategoryLayout<?> publicationCategoryLayout){
            ChartHandler chartHandler = new ChartHandler(this::saveComponent, component);
            publicationCategoryLayout.addObserver(chartHandler);
        }
    }

    private void saveComponent(DraggableComponent component){
        component.getParent().ifPresent(parent -> {
            if(parent instanceof DropCell dropCell){
                chartLocalStorageManager.add(new DashBoardChartItem(dropCell.getIndex(), component));
            }
        });
    }

    /**
     * Sets the editing mode and updates the UI to reflect the change.
     */
    private void setEditionMode() {
        changeEditionMode(true);
    }

    /**
     * Removes the edition mode and updates the UI accordingly.
     */
    private void removeEditionMode() {
        changeEditionMode(false);
    }

    private void changeEditionMode(boolean editionMode){
        dropGrid.changeEditionMode(editionMode);
        componentSelect.setVisible(editionMode);
    }

    /**
     * Adds a context menu to a component with the deletion option, and the edit chart option when applicable.
     *
     * @param component The component to which the context menu is added.
     */
    private void addContextMenuToDraggableComponent(DraggableComponent component) {
        component.getContextMenu().addItem(getTranslation("views.charts.edit"), event -> editChart(component), () -> component.isChart() && ((PublicationCategoryLayout<?>) component.getComponent()).isChartGenerated());
        component.getContextMenu().addItem(getTranslation("views.delete"), event -> dropGrid.removeComponent(component), component::isDraggable);
    }

    /**
     * Initiates the editing process for the specified chart component.
     *
     * @param component the draggable component containing the chart to be edited.
     */
    private void editChart(DraggableComponent component) {
        ((PublicationCategoryLayout<?>) component.getComponent()).removeChart();
    }
}