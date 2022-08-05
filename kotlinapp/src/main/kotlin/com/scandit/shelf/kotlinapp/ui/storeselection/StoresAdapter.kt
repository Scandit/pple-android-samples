package com.scandit.shelf.kotlinapp.ui.storeselection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import com.scandit.shelf.catalog.Store
import com.scandit.shelf.kotlinapp.R
import kotlinx.coroutines.channels.Channel

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
