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
