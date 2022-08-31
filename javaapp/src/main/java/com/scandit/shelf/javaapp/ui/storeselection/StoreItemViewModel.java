package com.scandit.shelf.javaapp.ui.storeselection;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.scandit.shelf.sdk.catalog.Store;

/**
 * A ViewModel that exposes the currently selected Store to Activity/Fragment
 */
public class StoreItemViewModel {
    // Communicates with Fragment when Store selection in stores list changes.
    private final MutableLiveData<Store> selectedStoreLiveData;

    // Refers to the current Store item bound through StoresAdapter
    private Store item = null;

    MutableLiveData<String> _storeName = new MutableLiveData<>();
    // Observes Store item binding in StoresAdapter
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
