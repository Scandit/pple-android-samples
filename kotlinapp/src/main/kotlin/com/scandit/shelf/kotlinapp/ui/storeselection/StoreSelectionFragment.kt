package com.scandit.shelf.kotlinapp.ui.storeselection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.textfield.TextInputEditText
import com.scandit.shelf.catalog.Store
import com.scandit.shelf.kotlinapp.R
import com.scandit.shelf.kotlinapp.ui.base.NavigationFragment
import com.scandit.shelf.kotlinapp.ui.pricecheck.PriceCheckFragment
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class StoreSelectionFragment : NavigationFragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var viewModel: StoreSelectionViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var searchView: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[StoreSelectionViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.store_selection_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout = rootView.findViewById(R.id.swipe_layout)
        swipeRefreshLayout.setOnRefreshListener(this)
        setupSearchView()

        viewModel.initStoreSelection()

        setupRecyclerView()
        collectFlows()

        viewModel.refreshStores()
    }

    private fun setupSearchView() {
        searchView = rootView.findViewById<TextInputEditText>(R.id.search_view)
            .apply {
                doOnTextChanged { text, _, _, _ -> viewModel.onSearchTextChanged(text.toString()) }
            }
    }

    private fun setupRecyclerView() {
        val selectedStoreChannel = Channel<Store>()
        val storesAdapter = StoresAdapter(viewLifecycleOwner, selectedStoreChannel)

        rootView.findViewById<RecyclerView>(R.id.stores).apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = storesAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.storeListFlow.collectLatest {
                storesAdapter.submitList(it.sortedBy { store -> store.name })
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            selectedStoreChannel.receiveAsFlow()
                .collect {
                    showSnackbar("Fetching the products for ${it.name} (id=${it.id})")
                    viewModel.refreshProducts(it)
                    hideKeyboard()
                }
        }
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isRefreshingFlow.collectLatest(swipeRefreshLayout::setRefreshing)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.snackbarMessageFlow.filterNotNull().collectLatest(::showSnackbar)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.storeFlow.filterNotNull().collectLatest { store ->
                moveToFragment(
                    PriceCheckFragment.newInstance(store.name),
                    true,
                    null
                )
            }
        }
    }

    override fun onRefresh() {
        searchView.setText("")
        viewModel.refreshStores()
    }

    companion object {
        fun newInstance() = StoreSelectionFragment()
    }
}

private fun Fragment.hideKeyboard() {
    ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
        ?.hideSoftInputFromWindow(requireView().windowToken, 0)
}
