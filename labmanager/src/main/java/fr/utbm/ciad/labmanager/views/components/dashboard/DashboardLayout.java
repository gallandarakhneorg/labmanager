package fr.utbm.ciad.labmanager.views.components.dashboard;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.dom.Style;
import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import fr.utbm.ciad.labmanager.utils.dragdrop.DraggableComponent;
import fr.utbm.ciad.labmanager.utils.dragdrop.DropCell;
import fr.utbm.ciad.labmanager.utils.dragdrop.DropGrid;
import fr.utbm.ciad.labmanager.utils.dragdrop.ToggleButton;
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
import org.vaadin.addons.idle.Idle;

import java.util.*;

public class DashboardLayout extends VerticalLayout {

    private final Select<String> componentSelect = new Select<>();
    private final ToggleButton editionButton = new ToggleButton(this::setEditionMode, this::removeEditionMode);
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
        barChartFactory = new PublicationCategoryBarChartFactory();
        pieChartFactory = new PublicationCategoryPieChartFactory();
        nightingaleChartFactory = new PublicationCategoryNightingaleRoseChartFactory();
        Idle idle = Idle.track(UI.getCurrent(), 30000);

        // Listener to handle user inactivity and exit edition mode if necessary.
        idle.addUserInactiveListener(event -> {
            if (editionButton.isInTrueMode()) {
                removeEditionMode();
            }
        });
        updateSelectItems();

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
                addContextMenuToComponent(draggableComponent);

                draggableComponent.getElement().setAttribute("data-custom-id", selectedItem);

                dropGrid.addComponentInFirstEmptyCell(draggableComponent);

                availableComponents.remove(selectedItem);
                updateSelectItems();
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
    @NotNull
    private DraggableComponent getDraggableComponent(PublicationService publicationService, String selectedItem) {
        Component component = switch (selectedItem) {
            case "BarChart" -> new PublicationCategoryLayout<>(publicationService, barChartFactory);
            case "PieChart" -> new PublicationCategoryLayout<>(publicationService, pieChartFactory);
            case "NightingaleRoseChart" -> new PublicationCategoryLayout<>(publicationService, nightingaleChartFactory);
            default -> null;
        };
        if (component == null) {
            throw new IllegalArgumentException("Unknown component type: " + selectedItem);
        }
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

    /**
     * Updates the items in the component select dropdown.
     * Enables or disables the dropdown based on the availability of components.
     */
    private void updateSelectItems() {
        componentSelect.setItems(availableComponents);
        componentSelect.setEnabled(!availableComponents.isEmpty());
    }

    /**
     * Sets the editing mode and updates the UI to reflect the change.
     */
    private void setEditionMode() {
        dropGrid.changeEditionMode(true);
        dropGrid.getCellsContainingComponents().forEach(cell -> cell.getChild().ifPresent(component -> addContextMenuToComponent((DraggableComponent) component)));
        componentSelect.setVisible(true);
    }

    /**
     * Removes the edition mode and updates the UI accordingly.
     */
    private void removeEditionMode() {
        dropGrid.changeEditionMode(false);
        componentSelect.setVisible(false);
    }

    /**
     * Adds a context menu to a component with the deletion option, and the edit chart option when applicable.
     *
     * @param component The component to which the context menu is added.
     */
    private void addContextMenuToComponent(DraggableComponent component) {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setTarget(component);
        contextMenu.addItem(getTranslation("views.delete"), event -> removeComponent(component));
        if (component.isChart()) {
            contextMenu.addItem(getTranslation("views.charts.edit"), event -> editChart(component));
        }
    }

    /**
     * Removes a specified component from the parent layout and updates the dropdown.
     *
     * @param component The component to be removed.
     */
    private void removeComponent(DraggableComponent component) {
        component.getParent().ifPresent(parent -> {
            String componentId = component.getElement().getAttribute("data-custom-id");
            if (componentId != null && !availableComponents.contains(componentId)) {
                availableComponents.add(componentId);
            }
            updateSelectItems();
            dropGrid.removeComponent((DropCell) parent, component);
        });
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
