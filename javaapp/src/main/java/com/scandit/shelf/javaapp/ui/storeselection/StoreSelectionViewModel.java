package com.scandit.shelf.javaapp.ui.storeselection;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scandit.shelf.sdk.catalog.ProductCatalog;
import com.scandit.shelf.sdk.catalog.Store;
import com.scandit.shelf.sdk.common.CompletionHandler;
import com.scandit.shelf.javaapp.catalog.CatalogStore;
import com.scandit.shelf.sdk.catalog.Catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kotlin.Unit;

/**
 * ViewModel class for
 * - fetching the list of all Stores,
 * - allowing user to select a Store,
 * - fetching the ProductCatalog (which is essentially a collection of data about Products) for the selected Store.
 */
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
    // Posts the Store for which Product catalog is updated with a call to refreshProducts().
    LiveData<Store> storeLiveData = _storeLiveData;

    public void initStoreSelection() {
        // Initialize CatalogStore with a null ProductCatalog
        CatalogStore.getInstance().setProductCatalog(null);
        // Set initial values to the LiveData that communicate with StoreSelectionFragment
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
        // Get/Update the list of stores by using the Catalog singleton object of the PPLE SDK.
        // Pass a CompletionHandler to the getStores method for handling API result.
        Catalog.getStores(new CompletionHandler<List<Store>>() {
            @Override
            public void success(List<Store> result) {
                // Stores list fetching was successful. Keep reference to the stores and notify the UI.
                allStores.clear();
                allStores.addAll(result);
                emitStores();
                setRefreshing(false);
            }

            @Override
            public void failure(@NonNull Exception error) {
                // The API call to update Stores has failed. Communicate the error with Fragment.
                _snackbarMessageLiveData.postValue("Fetching the list of stores failed");
                setRefreshing(false);
            }
        });
    }

    private void emitStores() {
        // Filter the in-memory list of Stores by searchText
        List<Store> filteredStores = new ArrayList<>();
        for (Store s : allStores) {
            if (containsIgnoringCase(s.getName(), searchText)) {
                filteredStores.add(s);
            }
        }
        // Post the filtered stores list to Fragment
        _storeListLiveData.postValue(filteredStores);
    }

    private boolean containsIgnoringCase(String s1, String s2) {
        return s1.toLowerCase(Locale.getDefault()).contains(s2.toLowerCase(Locale.getDefault()));
    }

    private void getProducts(Store store) {
        // Get/Update the Product items for a given Store.
        // First get the ProductCatalog object with the Catalog singleton object of the PPLE SDK.
        ProductCatalog catalog = Catalog.getProductCatalog(store);
        // Store the ProductCatalog object to CatalogStore. We will need it for price check in PriceCheckViewModel.
        CatalogStore.getInstance().setProductCatalog(catalog);
        // Now we can update the list of Products by calling update() on ProductCatalog.
        catalog.update(
                new CompletionHandler<Unit>() {
                    @Override
                    public void success(Unit result) {
                        // Product catalog was fetched and stored successfully. Update UI accordingly.
                        _snackbarMessageLiveData.postValue(
                                "Updating the Product Catalog for store " + store.getName()
                                        + " (id=" + store.getId() + ") ready"
                        );
                        setRefreshing(false);
                        _storeLiveData.postValue(store);
                    }

                    @Override
                    public void failure(@NonNull Exception error) {
                        // Catalog fetch failed. Communicate the error with Fragment through LiveData.
                        _snackbarMessageLiveData.postValue(
                                "Updating the Product Catalog for store with id=" + store.getId() + " failed"
                        );
                        setRefreshing(false);
                    }
                });
    }
}
