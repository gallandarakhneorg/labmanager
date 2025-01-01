package fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component;

import com.vaadin.flow.component.Component;
import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import fr.utbm.ciad.labmanager.utils.localStorage.AbstractLocalStorageItem;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.factory.component.DashBoardComponentFactory;

/**
 * Abstract class defining items describing dashBoard components that can be store locally
 *
 * @author $Author: sgalland$
 * @author $Author: pschneiderlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public abstract class AbstractDashBoardComponentItem extends AbstractLocalStorageItem implements DashBoardComponentItem {

    DashBoardComponentFactory componentFactory;
    private DashBoardComponentType componentType;
    private int index;
    private String width;
    private String height;

    /**
     * Constructor with a default index
     *
     * @param componentFactory the factory to create the component
     */
    public AbstractDashBoardComponentItem(DashBoardComponentFactory<?, ?> componentFactory){
        this(componentFactory, 0);
    }

    /**
     * Constructor
     *
     * @param componentFactory the factory to create the component
     * @param index the index of the component in the dashboard
     */
    public AbstractDashBoardComponentItem(DashBoardComponentFactory<?, ?> componentFactory, int index){
        super(index + "");
        this.componentFactory = componentFactory;
        this.index = index;
    }

    /**
     * Constructor
     *
     * @param componentFactory the factory to create the component
     * @param index the index of the component
     * @param componentType the type of the component
     * @param width the width of the component
     * @param height the height of the component
     */
    public AbstractDashBoardComponentItem(DashBoardComponentFactory<?, ?> componentFactory, int index, DashBoardComponentType componentType, String width, String height){
        this(componentFactory, index);
        this.componentType = componentType;
        this.width = width;
        this.height = height;
    }
    /**
     * Constructor
     *
     * @param componentFactory the factory to create the component
     * @param index the index of the component
     * @param component the existing component to extract size and type
     */
    public AbstractDashBoardComponentItem(DashBoardComponentFactory<?, ?> componentFactory, int index, Component component){
        this(componentFactory, index, DashBoardComponentType.NONE, component.getStyle().get("width"), component.getStyle().get("height"));
    }

    /**
     * Constructor to create a new component from an existing AbstractDashBoardComponentItem.
     *
     * @param componentFactory the factory to create the component
     * @param item the existing component item to copy properties from
     */
    public AbstractDashBoardComponentItem(DashBoardComponentFactory<?, ?> componentFactory, AbstractDashBoardComponentItem item){
        this(componentFactory, item.getIndex(), item.getComponentType(), item.getWidth(), item.getHeight());
    }

    @Override
    public DashBoardComponentType getComponentType(){
        return componentType;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public String getWidth() {
        return width;
    }

    @Override
    public String getHeight() {
        return height;
    }

    @Override
    public void setComponentType(DashBoardComponentType componentType){
        this.componentType = componentType;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void setWidth(String width) {
        this.width = width;
    }

    @Override
    public void setHeight(String height) {
        this.height = height;
    }

    @Override
    public Component createComponent(PublicationService publicationService) {
        return componentFactory.createComponent(publicationService, this);
    }
}
