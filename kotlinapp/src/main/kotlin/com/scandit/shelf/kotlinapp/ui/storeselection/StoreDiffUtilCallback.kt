package com.scandit.shelf.kotlinapp.ui.storeselection

import androidx.recyclerview.widget.DiffUtil
import com.scandit.shelf.catalog.Store

object StoreDiffUtilCallback : DiffUtil.ItemCallback<Store>() {
    override fun areItemsTheSame(oldItem: Store, newItem: Store): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Store, newItem: Store): Boolean = oldItem == newItem
}
