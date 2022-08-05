package com.scandit.shelf.kotlinapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.scandit.shelf.ShelfSdkVersion
import com.scandit.shelf.kotlinapp.R
import com.scandit.shelf.kotlinapp.ui.base.NavigationFragment
import com.scandit.shelf.kotlinapp.ui.storeselection.StoreSelectionFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class LoginFragment : NavigationFragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.login_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView.apply {
            swipeRefreshLayout = findViewById(R.id.swipe_layout)

            findViewById<TextView>(R.id.sdk_version).text = ShelfSdkVersion.VERSION_STRING

            findViewById<Button>(R.id.login_button).setOnClickListener {
                viewModel.login(
                    findViewById<TextView>(R.id.email_text).text.toString(),
                    findViewById<TextView>(R.id.password_text).text.toString()
                )
            }
        }

        collectFlows()
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isRefreshingStateFlow.collectLatest(swipeRefreshLayout::setRefreshing)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginSucceededFlow.filterNotNull().collectLatest { loginSucceeded ->
                if (loginSucceeded) {
                    moveToFragment(StoreSelectionFragment.newInstance(), false, null)
                } else {
                    showSnackbar("Login failed")
                }
            }
        }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}
