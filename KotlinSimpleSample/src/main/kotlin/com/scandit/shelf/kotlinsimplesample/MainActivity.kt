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

package com.scandit.shelf.kotlinsimplesample

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.scandit.shelf.kotlinsimplesample.base.CameraPermissionActivity
import com.scandit.shelf.sdk.core.ui.CaptureView
import com.scandit.shelf.sdk.core.ui.style.Brush
import com.scandit.shelf.sdk.core.ui.viewfinder.RectangularViewfinder
import com.scandit.shelf.sdk.core.ui.viewfinder.ViewfinderConfiguration
import com.scandit.shelf.sdk.price.PriceCheckResult
import com.scandit.shelf.sdk.price.ui.BasicPriceCheckOverlay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

/**
 * The main Activity that will setup a CaptureView and run price check.
 * Most of the work is delegated to the corresponding ViewModel.
 */
class MainActivity : CameraPermissionActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var root: ConstraintLayout
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        setContentView(R.layout.main_activity)
        root = findViewById(R.id.container)
        status = findViewById(R.id.status_text_view)
        collectFlows()
        viewModel.authenticateAndFetchData()
    }

    override fun onResume() {
        super.onResume()
        // Resume price checking when Activity resumes.
        viewModel.resumePriceCheck()
    }

    override fun onPause() {
        // Pause price checking when Activity pauses.
        viewModel.pausePriceCheck()
        super.onPause()
    }

    override fun onDestroy() {
        // Destroy price check to release all resources.
        viewModel.onDestroyPriceCheck()
        super.onDestroy()
    }

    override fun onCameraPermissionGranted() {
        // As price check requires the camera feed, it can only be started once the camera
        // permission has been granted.
        val captureView = CaptureView(this)
        root.addView(
            captureView, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        viewModel.initPriceCheck(
            captureView,
            BasicPriceCheckOverlay(
                correctPriceBrush = solidBrush(this, R.color.transparentGreen),
                wrongPriceBrush = solidBrush(this, R.color.transparentRed),
                unknownProductBrush = solidBrush(this, R.color.transparentGrey),
            ),
            ViewfinderConfiguration(RectangularViewfinder(), null)
        )

        viewModel.resumePriceCheck()
    }

    override fun onCameraPermissionDenied() {
        // Display short message on the screen that the camera permission has been rejected.
        // In your app, you might want to provide additional explanation to the user,
        // explaining why the permission is required, and request the permission again
        // or gracefully handle the permission denial.
        setStatus(R.string.camera_permission_denied)
    }

    private fun collectFlows() {
        lifecycleScope.launch {
            // Collect the Flow that emits the status of the price check setup.
            viewModel.statusFlow.collectLatest {
                when (it) {
                    Status.INIT -> setStatus(R.string.init)
                    Status.READY -> onStatusReady()
                    Status.AUTH_FAILED -> setStatus(R.string.authentication_failed)
                    Status.STORE_DOWNLOAD_FAILED -> setStatus(R.string.store_download_failed)
                    Status.STORES_EMPTY -> setStatus(R.string.stores_empty)
                    Status.CATALOG_DOWNLOAD_FAILED -> setStatus(R.string.catalog_download_Failed)
                }
            }
        }

        lifecycleScope.launch {
            // Collect the Flow that emits price checking results.
            viewModel.resultFlow.filterNotNull().collectLatest {
                Snackbar.make(root, it.toMessage(), Snackbar.LENGTH_LONG).show()
            }
        }

        lifecycleScope.launch {
            // Collect the Flow that emits current store to set ActionBar title
            viewModel.currentStore.filterNotNull().collectLatest {
                supportActionBar?.title = it.name
            }
        }
    }

    private fun onStatusReady() {
        // Check for camera permission and request it, if it hasn't yet been granted.
        // Once we have the permission the onCameraPermissionGranted() method will be called.
        requestCameraPermission()
    }

    private fun setStatus(@StringRes message: Int) {
        status.setText(message)
    }
}

private fun PriceCheckResult.toMessage(): String = when (correctPrice) {
    null -> "Unrecognized product - captured price: ${capturedPrice.priceFormat()}"
    capturedPrice -> "$name\nCorrect Price: ${capturedPrice.priceFormat()}"
    else -> "$name\nWrong Price: ${capturedPrice.priceFormat()}, should be ${correctPrice?.priceFormat()}"
}

fun Float.priceFormat(digits: Int = 2): String = "%.${digits}f".format(this)

private fun solidBrush(@ColorInt color: Int): Brush = Brush(color, Color.TRANSPARENT, 0f)

private fun solidBrush(context: Context, @ColorRes color: Int): Brush =
    solidBrush(ContextCompat.getColor(context, color))
