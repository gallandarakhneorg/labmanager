package fr.utbm.ciad.labmanager.views.components.dashboard.localstorage;

import fr.utbm.ciad.labmanager.utils.localStorage.AbstractLocalStorageManager;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component.DashBoardComponentItem;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.factory.item.DashBoardComponentItemFactory;


public class ComponentLocalStorageManager extends AbstractLocalStorageManager<DashBoardComponentItem> {
    public ComponentLocalStorageManager(){
        super(new DashBoardComponentItemFactory());
    }

}
