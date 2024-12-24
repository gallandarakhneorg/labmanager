package fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.factory.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component.DashBoardComponentItem;
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
public class DashBoardComponentFactory implements InterfaceDashBoardComponentFactory<DashBoardComponentItem> {

    @Override
    public Component createComponent(PublicationService publicationService, DashBoardComponentItem dashBoardComponentItem) {
        Component component;
        if (Objects.requireNonNull(dashBoardComponentItem.getComponentType()) == DashBoardComponentType.NONE) {
            component = new FlexLayout();
        } else {
            component = null;
        }

        if(component != null){
            component.getStyle()
                    .setWidth(dashBoardComponentItem.getWidth())
                    .setHeight(dashBoardComponentItem.getHeight());
        }
        return component;
    }
}

