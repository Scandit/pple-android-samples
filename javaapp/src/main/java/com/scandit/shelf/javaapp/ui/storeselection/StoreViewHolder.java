package com.scandit.shelf.javaapp.ui.storeselection;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.scandit.shelf.catalog.Store;
import com.scandit.shelf.javaapp.R;

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

        TextView storeTextView = itemView.findViewById(R.id.item_store_name);
        viewModel.storeName.observe(lifecycleOwner, storeTextView::setText);
    }

    public void bind(Store item) {
        viewModel.bind(item);
    }
}
