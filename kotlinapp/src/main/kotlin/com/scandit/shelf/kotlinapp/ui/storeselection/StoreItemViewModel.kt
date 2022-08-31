package com.scandit.shelf.kotlinapp.ui.storeselection

import androidx.lifecycle.MutableLiveData
import com.scandit.shelf.sdk.catalog.Store
import kotlinx.coroutines.channels.Channel

/**
 * A ViewModel that exposes the currently selected Store to Activity/Fragment.
 */
class StoreItemViewModel(
    // Communicates with Fragment when Store selection in stores list changes.
    private val storeChannel: Channel<Store>
) {
    // Refers to the current Store item bound through [StoresAdapter].
    var item: Store? = null

    // Observes Store item binding in StoresAdapter.
    val storeName = MutableLiveData<String>()

    fun bind(store: Store) {
        item = store
        storeName.postValue(store.name)
    }

    fun onClick() {
        item?.let { storeChannel.trySend(it) }
    }
}
