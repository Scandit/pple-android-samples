package com.scandit.shelf.kotlinapp.ui.storeselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scandit.shelf.catalog.Catalog
import com.scandit.shelf.catalog.Store
import com.scandit.shelf.common.CompletionHandler
import com.scandit.shelf.kotlinapp.catalog.CatalogStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class StoreSelectionViewModel : ViewModel() {

    private val allStores = mutableListOf<Store>()
    private var searchText = ""

    val isRefreshingFlow = MutableStateFlow(false)
    val snackbarMessageFlow = MutableStateFlow<String?>(null)
    val storeListFlow = MutableStateFlow<List<Store>>(emptyList())
    val storeFlow = MutableStateFlow<Store?>(null)

    fun initStoreSelection() {
        CatalogStore.productCatalog = null
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
        Catalog.getStores(
            object : CompletionHandler<List<Store>> {
                override fun success(result: List<Store>) {
                    allStores.clear()
                    allStores.addAll(result)
                    emitStores()
                    setRefreshing(false)
                }

                override fun failure(error: Exception) {
                    snackbarMessageFlow.tryEmit("Fetching the list of stores failed")
                    setRefreshing(false)
                }
            }
        )
    }

    private fun emitStores() {
        storeListFlow.tryEmit(allStores.filter { it.name.contains(searchText, true) })
    }

    private fun getProducts(store: Store) {
        Catalog.getProductCatalog(store).apply {
            CatalogStore.productCatalog = this
            update(
                object : CompletionHandler<Unit> {
                    override fun success(result: Unit) {
                        snackbarMessageFlow.tryEmit(
                            "Updating the Product Catalog for store ${store.name} (id=${store.id}) ready"
                        )
                        setRefreshing(false)
                        storeFlow.tryEmit(store)
                    }

                    override fun failure(error: Exception) {
                        snackbarMessageFlow.tryEmit("Updating the Product Catalog for store with id=${store.id} failed")
                        setRefreshing(false)
                    }
                }
            )
        }
    }
}
