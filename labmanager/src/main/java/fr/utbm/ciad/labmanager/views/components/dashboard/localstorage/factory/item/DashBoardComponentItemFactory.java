package fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.factory.item;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.utbm.ciad.labmanager.views.components.dashboard.localstorage.component.DashBoardComponentItem;
import fr.utbm.ciad.labmanager.utils.localStorage.factory.LocalStorageItemFactory;

public class DashBoardComponentItemFactory implements LocalStorageItemFactory<DashBoardComponentItem> {

    @Override
    public DashBoardComponentItem createItem(String jsonValue) {
        if(jsonValue == null){
            return new DashBoardComponentItem();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        DashBoardComponentItem item;
        try {
            item = new DashBoardComponentItem(objectMapper.readValue(jsonValue, DashBoardComponentItem.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new DashBoardComponentItem(item);
    }
}
