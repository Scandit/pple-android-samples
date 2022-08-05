package com.scandit.shelf.kotlinapp.ui.storeselection

import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.scandit.shelf.catalog.Store
import com.scandit.shelf.kotlinapp.R
import kotlinx.coroutines.channels.Channel

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
        viewModel.storeName.observe(lifecycleOwner) {
            storeTextView.text = it
        }
    }

    fun bind(item: Store) {
        viewModel.bind(item)
    }
}
