package fr.utbm.ciad.labmanager.utils.localStorage;

public abstract class AbstractLocalStorageItem implements LocalStorageItem {

    private String id;

    public AbstractLocalStorageItem(String id){
        setId(id);
    }

    @Override
    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }
}
