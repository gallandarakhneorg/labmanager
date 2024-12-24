package fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.factory.component;

import com.vaadin.flow.component.orderedlayout.FlexLayout;
import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component.DashBoardChartItem;
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryBarChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryNightingaleRoseChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryPieChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.layout.PublicationCategoryLayout;
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
public class DashBoardChartFactory implements InterfaceDashBoardComponentFactory<DashBoardChartItem> {

    public PublicationCategoryLayout<?> createComponent(PublicationService publicationService, DashBoardChartItem dashBoardChartItem) {
        PublicationCategoryChartFactory factory = null;
        switch (dashBoardChartItem.getComponentType()) {
            case ComponentType.BAR_CHART -> factory = new PublicationCategoryBarChartFactory();
            case ComponentType.PIE_CHART -> factory = new PublicationCategoryPieChartFactory();
            case ComponentType.NIGHTINGALE_CHART -> factory = new PublicationCategoryNightingaleRoseChartFactory();
            default ->  new FlexLayout();
        }
        PublicationCategoryLayout component = new PublicationCategoryLayout<>(publicationService,
                factory,
                dashBoardChartItem.getMultiSelectComboBoxItems(),
                dashBoardChartItem.getYearRangeStartValue(),
                dashBoardChartItem.getYearRangeEndValue(),
                dashBoardChartItem.isChartGenerated());

        component.getStyle()
                .setWidth(dashBoardChartItem.getWidth())
                .setHeight(dashBoardChartItem.getHeight());

        return component;
    }
}

