package com.scandit.shelf.kotlinapp.ui.pricecheck

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.scandit.shelf.core.ui.CaptureView
import com.scandit.shelf.core.ui.style.Brush
import com.scandit.shelf.core.ui.viewfinder.RectangularViewfinder
import com.scandit.shelf.kotlinapp.R
import com.scandit.shelf.kotlinapp.ui.base.CameraPermissionFragment
import com.scandit.shelf.kotlinapp.ui.login.LoginFragment
import com.scandit.shelf.price.PriceCheckResult
import com.scandit.shelf.price.ui.PriceCheckOverlay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class PriceCheckFragment : CameraPermissionFragment() {

    private lateinit var viewModel: PriceCheckViewModel
    private lateinit var captureView: CaptureView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PriceCheckViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.price_check_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar(rootView.findViewById(R.id.toolbar), "", true)

        rootView.findViewById<View>(R.id.btn_logout).setOnClickListener {
            viewModel.logout()
        }

        rootView.findViewById<TextView>(R.id.store_name).text =
            arguments?.getString(ARG_STORE_NAME) ?: ""

        captureView = rootView.findViewById(R.id.capture_view)

        collectFlows()
    }

    override fun onResume() {
        super.onResume()
        requestCameraPermission()
    }

    override fun onCameraPermissionGranted() {
        viewModel.initPriceCheck(
            captureView,
            PriceCheckOverlay(
                viewfinder = RectangularViewfinder(),
                correctPriceBrush = solidBrush(requireContext(), R.color.transparentGreen),
                wrongPriceBrush = solidBrush(requireContext(), R.color.transparentRed),
                unknownProductBrush = solidBrush(requireContext(), R.color.transparentGrey),
            )
        )
    }

    override fun onPause() {
        viewModel.pausePriceCheck()
        super.onPause()
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.resultFlow.filterNotNull().collectLatest {
                showTopSnackbar(it.toMessage())
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.logoutSucceededFlow.filterNotNull().collectLatest {
                if (it) {
                    clearBackStack()
                    moveToFragment(LoginFragment.newInstance(), false, null)
                } else {
                    showSnackbar("Logout failed")
                }
            }
        }
    }

    private fun showTopSnackbar(message: String) {
        rootView.findViewById<View>(R.id.top_snackbar)?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
        }
    }

    companion object {

        private const val ARG_STORE_NAME = "store_name"

        fun newInstance(storeName: String) =
            PriceCheckFragment().apply {
                arguments = Bundle().apply { putString(ARG_STORE_NAME, storeName) }
            }
    }
}

private fun PriceCheckResult.toMessage(): String = when {
    correctPrice == null -> "Unrecognized product - captured price: ${capturedPrice.priceFormat()}"
    correctPrice == capturedPrice -> "$name\nCorrect Price: ${capturedPrice.priceFormat()}"
    else -> "$name\nWrong Price: ${capturedPrice.priceFormat()}, should be ${correctPrice?.priceFormat()}"
}

fun Float.priceFormat(digits: Int = 2): String = "%.${digits}f".format(this)

private fun solidBrush(@ColorInt color: Int): Brush = Brush(color, Color.TRANSPARENT, 0f)

private fun solidBrush(context: Context, @ColorRes color: Int): Brush =
    solidBrush(ContextCompat.getColor(context, color))
