package fr.utbm.ciad.labmanager.views.components.dashboard;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.dom.Style;
import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import fr.utbm.ciad.labmanager.utils.container.DraggableComponent;
import fr.utbm.ciad.labmanager.utils.grid.DropGrid;
import fr.utbm.ciad.labmanager.utils.button.ToggleButton;
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryBarChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryNightingaleRoseChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryPieChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.layout.PublicationCategoryLayout;
import fr.utbm.ciad.labmanager.views.components.charts.publicationcategory.PublicationCategoryBarChart;
import fr.utbm.ciad.labmanager.views.components.charts.publicationcategory.PublicationCategoryNightingaleRoseChart;
import fr.utbm.ciad.labmanager.views.components.charts.publicationcategory.PublicationCategoryPieChart;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class DashboardLayout extends VerticalLayout {

    private final Select<String> componentSelect = new Select<>();
    private final ToggleButton editionButton;
    private final DropGrid dropGrid = new DropGrid();
    private final List<String> availableComponents = new ArrayList<>(List.of("BarChart", "PieChart", "NightingaleRoseChart"));
    private final PublicationCategoryChartFactory<PublicationCategoryBarChart> barChartFactory;
    private final PublicationCategoryChartFactory<PublicationCategoryPieChart> pieChartFactory;
    private final PublicationCategoryChartFactory<PublicationCategoryNightingaleRoseChart> nightingaleChartFactory;

    /**
     * Constructor that initializes the dashboard layout with publication service.
     *
     * @param publicationService the service for accessing the scientific publications.
     */
    public DashboardLayout(@Autowired PublicationService publicationService) {
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

                DraggableComponent draggableComponent = getDraggableComponent(publicationService, selectedItem);
                addContextMenuToDraggableComponent(draggableComponent);

                dropGrid.addComponentInFirstEmptyCell(draggableComponent);

                componentSelect.clear();
            }
        });

        componentSelect.setVisible(!dropGrid.isEmpty());
        componentSelect.getStyle().set("grid-column", "1");
        componentSelect.getStyle().setMarginRight("auto");
    }

    /**
     * Creates and returns a draggable UI component based on the selected item.
     *
     * @param publicationService the service for accessing publication data.
     * @param selectedItem       the type of component to create.
     * @return DraggableComponent a draggable component based on the selected item.
     */
    @NotNull
    private DraggableComponent getDraggableComponent(PublicationService publicationService, String selectedItem) {
        Component component = getComponent(publicationService, selectedItem);
        if (component instanceof PublicationCategoryLayout<?>) {
            ((PublicationCategoryLayout<?>) component).setWidth("500px");
            ((PublicationCategoryLayout<?>) component).setHeight("200px");
            ((PublicationCategoryLayout<?>) component).setSize("450px", "180px");
            ((PublicationCategoryLayout<?>) component).setFormHidden(true);
        }
        component.getStyle().setBackgroundColor("#f2f2f2");
        component.getStyle().setBorder("#bfbfbf");
        return new DraggableComponent(component);
    }

    //TODO the cases have to be the same as in the availableComponents list
    @NotNull
    private Component getComponent(PublicationService publicationService, String selectedItem) {
        Component component = switch (selectedItem) {
            case "BarChart" -> new PublicationCategoryLayout<>(publicationService, barChartFactory);
            case "PieChart" -> new PublicationCategoryLayout<>(publicationService, pieChartFactory);
            case "NightingaleRoseChart" -> new PublicationCategoryLayout<>(publicationService, nightingaleChartFactory);
            default -> null;
        };
        if (component == null) {
            throw new IllegalArgumentException("Unknown component type: " + selectedItem);
        }
        return component;
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
        //TODO Remove Edit Chart button when there is no chart
        ((PublicationCategoryLayout<?>) component.getComponent()).removeChart();
    }
}