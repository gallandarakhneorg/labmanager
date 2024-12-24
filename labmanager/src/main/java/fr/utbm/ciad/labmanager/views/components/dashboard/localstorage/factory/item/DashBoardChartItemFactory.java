package fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.factory.item;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component.DashBoardChartItem;
import fr.utbm.ciad.labmanager.utils.localStorage.factory.LocalStorageItemFactory;

/**
 * Factory class for creating DashBoardChartItem instances from a string representation (e.g., JSON).
 * Provides the logic for deserializing a JSON string into a DashBoardChartItem object.
 *
 * @author $Author: sgalland$
 * @author $Author: pschneiderlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class DashBoardChartItemFactory implements LocalStorageItemFactory<DashBoardChartItem> {

    @Override
    public DashBoardChartItem createItem(String jsonValue) {
        if(jsonValue == null){
            return new DashBoardChartItem();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        DashBoardChartItem item;
        try {
            item = new DashBoardChartItem(objectMapper.readValue(jsonValue, DashBoardChartItem.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new DashBoardChartItem(item);
    }
}
