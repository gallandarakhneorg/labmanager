package fr.utbm.ciad.labmanager.views.components.dashboard.localstorage;

import fr.utbm.ciad.labmanager.utils.localStorage.AbstractLocalStorageManager;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component.DashBoardBasicComponentItem;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.factory.item.DashBoardBasicComponentItemFactory;

/**
 * Class Managing the local storage for DashBoardComponentItem objects.
 *
 * @author $Author: sgalland$
 * @author $Author: pschneiderlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class ComponentLocalStorageManager extends AbstractLocalStorageManager<DashBoardBasicComponentItem> {

    /**
     * Default Constructor
     */
    public ComponentLocalStorageManager(){
        super(new DashBoardBasicComponentItemFactory());
    }
}
