package com.scandit.shelf.javaapp.ui.storeselection;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.scandit.shelf.catalog.Store;

public class StoreItemViewModel {

    private final MutableLiveData<Store> selectedStoreLiveData;

    private Store item = null;

    MutableLiveData<String> _storeName = new MutableLiveData<>();
    LiveData<String> storeName = _storeName;

    public StoreItemViewModel(MutableLiveData<Store> selectedStoreLiveData) {
        this.selectedStoreLiveData = selectedStoreLiveData;
    }

    public void bind(Store store) {
        item = store;
        _storeName.postValue(store.getName());
    }

    public void onClick() {
        if (item != null) {
            selectedStoreLiveData.postValue(item);
        }
    }
}
