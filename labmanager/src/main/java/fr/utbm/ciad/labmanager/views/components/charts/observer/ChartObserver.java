package fr.utbm.ciad.labmanager.views.components.charts.observer;

/**
 * Interface for observing chart-related events.
 * Implementing classes will define actions to take when a chart is generated.
 *
 * @author $Author: sgalland$
 * @author $Author: pschneiderlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public interface ChartObserver {
    /**
     * Called when a chart is successfully generated.
     * Specify the behavior triggered by the chart generation event.
     */
    void onChartGenerated();
}
