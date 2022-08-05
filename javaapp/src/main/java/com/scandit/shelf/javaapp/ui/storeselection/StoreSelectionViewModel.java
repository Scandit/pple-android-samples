package com.scandit.shelf.javaapp.ui.storeselection;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scandit.shelf.catalog.Catalog;
import com.scandit.shelf.catalog.ProductCatalog;
import com.scandit.shelf.common.CompletionHandler;
import com.scandit.shelf.catalog.Store;
import com.scandit.shelf.javaapp.catalog.CatalogStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kotlin.Unit;

public class StoreSelectionViewModel extends ViewModel {

    private final List<Store> allStores = new ArrayList<>();
    private String searchText = "";

    private final MutableLiveData<Boolean> _isRefreshingLiveData = new MutableLiveData<>();
    LiveData<Boolean> isRefreshingLiveData = _isRefreshingLiveData;

    private final MutableLiveData<String> _snackbarMessageLiveData = new MutableLiveData<>();
    LiveData<String> snackbarMessageLiveData = _snackbarMessageLiveData;

    private final MutableLiveData<List<Store>> _storeListLiveData = new MutableLiveData<>(allStores);
    LiveData<List<Store>> storeListLiveData = _storeListLiveData;

    private final MutableLiveData<Store> _storeLiveData = new MutableLiveData<>();
    LiveData<Store> storeLiveData = _storeLiveData;

    public void initStoreSelection() {
        CatalogStore.getInstance().setProductCatalog(null);
        _snackbarMessageLiveData.setValue(null);
        allStores.clear();
        _storeListLiveData.setValue(allStores);
        _storeLiveData.setValue(null);
    }

    public void refreshStores() {
        setRefreshing(true);
        getStores();
    }

    public void refreshProducts(Store store) {
        setRefreshing(true);
        getProducts(store);
    }

    public void onSearchTextChanged(String text) {
        searchText = text;
        emitStores();
    }

    private void setRefreshing(boolean isRefreshing) {
        _isRefreshingLiveData.postValue(isRefreshing);
    }

    private void getStores() {
        Catalog.getStores(new CompletionHandler<List<Store>>() {
            @Override
            public void success(List<Store> result) {
                allStores.clear();
                allStores.addAll(result);
                emitStores();
                setRefreshing(false);
            }

            @Override
            public void failure(@NonNull Exception error) {
                _snackbarMessageLiveData.postValue("Fetching the list of stores failed");
                setRefreshing(false);
            }
        });
    }

    private void emitStores() {
        List<Store> filteredStores = new ArrayList<>();
        for (Store s : allStores) {
            if (containsIgnoringCase(s.getName(), searchText)) {
                filteredStores.add(s);
            }
        }
        _storeListLiveData.postValue(filteredStores);
    }

    private boolean containsIgnoringCase(String s1, String s2) {
        return s1.toLowerCase(Locale.getDefault()).contains(s2.toLowerCase(Locale.getDefault()));
    }

    private void getProducts(Store store) {
        ProductCatalog catalog = Catalog.getProductCatalog(store);
        CatalogStore.getInstance().setProductCatalog(catalog);
        catalog.update(
                new CompletionHandler<Unit>() {
                    @Override
                    public void success(Unit result) {
                        _snackbarMessageLiveData.postValue(
                                "Updating the Product Catalog for store " + store.getName()
                                        + " (id=" + store.getId() + ") ready"
                        );
                        setRefreshing(false);
                        _storeLiveData.postValue(store);
                    }

                    @Override
                    public void failure(@NonNull Exception error) {
                        _snackbarMessageLiveData.postValue(
                                "Updating the Product Catalog for store with id=" + store.getId() + " failed"
                        );
                        setRefreshing(false);
                    }
                });
    }
}
