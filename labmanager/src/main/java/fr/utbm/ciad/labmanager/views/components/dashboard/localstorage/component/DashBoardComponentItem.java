package fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component;

import com.vaadin.flow.component.Component;
import fr.utbm.ciad.labmanager.views.components.dashboard.DraggableComponent;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.factory.component.DashBoardComponentFactory;

public class DashBoardComponentItem extends AbstractDashBoardComponentItem {

    public DashBoardComponentItem(){
        super(new DashBoardComponentFactory());
    }

    public DashBoardComponentItem(int index){
        super(new DashBoardComponentFactory(), index);
    }

    public DashBoardComponentItem(int index, ComponentType componentType, String width, String height){
        super(new DashBoardComponentFactory(), index, componentType, width, height);
    }

    public DashBoardComponentItem(int index, Component component){
        super(new DashBoardComponentFactory(), index, component);
    }

    public DashBoardComponentItem(int index, DraggableComponent component){
        this(index, component.getComponent());
    }

    public DashBoardComponentItem(DashBoardComponentItem item){
        super(new DashBoardComponentFactory(), item);
    }

    @Override
    public void setId(String id){
        super.setId("DashBoardComponent_" + id);
    }
}
