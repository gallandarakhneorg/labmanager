package fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component;

import com.vaadin.flow.component.Component;
import fr.utbm.ciad.labmanager.services.publication.PublicationService;

public interface InterfaceDashBoardComponentItem {
    Component getComponent(PublicationService publicationService);

    ComponentType getComponentType();

    int getIndex();

    String getWidth();

    String getHeight();

    void setComponentType(ComponentType componentType);
}
