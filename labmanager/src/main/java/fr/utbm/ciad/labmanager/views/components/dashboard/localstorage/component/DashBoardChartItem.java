package fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component;
import com.vaadin.flow.component.Component;
import fr.utbm.ciad.labmanager.views.components.dashboard.DraggableComponent;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.factory.component.DashBoardChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.layout.PublicationCategoryLayout;

import java.util.Set;

public class DashBoardChartItem extends AbstractDashBoardComponentItem {

    private Set<String> multiSelectComboBoxItems;
    private Integer yearRangeStartValue;
    private Integer yearRangeEndValue;
    private boolean chartGenerated;

    public DashBoardChartItem(){
        super(new DashBoardChartFactory());
    }

    public DashBoardChartItem(int index){
        super(new DashBoardChartFactory(), index);
    }

    public DashBoardChartItem(int index, ComponentType componentType, String width, String height){
        super(new DashBoardChartFactory(), index, componentType, width, height);
    }

    public DashBoardChartItem(int index, Component component){
        super(new DashBoardChartFactory(), index, component);
        if(component instanceof PublicationCategoryLayout<?> publicationCategoryLayout) {
            multiSelectComboBoxItems = publicationCategoryLayout.getMultiSelectComboBoxItems();
            yearRangeStartValue = publicationCategoryLayout.getYearRangeStartValue();
            yearRangeEndValue = publicationCategoryLayout.getYearRangeEndValue();
            chartGenerated = publicationCategoryLayout.isChartGenerated();
        }
    }

    public DashBoardChartItem(int index, DraggableComponent component){
        this(index, component.getComponent());
        setComponentType(component.getComponentType());
    }

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

    public Integer getYearRangeStartValue() {
        return yearRangeStartValue;
    }

    public Integer getYearRangeEndValue() {
        return yearRangeEndValue;
    }

    public Set<String> getMultiSelectComboBoxItems() {
        return multiSelectComboBoxItems;
    }

    public boolean isChartGenerated() {
        return chartGenerated;
    }

    @Override
    public String toString(){
        return "multiSelectComboBoxItems : " + multiSelectComboBoxItems +
                "yearRangeStartValue : " + yearRangeStartValue +
                "yearRangeEndValue : " + yearRangeEndValue +
                "chartGenerated : " + chartGenerated;
    }
}

