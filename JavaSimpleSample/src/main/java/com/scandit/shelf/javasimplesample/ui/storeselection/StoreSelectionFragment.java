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

package com.scandit.shelf.javasimplesample.ui.storeselection;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.scandit.shelf.sdk.catalog.Store;
import com.scandit.shelf.javasimplesample.R;
import com.scandit.shelf.javasimplesample.ui.base.NavigationFragment;
import com.scandit.shelf.javasimplesample.ui.pricecheck.PriceCheckFragment;

import java.util.Collections;

/**
 * A Fragment that fetches and displays the list of Stores for an organization.
 */
public class StoreSelectionFragment extends NavigationFragment implements SwipeRefreshLayout.OnRefreshListener {

    public static StoreSelectionFragment newInstance() {
        return new StoreSelectionFragment();
    }

    private StoreSelectionViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextInputEditText searchView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = (new ViewModelProvider(this)).get(StoreSelectionViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.store_selection_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = rootView.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        setupSearchView();

        viewModel.initStoreSelection();

        setupRecyclerView();
        observeLiveData();

        // We do an initial update of the list of Stores so that it is not empty.
        viewModel.refreshStores();
    }

    private void setupSearchView() {
        searchView = rootView.findViewById(R.id.search_view);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing to do
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.onSearchTextChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Nothing to do
            }
        });
    }

    private void setupRecyclerView() {
        MutableLiveData<Store> selectedStoreLiveData = new MutableLiveData<>(null);
        StoresAdapter storesAdapter = new StoresAdapter(getViewLifecycleOwner(), selectedStoreLiveData);

        RecyclerView storesRecyclerView = rootView.findViewById(R.id.stores);
        storesRecyclerView.setHasFixedSize(false);
        storesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        storesRecyclerView.setAdapter(storesAdapter);
        storesRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL));

        // Observe the LiveData that posts the list of fetched Stores.
        viewModel.getStoreList().observe(getViewLifecycleOwner(), it -> {
            Collections.sort(it, (o1, o2) -> o1.getName().compareTo(o2.getName()));
            storesAdapter.submitList(it);
        });

        selectedStoreLiveData.observe(getViewLifecycleOwner(), selectedStore -> {
            if (selectedStore != null) {
                showSnackbar(
                        "Fetching the products for " + selectedStore.getName() + " (id=" + selectedStore.getId() + ")"
                );
                // As soon as the Store selection changes, we request to update the list of Products for that Store.
                viewModel.refreshProducts(selectedStore);
                hideKeyboard();
            }
        });
    }

    private void observeLiveData() {
        // Update the SwipeRefreshLayout refresh progress state.
        viewModel.isRefreshing().observe(getViewLifecycleOwner(), swipeRefreshLayout::setRefreshing);

        // Respond to any user-facing message generated in the ViewModel.
        viewModel.getSnackbarMessage().observe(getViewLifecycleOwner(), this::showSnackbar);

        // Observe the LiveData that posts the user-selected Store for which Product catalog has
        // been fetched. At this point, we should move to PriceCheckFragment for price checking in
        // the selected Store.
        viewModel.getStore().observe(getViewLifecycleOwner(), store ->
        {
            if (store != null) {
                moveToFragment(PriceCheckFragment.newInstance(store.getName()), true, null);
            }
        });
    }

    @Override
    public void onRefresh() {
        // User swiped down from top to refresh the list Stores. We reset the search query
        // in order to not launch any text search for Products.
        searchView.setText("");
        viewModel.refreshStores();
    }

    private void hideKeyboard() {
        InputMethodManager imm = ContextCompat.getSystemService(requireContext(), InputMethodManager.class);
        if (imm != null) {
            imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
        }
    }
}
