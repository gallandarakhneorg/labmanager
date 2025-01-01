package fr.utbm.ciad.labmanager.views.components.dashboard;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.dom.Style;
import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import fr.utbm.ciad.labmanager.views.components.dashboard.component.DraggableComponent;
import fr.utbm.ciad.labmanager.views.components.dashboard.component.SelectComponent;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.ChartLocalStorageManager;
import fr.utbm.ciad.labmanager.views.components.dashboard.cell.DropCell;
import fr.utbm.ciad.labmanager.views.components.charts.observer.ChartHandler;
import fr.utbm.ciad.labmanager.views.components.dashboard.grid.DropGrid;
import fr.utbm.ciad.labmanager.utils.button.ToggleButton;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component.DashBoardComponentType;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component.DashBoardChartItem;
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryBarChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryNightingaleRoseChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryPieChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.layout.PublicationCategoryLayout;
import fr.utbm.ciad.labmanager.views.components.charts.publicationcategory.PublicationCategoryBarChart;
import fr.utbm.ciad.labmanager.views.components.charts.publicationcategory.PublicationCategoryNightingaleRoseChart;
import fr.utbm.ciad.labmanager.views.components.charts.publicationcategory.PublicationCategoryPieChart;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Implementation a dashboard layout.
 *
 * @author $Author: sgalland$
 * @author $Author: pschneiderlin
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class DashboardLayout extends VerticalLayout {

    private final DropGrid dropGrid = new DropGrid();

    private final Select<String> componentSelect = new Select<>();
    private final List<SelectComponent<DraggableComponent>> availableComponents;

    private final ToggleButton editionButton;

    private final PublicationCategoryChartFactory<PublicationCategoryBarChart> barChartFactory;
    private final PublicationCategoryChartFactory<PublicationCategoryPieChart> pieChartFactory;
    private final PublicationCategoryChartFactory<PublicationCategoryNightingaleRoseChart> nightingaleChartFactory;

    private final ChartLocalStorageManager chartLocalStorageManager = new ChartLocalStorageManager();

    /**
     * Constructor.
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

        availableComponents = List.of(
                new SelectComponent<>(
                        "BarChart",
                        () -> new DraggableComponent(new PublicationCategoryLayout<>(publicationService, barChartFactory), DashBoardComponentType.BAR_CHART),
                        this::initializeChart),
                new SelectComponent<>(
                        "PieChart",
                        () -> new DraggableComponent(new PublicationCategoryLayout<>(publicationService, pieChartFactory), DashBoardComponentType.PIE_CHART),
                        this::initializeChart),
                new SelectComponent<>(
                        "NightingaleChart",
                        () -> new DraggableComponent(new PublicationCategoryLayout<>(publicationService, nightingaleChartFactory), DashBoardComponentType.NIGHTINGALE_CHART),
                        this::initializeChart)
                );

        createSelect();
        add(createHeader(), dropGrid);

        Button button = new Button("Clear");
        button.addClickListener(event -> WebStorage.clear());
        add(button);

        getWebStorageComponents(publicationService);
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

    /**
     * Changes the mode of the dashboard between editing and non-editing states.
     *
     * @param editionMode boolean indicating whether the editing mode should be enabled (true) or disabled (false)
     */
    private void changeEditionMode(boolean editionMode){
        dropGrid.changeEditionMode(editionMode);
        componentSelect.setVisible(editionMode);
    }

    /**
     * Create the Select component used for selecting components.
     * Sets the label, populates items, and defines behavior on component selection.
     */
    private void createSelect() {
        componentSelect.setLabel(getTranslation("views.select_component"));

        componentSelect.setItems(
                availableComponents.stream().map(SelectComponent::getName).toList()
        );

        componentSelect.addValueChangeListener(event -> {
            String selectedItem = event.getValue();
            if (selectedItem != null) {
                availableComponents.stream()
                        .filter(c -> c.getName().equals(selectedItem))
                        .findFirst()
                        .ifPresent(selectComponent -> addComponent(selectComponent.getComponent()));
                componentSelect.clear();
            }
        });

        componentSelect.setVisible(!dropGrid.isEmpty());
        componentSelect.getStyle().set("grid-column", "1");
        componentSelect.getStyle().setMarginRight("auto");
    }

    /**
     * Initializes the properties of the given PublicationCategoryLayout chart.
     *
     * @param chart the chart (PublicationCategoryLayout) to initialize
     */
    private void initializeChart(PublicationCategoryLayout<?> chart){
        if(chart != null){
            chart.setSize("500px", "200px");
            chart.setChartSize("450px", "180px");
            chart.setFormHidden(true);
        }
    }

    /**
     * Initializes the properties of the PublicationCategoryLayout chart contained in the given DraggableComponent.
     *
     * @param component the draggableComponent containing the chart (PublicationCategoryLayout) to initialize
     */
    private void initializeChart(DraggableComponent component){
        if(component.getComponent() instanceof PublicationCategoryLayout<?> chart){
            initializeChart(chart);
        }
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
     * Retrieves the components stored in the web storage and adds them to the grid.
     *
     * @param publicationService the publication service used to create the chart components
     */
    private void getWebStorageComponents(PublicationService publicationService) {
        for (DropCell cell : dropGrid.getCells()) {
            DashBoardChartItem item = new DashBoardChartItem(cell.getIndex());

            chartLocalStorageManager.getItem(item.getId(), dashBoardChartItem -> {
                if (dashBoardChartItem != null) {
                    DraggableComponent component;
                    component = new DraggableComponent(dashBoardChartItem.createComponent(publicationService), dashBoardChartItem.getComponentType());
                    addComponent(component, cell);
                }
            });
        }
    }

    /**
     * Sets up the visual style and behavior of the given DraggableComponent.
     *
     * @param component the DraggableComponent to be set up
     */
    private void setupComponent(DraggableComponent component){
        component.getStyle().setBackgroundColor("#f2f2f2");
        component.getStyle().setBorder("#bfbfbf");
        addObservertoChartComponent(component);
        addContextMenuToDraggableComponent(component);
    }

    /**
     * Adds a DraggableComponent to a specific DropCell in the drop grid.
     * This method sets up the component's visual style, disables its draggable functionality,
     * and then adds it to the specified cell in the drop grid.
     *
     * @param component the DraggableComponent to be added
     * @param cell the DropCell where the component will be placed
     */
    private void addComponent(DraggableComponent component, DropCell cell){
        setupComponent(component);
        component.setDraggable(false);
        dropGrid.addNewComponent(cell, component);
    }

    /**
     * Adds a DraggableComponent in the first empty cell in the drop grid.
     * This method sets up the component's visual style, disables its draggable functionality,
     * and then adds it in the first empty cell in the drop grid.
     *
     * @param component the DraggableComponent to be added
     */
    private void addComponent(DraggableComponent component){
        setupComponent(component);
        dropGrid.addComponentInFirstEmptyCell(component);
    }

    /**
     * Adds an observer to the PublicationCategoryLayout stored in the given DraggableComponent
     * (if it contains a component of this type, otherwise nothing happen).
     *
     * @param component the DraggableComponent to which the observer will be added
     */
    private void addObservertoChartComponent(DraggableComponent component){
        if(component.getComponent() instanceof PublicationCategoryLayout<?> publicationCategoryLayout){
            ChartHandler chartHandler = new ChartHandler(this::saveComponent, component);
            publicationCategoryLayout.addObserver(chartHandler);
        }
    }

    /**
     * Saves the DraggableComponent by storing its data in local storage.
     *
     * @param component the DraggableComponent to be saved
     */
    private void saveComponent(DraggableComponent component){
        component.getParent().ifPresent(parent -> {
            if(parent instanceof DropCell dropCell){
                chartLocalStorageManager.add(new DashBoardChartItem(dropCell.getIndex(), component));
            }
        });
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