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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import com.scandit.shelf.kotlinadvancedoverlaysample.R
import com.scandit.shelf.sdk.catalog.Store
import kotlinx.coroutines.channels.Channel

/**
 * A RecyclerView Adapter for displaying the list of Stores.
 */
class StoresAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val selectedStoreChannel: Channel<Store>,
) : ListAdapter<Store, StoreViewHolder>(StoreDiffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder = StoreViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_store_row, parent, false),
        lifecycleOwner,
        selectedStoreChannel
    )

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
