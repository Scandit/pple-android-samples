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

package com.scandit.shelf.kotlinsimplesample.ui.storeselection

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
import com.scandit.shelf.kotlinsimplesample.R
import com.scandit.shelf.kotlinsimplesample.ui.base.NavigationFragment
import com.scandit.shelf.kotlinsimplesample.ui.pricecheck.PriceCheckFragment
import com.scandit.shelf.sdk.catalog.Store
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * A Fragment that fetches and displays the list of Stores for an organization.
 */
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

        // We do an initial update of the list of Stores so that it is not empty.
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
            // Collect the Flow that emits the list of fetched Stores.
            viewModel.storeListFlow.collectLatest {
                storesAdapter.submitList(it.sortedBy { store -> store.name })
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            selectedStoreChannel.receiveAsFlow()
                .collect {
                    showSnackbar("Fetching the products for ${it.name} (id=${it.id})")
                    // As soon as the Store selection changes, we request to update the
                    // list of Products for that Store.
                    viewModel.refreshProducts(it)
                    hideKeyboard()
                }
        }
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            // Update the SwipeRefreshLayout refresh progress state.
            viewModel.isRefreshingFlow.collectLatest(swipeRefreshLayout::setRefreshing)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            // Respond to any user-facing message generated in the ViewModel.
            viewModel.snackbarMessageFlow.filterNotNull().collectLatest(::showSnackbar)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            // Collect the Flow that emits the user-selected Store for which Product catalog has
            // been fetched. At this point, we should move to PriceCheckFragment for
            // price checking in the selected Store.
            viewModel.storeFlow.filterNotNull().collectLatest { store ->
                moveToFragment(PriceCheckFragment.newInstance(store.name), true, null)
            }
        }
    }

    override fun onRefresh() {
        // User swiped down from top to refresh the list Stores. We reset the search query
        // in order to not launch any text search for Products.
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
