package fr.utbm.ciad.labmanager.views.components.charts.layout;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Abstract implementation of the layout for displaying charts.
 *
 * @author $Author: sgalland$
 * @author $Author: erenon$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public abstract class AbstractChartLayout extends VerticalLayout implements ChartLayout {

    /**
     * Constructor.
     */
    public AbstractChartLayout() {
        setSizeFull();
        setHeight(1000, Unit.PIXELS);
    }

    /**
     * Refreshes the chart by deleting it and creating it with the same values
     */
    public abstract void refreshChart();
}
