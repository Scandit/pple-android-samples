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

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.scandit.shelf.sdk.catalog.Store;
import com.scandit.shelf.javasettingssample.R;

/**
 * A RecyclerView Adapter for displaying the list of Stores.
 */
public class StoresAdapter extends ListAdapter<Store, StoreViewHolder> {

    private final LifecycleOwner lifecycleOwner;
    private final MutableLiveData<Store> selectedStoreLiveData;

    public StoresAdapter(LifecycleOwner lifecycleOwner, MutableLiveData<Store> selectedStoreLiveData) {
        super(diffCallback);
        this.lifecycleOwner = lifecycleOwner;
        this.selectedStoreLiveData = selectedStoreLiveData;
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StoreViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_row, parent, false),
                lifecycleOwner,
                selectedStoreLiveData
        );
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static DiffUtil.ItemCallback<Store> diffCallback = new DiffUtil.ItemCallback<Store>() {

        @Override
        public boolean areItemsTheSame(@NonNull Store oldItem, @NonNull Store newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Store oldItem, @NonNull Store newItem) {
            return oldItem.equals(newItem);
        }
    };
}
