package fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.factory.item;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component.DashBoardBasicComponentItem;
import fr.utbm.ciad.labmanager.utils.localStorage.factory.LocalStorageItemFactory;

/**
 * Factory class for creating DashBoardComponentItem instances from a string representation (e.g., JSON).
 * Provides the logic for deserializing a JSON string into a DashBoardComponentItem object.
 *
 * @author $Author: sgalland$
 * @author $Author: pschneiderlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class DashBoardBasicComponentItemFactory implements LocalStorageItemFactory<DashBoardBasicComponentItem> {

    @Override
    public DashBoardBasicComponentItem createItem(String jsonValue) {
        if(jsonValue == null){
            return new DashBoardBasicComponentItem();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        DashBoardBasicComponentItem item;
        try {
            item = new DashBoardBasicComponentItem(objectMapper.readValue(jsonValue, DashBoardBasicComponentItem.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new DashBoardBasicComponentItem(item);
    }
}
