package fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component;
import com.vaadin.flow.component.Component;
import fr.utbm.ciad.labmanager.views.components.dashboard.DraggableComponent;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.factory.component.DashBoardChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.layout.PublicationCategoryLayout;

import java.util.Set;

/**
 * Class defining items describing dashBoard chart components that can be store locally
 *
 * @author $Author: sgalland$
 * @author $Author: pschneiderlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class DashBoardChartItem extends AbstractDashBoardComponentItem {

    private Set<String> multiSelectComboBoxItems;
    private Integer yearRangeStartValue;
    private Integer yearRangeEndValue;
    private boolean chartGenerated;

    /**
     * Default Constructor
     */
    public DashBoardChartItem(){
        super(new DashBoardChartFactory());
    }

    /**
     * Constructor
     *
     * @param index the index of the component in the dashboard
     */
    public DashBoardChartItem(int index){
        super(new DashBoardChartFactory(), index);
    }

    /**
     * Constructor
     *
     * @param index the index of the component
     * @param componentType the type of the component
     * @param width the width of the component
     * @param height the height of the component
     */
    public DashBoardChartItem(int index, DashBoardComponentType componentType, String width, String height){
        super(new DashBoardChartFactory(), index, componentType, width, height);
    }

    /**
     * Constructor to initialize the item from attributes of a component.
     *
     * @param index the index of the component
     * @param component the existing component to extract properties from
     */
    public DashBoardChartItem(int index, Component component){
        super(new DashBoardChartFactory(), index, component);
        if(component instanceof PublicationCategoryLayout<?> publicationCategoryLayout) {
            multiSelectComboBoxItems = publicationCategoryLayout.getMultiSelectComboBoxItems();
            yearRangeStartValue = publicationCategoryLayout.getYearRangeStartValue();
            yearRangeEndValue = publicationCategoryLayout.getYearRangeEndValue();
            chartGenerated = publicationCategoryLayout.isChartGenerated();
        }
    }

    /**
     * Constructor to initialize the item from attributes of a component currently stored into a DraggableComponent
     *
     * @param index the index of the component
     * @param component the draggable component from which to extract size and type
     */
    public DashBoardChartItem(int index, DraggableComponent component){
        this(index, component.getComponent());
        setComponentType(component.getComponentType());
    }

    /**
     * Constructor to create a new DashBoardComponentItem from an existing one.
     *
     * @param item the existing DashBoardComponentItem to copy properties from
     */
    public DashBoardChartItem(DashBoardChartItem item){
        super(new DashBoardChartFactory(), item);
        this.multiSelectComboBoxItems = item.getMultiSelectComboBoxItems();
        this.yearRangeStartValue = item.getYearRangeStartValue();
        this.yearRangeEndValue = item.getYearRangeEndValue();
        this.chartGenerated = item.isChartGenerated();
    }

    @Override
    public void setId(String id){
        super.setId("DashBoardChart_" + id);
    }

    /**
     * Returns the start year value for the chart's range.
     *
     * @return the start year value for the chart's range
     */
    public Integer getYearRangeStartValue() {
        return yearRangeStartValue;
    }

    /**
     * Returns the end year value for the chart's range.
     *
     * @return the end year value for the chart's range
     */
    public Integer getYearRangeEndValue() {
        return yearRangeEndValue;
    }

    /**
     * Returns the set of multi-select combo box items for the chart.
     *
     * @return the set of multi-select combo box items
     */
    public Set<String> getMultiSelectComboBoxItems() {
        return multiSelectComboBoxItems;
    }

    /**
     * Returns whether the chart has been generated.
     *
     * @return true if the chart is generated, false otherwise
     */
    public boolean isChartGenerated() {
        return chartGenerated;
    }
}

