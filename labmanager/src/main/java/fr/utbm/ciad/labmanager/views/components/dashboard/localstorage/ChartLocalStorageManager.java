package fr.utbm.ciad.labmanager.views.components.dashboard.localstorage;

import fr.utbm.ciad.labmanager.utils.localStorage.AbstractLocalStorageManager;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component.DashBoardChartItem;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.factory.item.DashBoardChartItemFactory;

/**
 * Class Managing the local storage for DashBoardChartItem objects.
 *
 * @author $Author: sgalland$
 * @author $Author: pschneiderlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class ChartLocalStorageManager extends AbstractLocalStorageManager<DashBoardChartItem> {

    /**
     * Default Constructor
     */
    public ChartLocalStorageManager(){
        super(new DashBoardChartItemFactory());
    }
}
