package fr.utbm.ciad.labmanager.utils.localStorage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.page.WebStorage;
import fr.utbm.ciad.labmanager.utils.localStorage.factory.LocalStorageItemFactory;

import java.util.function.Consumer;

public abstract class AbstractLocalStorageManager<T extends LocalStorageItem> {

    LocalStorageItemFactory<T> factory;

    public AbstractLocalStorageManager(LocalStorageItemFactory<T> factory){
        this.factory = factory;
    }

    public void getItem(String id, Consumer<T> onComplete) {
        WebStorage.getItem(id, jsonValue -> {
            T newItem = null;
            if (jsonValue != null) {
                newItem = factory.createItem(jsonValue);
            }
            onComplete.accept(newItem);
        });
    }

    public void add(T item){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(item);
            WebStorage.setItem(item.getId(), json);
        }catch(JsonProcessingException e){
            e.printStackTrace();
        }
    }

    public void remove(T item){
        remove(item.getId());
    }

    public void remove(String id){
        WebStorage.removeItem(id);
    }

}
