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

package com.scandit.shelf.kotlinadvancedoverlaysample.ui.storeselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scandit.shelf.kotlinadvancedoverlaysample.catalog.CatalogStore
import com.scandit.shelf.sdk.catalog.Catalog
import com.scandit.shelf.sdk.catalog.Store
import com.scandit.shelf.sdk.common.CompletionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel class for
 * - fetching the list of all Stores,
 * - allowing user to select a Store,
 * - fetching the ProductCatalog (which is essentially a collection of data about Products) for the selected Store.
 */
class StoreSelectionViewModel : ViewModel() {

    private val allStores = mutableListOf<Store>()
    private var searchText = ""

    val isRefreshingFlow = MutableStateFlow(false)
    val snackbarMessageFlow = MutableStateFlow<String?>(null)
    val storeListFlow = MutableStateFlow<List<Store>>(emptyList())
    // Emits the Store for which Product catalog is updated with a call to refreshProducts().
    val storeFlow = MutableStateFlow<Store?>(null)

    fun initStoreSelection() {
        // Initialize CatalogStore with a null ProductCatalog
        CatalogStore.productCatalog = null
        // Emit initial values from the Flow that communicate with StoreSelectionFragment
        snackbarMessageFlow.tryEmit(null)
        allStores.clear()
        storeListFlow.tryEmit(allStores)
        storeFlow.tryEmit(null)
    }

    fun refreshStores() {
        viewModelScope.launch {
            setRefreshing(true)
            getStores()
        }
    }

    fun refreshProducts(store: Store) {
        viewModelScope.launch {
            setRefreshing(true)
            getProducts(store)
        }
    }

    fun onSearchTextChanged(text: String) {
        searchText = text
        emitStores()
    }

    private fun setRefreshing(isRefreshing: Boolean) {
        isRefreshingFlow.tryEmit(isRefreshing)
    }

    private fun getStores() {
        // Get/Update the list of stores by using the Catalog singleton object of the PPLE SDK.
        // Pass a CompletionHandler to the getStores method for handling API result.
        Catalog.getStores(
            object : CompletionHandler<List<Store>> {
                override fun success(result: List<Store>) {
                    // Stores list fetching was successful. Keep reference to the stores and notify the UI.
                    allStores.clear()
                    allStores.addAll(result)
                    emitStores()
                    setRefreshing(false)
                }

                override fun failure(error: Exception) {
                    // The API call to update Stores has failed. Communicate the error with Fragment.
                    snackbarMessageFlow.tryEmit("Fetching the list of stores failed")
                    setRefreshing(false)
                }
            }
        )
    }

    private fun emitStores() {
        // Filter the in-memory list of Stores by searchText and emit the filtered list.
        storeListFlow.tryEmit(allStores.filter { it.name.contains(searchText, true) })
    }

    private fun getProducts(store: Store) {
        // Get/Update the Product items for a given Store.

        // First create the ProductCatalog object.
        //
        // If you are using the ShelfView backend as your product catalog provider, you only need to specify the Store,
        // for which you will perform the Price Check - just like in the code below.
        //
        // If on the other hand, you would like to use a different source of data for the ProductCatalog,
        // you should should pass your custom implementation of the ProductProvider interface, as the second argument
        // for the Catalog.getProductCatalog method - check the docs for more details.
        Catalog.getProductCatalog(store).apply {
            // Store the ProductCatalog object to CatalogStore. We will need it for price check in PriceCheckViewModel.
            CatalogStore.productCatalog = this
            // Now we can update the list of Products by calling update() on ProductCatalog.
            update(
                object : CompletionHandler<Unit> {
                    override fun success(result: Unit) {
                        // Product catalog was fetched and stored successfully. Update UI accordingly.
                        snackbarMessageFlow.tryEmit(
                            "Updating the Product Catalog for store ${store.name} (id=${store.id}) ready"
                        )
                        setRefreshing(false)
                        storeFlow.tryEmit(store)
                    }

                    override fun failure(error: Exception) {
                        // Catalog fetch failed. Communicate the error with Fragment by emitting to the Flow.
                        snackbarMessageFlow.tryEmit("Updating the Product Catalog for store with id=${store.id} failed")
                        setRefreshing(false)
                    }
                }
            )
        }
    }
}
