package com.scandit.shelf.javaapp.ui.storeselection;

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
import com.scandit.shelf.catalog.Store;
import com.scandit.shelf.javaapp.R;
import com.scandit.shelf.javaapp.ui.base.NavigationFragment;
import com.scandit.shelf.javaapp.ui.pricecheck.PriceCheckFragment;

import java.util.Collections;

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

        viewModel.storeListLiveData.observe(getViewLifecycleOwner(), it -> {
            Collections.sort(it, (o1, o2) -> o1.getName().compareTo(o2.getName()));
            storesAdapter.submitList(it);
        });

        selectedStoreLiveData.observe(getViewLifecycleOwner(), selectedStore -> {
            if (selectedStore != null) {
                showSnackbar(
                        "Fetching the products for " + selectedStore.getName() + " (id=" + selectedStore.getId() + ")"
                );
                viewModel.refreshProducts(selectedStore);
                hideKeyboard();
            }
        });
    }

    private void observeLiveData() {
        viewModel.isRefreshingLiveData.observe(getViewLifecycleOwner(), swipeRefreshLayout::setRefreshing);

        viewModel.snackbarMessageLiveData.observe(getViewLifecycleOwner(), this::showSnackbar);

        viewModel.storeLiveData.observe(getViewLifecycleOwner(), store ->
        {
            if (store != null) {
                moveToFragment(PriceCheckFragment.newInstance(store.getName()), true, null);
            }
        });
    }

    @Override
    public void onRefresh() {
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
