package fr.utbm.ciad.labmanager.views.components.charts.observer;

import fr.utbm.ciad.labmanager.views.components.dashboard.component.DraggableComponent;

import java.util.function.Consumer;

/**
 * Handles chart-related events and executes specified actions when a chart is generated.
 *
 * @author $Author: sgalland$
 * @author $Author: pschneiderlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class ChartHandler implements ChartObserver {

    private Consumer<DraggableComponent> callback;
    private DraggableComponent component;

    /**
     * Default Constructor
     */
    public ChartHandler(){
    }

    /**
     * Constructor
     *
     * @param callback  the action to perform when the chart is generated
     * @param component the draggable component associated with the chart
     */
    public ChartHandler(Consumer<DraggableComponent> callback, DraggableComponent component){
        this.callback = callback;
        this.component = component;
    }

    @Override
    public void onChartGenerated() {
        if(callback != null && component != null){
            callback.accept(component);
        }
    }
}
