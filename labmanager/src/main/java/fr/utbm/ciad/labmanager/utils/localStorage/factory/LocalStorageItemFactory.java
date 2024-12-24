package fr.utbm.ciad.labmanager.utils.localStorage.factory;

import fr.utbm.ciad.labmanager.utils.localStorage.LocalStorageItem;

public interface LocalStorageItemFactory<T extends LocalStorageItem> {
    T createItem(String item);
}
