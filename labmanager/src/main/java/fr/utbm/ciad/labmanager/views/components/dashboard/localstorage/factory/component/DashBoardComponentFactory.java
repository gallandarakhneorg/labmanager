package fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.factory.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component.DashBoardComponentItem;
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryBarChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryNightingaleRoseChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryPieChartFactory;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component.ComponentType;

/**
 * The factory of a ContextMenu
 *
 * @author $Author: sgalland$
 * @author $Author: pschneiderlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class DashBoardComponentFactory implements InterfaceDashBoardComponentFactory<DashBoardComponentItem> {


    public Component createComponent(PublicationService publicationService, DashBoardComponentItem dashBoardComponentItem) {
        PublicationCategoryChartFactory factory = null;
        switch (dashBoardComponentItem.getComponentType()) {
            case ComponentType.BAR_CHART -> factory = new PublicationCategoryBarChartFactory();
            case ComponentType.PIE_CHART -> factory = new PublicationCategoryPieChartFactory();
            case ComponentType.NIGHTINGALE_CHART -> factory = new PublicationCategoryNightingaleRoseChartFactory();
            default ->  new FlexLayout();
        }
        Component component = new FlexLayout();

        component.getStyle()
                .setWidth(dashBoardComponentItem.getWidth())
                .setHeight(dashBoardComponentItem.getHeight());
        return component;
    }
}

