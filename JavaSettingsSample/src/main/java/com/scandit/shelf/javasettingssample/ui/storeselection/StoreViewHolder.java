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

package com.scandit.shelf.javasettingssample.ui.storeselection;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.scandit.shelf.javasettingssample.R;
import com.scandit.shelf.sdk.catalog.Store;

/**
 * A RecyclerView ViewHolder that binds and displays the Store item as well
 * as responds to click event on the item.
 */
public class StoreViewHolder extends RecyclerView.ViewHolder {

    private final StoreItemViewModel viewModel;

    public StoreViewHolder(
            @NonNull View itemView,
            LifecycleOwner lifecycleOwner,
            MutableLiveData<Store> selectedStoreLiveData
    ) {
        super(itemView);
        viewModel = new StoreItemViewModel(selectedStoreLiveData);

        itemView.setOnClickListener(v -> viewModel.onClick());

        MaterialTextView storeTextView = itemView.findViewById(R.id.item_store_name);

        viewModel.getStoreName().observe(lifecycleOwner, storeTextView::setText);
    }

    public void bind(Store item) {
        viewModel.bind(item);
    }
}
