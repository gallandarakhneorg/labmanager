package fr.utbm.ciad.labmanager.views.components.charts.observer;

import fr.utbm.ciad.labmanager.views.components.dashboard.DraggableComponent;

import java.util.function.Consumer;

public class ChartHandler implements ChartObserver {
    private Consumer<DraggableComponent> callback;
    private DraggableComponent component;

    public ChartHandler(){
    }

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
