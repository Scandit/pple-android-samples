package com.scandit.shelf.javaapp.ui.storeselection;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.scandit.shelf.catalog.Store;
import com.scandit.shelf.javaapp.R;

public class StoresAdapter extends ListAdapter<Store, StoreViewHolder> {

    private final LifecycleOwner lifecycleOwner;
    private final MutableLiveData<Store> selectedStoreLiveData;

    public StoresAdapter(LifecycleOwner lifecycleOwner, MutableLiveData<Store> selectedStoreLiveData) {
        super(DIFF_CALLBACK);
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

    static DiffUtil.ItemCallback<Store> DIFF_CALLBACK = new DiffUtil.ItemCallback<Store>() {

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
