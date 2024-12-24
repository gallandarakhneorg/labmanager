package fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component;

import com.vaadin.flow.component.Component;
import fr.utbm.ciad.labmanager.views.components.dashboard.DraggableComponent;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.factory.component.DashBoardComponentFactory;

/**
 * Class defining items describing dashBoard components that can be store locally
 *
 * @author $Author: sgalland$
 * @author $Author: pschneiderlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class DashBoardComponentItem extends AbstractDashBoardComponentItem {

    /**
     * Default Constructor
     */
    public DashBoardComponentItem(){
        super(new DashBoardComponentFactory());
    }

    /**
     * Constructor
     *
     * @param index the index of the component in the dashboard
     */
    public DashBoardComponentItem(int index){
        super(new DashBoardComponentFactory(), index);
    }

    /**
     * Constructor
     *
     * @param index the index of the component
     * @param componentType the type of the component
     * @param width the width of the component
     * @param height the height of the component
     */
    public DashBoardComponentItem(int index, DashBoardComponentType componentType, String width, String height){
        super(new DashBoardComponentFactory(), index, componentType, width, height);
    }

    /**
     * Constructor to initialize the item from attributes of a component.
     *
     * @param index the index of the component
     * @param component the existing component to extract attributes
     */
    public DashBoardComponentItem(int index, Component component){
        super(new DashBoardComponentFactory(), index, component);
    }

    /**
     * Constructor to initialize the item from attributes of a component currently stored into a DraggableComponent
     *
     * @param index the index of the component
     * @param component the draggable component from which to extract the size and type
     */
    public DashBoardComponentItem(int index, DraggableComponent component){
        this(index, component.getComponent());
    }

    /**
     * Constructor to create a new DashBoardComponentItem from an existing one.
     *
     * @param item the existing DashBoardComponentItem to copy properties from
     */
    public DashBoardComponentItem(DashBoardComponentItem item){
        super(new DashBoardComponentFactory(), item);
    }

    @Override
    public void setId(String id){
        super.setId("DashBoardComponent_" + id);
    }
}
