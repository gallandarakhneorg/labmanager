package fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.factory.component;

import com.vaadin.flow.component.Component;
import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component.InterfaceDashBoardComponentItem;

public interface InterfaceDashBoardComponentFactory<T extends InterfaceDashBoardComponentItem> {

    Component createComponent(
            PublicationService publicationService,
            T localStorageItem);
}
