package com.scandit.shelf.kotlinapp.ui.storeselection

import androidx.recyclerview.widget.DiffUtil
import com.scandit.shelf.sdk.catalog.Store

/**
 * A custom DiffUtil object to compare differences between two Store items based on their Store id.
 */
object StoreDiffUtilCallback : DiffUtil.ItemCallback<Store>() {
    override fun areItemsTheSame(oldItem: Store, newItem: Store): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Store, newItem: Store): Boolean = oldItem == newItem
}
