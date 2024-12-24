package fr.utbm.ciad.labmanager.views.components.dashboard.localstorage;

import fr.utbm.ciad.labmanager.utils.localStorage.AbstractLocalStorageManager;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component.DashBoardChartItem;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.factory.item.DashBoardChartItemFactory;


public class ChartLocalStorageManager extends AbstractLocalStorageManager<DashBoardChartItem> {
    public ChartLocalStorageManager(){
        super(new DashBoardChartItemFactory());
    }

}
