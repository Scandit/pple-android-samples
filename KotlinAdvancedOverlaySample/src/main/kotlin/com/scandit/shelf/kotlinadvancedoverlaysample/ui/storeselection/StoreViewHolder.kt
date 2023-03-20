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

import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.scandit.shelf.kotlinadvancedoverlaysample.R
import com.scandit.shelf.sdk.catalog.Store
import kotlinx.coroutines.channels.Channel

/**
 * A RecyclerView ViewHolder that binds and displays the Store item as well
 * as responds to click event on the item.
 */
class StoreViewHolder(
    itemView: View,
    lifecycleOwner: LifecycleOwner,
    selectedStoreChannel: Channel<Store>
) : RecyclerView.ViewHolder(itemView) {

    private val viewModel = StoreItemViewModel(selectedStoreChannel)

    init {
        itemView.setOnClickListener {
            viewModel.onClick()
        }

        val storeTextView = itemView.findViewById<TextView>(R.id.item_store_name)

        viewModel.storeName.observe(lifecycleOwner, storeTextView::setText)
    }

    fun bind(item: Store) {
        viewModel.bind(item)
    }
}
