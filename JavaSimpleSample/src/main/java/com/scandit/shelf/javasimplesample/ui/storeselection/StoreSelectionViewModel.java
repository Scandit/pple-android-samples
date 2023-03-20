/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scandit.shelf.javasimplesample.ui.storeselection;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scandit.shelf.javasimplesample.catalog.CatalogStore;
import com.scandit.shelf.sdk.catalog.Catalog;
import com.scandit.shelf.sdk.catalog.ProductCatalog;
import com.scandit.shelf.sdk.catalog.Store;
import com.scandit.shelf.sdk.common.CompletionHandler;

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

    private final MutableLiveData<Boolean> isRefreshingLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> snackbarMessageLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Store>> storeListLiveData = new MutableLiveData<>(allStores);
    private final MutableLiveData<Store> storeLiveData = new MutableLiveData<>();

    public LiveData<Boolean> isRefreshing() {
        return isRefreshingLiveData;
    }

    public LiveData<String> getSnackbarMessage() {
        return snackbarMessageLiveData;
    }

    public LiveData<List<Store>> getStoreList() {
        return storeListLiveData;
    }

    // Posts the Store for which Product catalog is updated with a call to refreshProducts().
    public LiveData<Store> getStore() {
        return storeLiveData;
    }

    public void initStoreSelection() {
        // Initialize CatalogStore with a null ProductCatalog
        CatalogStore.getInstance().setProductCatalog(null);
        // Set initial values to the LiveData that communicate with StoreSelectionFragment
        snackbarMessageLiveData.setValue(null);
        allStores.clear();
        storeListLiveData.setValue(allStores);
        storeLiveData.setValue(null);
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
        isRefreshingLiveData.postValue(isRefreshing);
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
                snackbarMessageLiveData.postValue("Fetching the list of stores failed");
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
        storeListLiveData.postValue(filteredStores);
    }

    private boolean containsIgnoringCase(String s1, String s2) {
        return s1.toLowerCase(Locale.getDefault()).contains(s2.toLowerCase(Locale.getDefault()));
    }

    private void getProducts(Store store) {
        // Get/Update the Product items for a given Store.

        // First create the ProductCatalog object.
        //
        // If you are using the ShelfView backend as your product catalog provider, you only need to specify the Store,
        // for which you will perform the Price Check - just like in the code below.
        //
        // If on the other hand, you would like to use a different source of data for the ProductCatalog,
        // you should should pass your custom implementation of the ProductProvider interface, as the second argument
        // for the Catalog.getProductCatalog method - check the docs for more details.
        ProductCatalog catalog = Catalog.getProductCatalog(store);
        // Store the ProductCatalog object to CatalogStore. We will need it for price check in PriceCheckViewModel.
        CatalogStore.getInstance().setProductCatalog(catalog);
        // Now we can update the list of Products by calling update() on ProductCatalog.
        catalog.update(
                new CompletionHandler<Unit>() {
                    @Override
                    public void success(Unit result) {
                        // Product catalog was fetched and stored successfully. Update UI accordingly.
                        snackbarMessageLiveData.postValue(
                                "Updating the Product Catalog for store " + store.getName()
                                        + " (id=" + store.getId() + ") ready"
                        );
                        setRefreshing(false);
                        storeLiveData.postValue(store);
                    }

                    @Override
                    public void failure(@NonNull Exception error) {
                        // Catalog fetch failed. Communicate the error with Fragment through LiveData.
                        snackbarMessageLiveData.postValue(
                                "Updating the Product Catalog for store with id=" + store.getId() + " failed"
                        );
                        setRefreshing(false);
                    }
                });
    }
}
