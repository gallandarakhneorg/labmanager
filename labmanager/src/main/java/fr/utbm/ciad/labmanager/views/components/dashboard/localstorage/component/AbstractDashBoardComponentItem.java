package fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component;

import com.vaadin.flow.component.Component;
import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import fr.utbm.ciad.labmanager.views.components.dashboard.DraggableComponent;
import fr.utbm.ciad.labmanager.utils.localStorage.AbstractLocalStorageItem;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.factory.component.InterfaceDashBoardComponentFactory;

public abstract class AbstractDashBoardComponentItem extends AbstractLocalStorageItem implements InterfaceDashBoardComponentItem {

    InterfaceDashBoardComponentFactory componentFactory;

    private ComponentType componentType;
    private final int index;
    private String width;
    private String height;

    public AbstractDashBoardComponentItem(InterfaceDashBoardComponentFactory<?> componentFactory){
        this(componentFactory, 0);
    }

    public AbstractDashBoardComponentItem(InterfaceDashBoardComponentFactory<?> componentFactory,
                                          int index){
        super(index + "");
        this.componentFactory = componentFactory;
        this.index = index;
    }

    public AbstractDashBoardComponentItem(InterfaceDashBoardComponentFactory<?> componentFactory,
                                          int index, ComponentType componentType, String width, String height){
        this(componentFactory, index);
        this.componentType = componentType;
        this.width = width;
        this.height = height;
    }

    public AbstractDashBoardComponentItem(InterfaceDashBoardComponentFactory<?> componentFactory,
                                          int index, Component component){
        this(componentFactory, index, ComponentType.NONE, component.getStyle().get("width"), component.getStyle().get("height"));
    }

    public AbstractDashBoardComponentItem(InterfaceDashBoardComponentFactory<?> componentFactory,
                                          int index, DraggableComponent component){
        this(componentFactory, index, component.getComponent());
        this.componentType = component.getComponentType();
    }

    public AbstractDashBoardComponentItem(InterfaceDashBoardComponentFactory<?> componentFactory, AbstractDashBoardComponentItem item){
        this(componentFactory, item.getIndex(), item.getComponentType(), item.getWidth(), item.getHeight());
    }

    @Override
    public Component getComponent(PublicationService publicationService) {
        return componentFactory.createComponent(publicationService, this);
    }

    public ComponentType getComponentType(){
        return componentType;
    }

    public int getIndex() {
        return index;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }

    public void setComponentType(ComponentType componentType){
        this.componentType = componentType;
    }
}
