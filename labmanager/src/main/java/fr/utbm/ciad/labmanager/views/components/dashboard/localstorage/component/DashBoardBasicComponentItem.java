package fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component;

import com.vaadin.flow.component.Component;
import fr.utbm.ciad.labmanager.views.components.dashboard.component.DraggableComponent;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.factory.component.DashBoardBasicComponentFactory;

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
public class DashBoardBasicComponentItem extends AbstractDashBoardComponentItem {

    /**
     * Default Constructor
     */
    public DashBoardBasicComponentItem(){
        super(new DashBoardBasicComponentFactory());
    }

    /**
     * Constructor
     *
     * @param index the index of the component in the dashboard
     */
    public DashBoardBasicComponentItem(int index){
        super(new DashBoardBasicComponentFactory(), index);
    }

    /**
     * Constructor
     *
     * @param index the index of the component
     * @param componentType the type of the component
     * @param width the width of the component
     * @param height the height of the component
     */
    public DashBoardBasicComponentItem(int index, DashBoardComponentType componentType, String width, String height){
        super(new DashBoardBasicComponentFactory(), index, componentType, width, height);
    }

    /**
     * Constructor to initialize the item from attributes of a component.
     *
     * @param index the index of the component
     * @param component the existing component to extract attributes
     */
    public DashBoardBasicComponentItem(int index, Component component){
        super(new DashBoardBasicComponentFactory(), index, component);
    }

    /**
     * Constructor to initialize the item from attributes of a component currently stored into a DraggableComponent
     *
     * @param index the index of the component
     * @param component the draggable component from which to extract the size and type
     */
    public DashBoardBasicComponentItem(int index, DraggableComponent component){
        this(index, component.getComponent());
    }

    /**
     * Constructor to create a new DashBoardComponentItem from an existing one.
     *
     * @param item the existing DashBoardComponentItem to copy properties from
     */
    public DashBoardBasicComponentItem(DashBoardBasicComponentItem item){
        super(new DashBoardBasicComponentFactory(), item);
    }

    @Override
    public void setId(String id){
        super.setId("DashBoardComponent_" + id);
    }
}
