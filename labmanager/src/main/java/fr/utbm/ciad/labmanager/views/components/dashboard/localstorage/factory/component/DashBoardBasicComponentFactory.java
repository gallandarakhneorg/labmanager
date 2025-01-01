package fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.factory.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component.DashBoardBasicComponentItem;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component.DashBoardComponentType;

import java.util.Objects;

/**
 * Factory class for creating Components based on a DashBoardComponentItem.
 * Provides the logic for creating specific types of components based on the component type.
 *
 * @author $Author: sgalland$
 * @author $Author: pschneiderlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class DashBoardBasicComponentFactory implements DashBoardComponentFactory<DashBoardBasicComponentItem, Void> {

    @Override
    public Component createComponent(Void service, DashBoardBasicComponentItem dashBoardBasicComponentItem) {
        Component component;
        if (Objects.requireNonNull(dashBoardBasicComponentItem.getComponentType()) == DashBoardComponentType.NONE) {
            component = new FlexLayout();
        } else {
            component = null;
        }

        if(component != null){
            component.getStyle()
                    .setWidth(dashBoardBasicComponentItem.getWidth())
                    .setHeight(dashBoardBasicComponentItem.getHeight());
        }
        return component;
    }
}

