package com.scandit.shelf.kotlinapp.ui.storeselection

import androidx.lifecycle.MutableLiveData
import com.scandit.shelf.catalog.Store
import kotlinx.coroutines.channels.Channel

class StoreItemViewModel(
    private val storeChannel: Channel<Store>
) {
    var item: Store? = null
    val storeName = MutableLiveData<String>()

    fun bind(store: Store) {
        item = store
        storeName.postValue(store.name)
    }

    fun onClick() {
        item?.let { storeChannel.trySend(it) }
    }
}
