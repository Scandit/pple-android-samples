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

package com.scandit.shelf.kotlinexternalcatalogsample

import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.scandit.shelf.kotlinexternalcatalogsample.base.CameraPermissionActivity
import com.scandit.shelf.sdk.core.area.LocationSelection
import com.scandit.shelf.sdk.core.area.RectangularLocationSelection
import com.scandit.shelf.sdk.core.common.geometry.FloatWithUnit
import com.scandit.shelf.sdk.core.common.geometry.MeasureUnit
import com.scandit.shelf.sdk.core.common.geometry.SizeWithUnit
import com.scandit.shelf.sdk.core.ui.CaptureView
import com.scandit.shelf.sdk.core.ui.viewfinder.RectangularViewfinder
import com.scandit.shelf.sdk.core.ui.viewfinder.RectangularViewfinderStyle
import com.scandit.shelf.sdk.core.ui.viewfinder.Viewfinder
import com.scandit.shelf.sdk.core.ui.viewfinder.ViewfinderConfiguration
import com.scandit.shelf.sdk.price.ui.AdvancedPriceCheckOverlay
import com.scandit.shelf.sdk.price.ui.DefaultPriceCheckAdvancedOverlayListener
import com.scandit.shelf.sdk.price.ui.PriceCheckOverlay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

/**
 * The main Activity that will setup a CaptureView and run price check
 * with a custom implementation of the ProductProvider.
 * Most of the work is delegated to the corresponding ViewModel.
 */
class MainActivity : CameraPermissionActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var root: ConstraintLayout
    private lateinit var captureViewContainer: FrameLayout
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        setContentView(R.layout.main_activity)
        root = findViewById(R.id.container)
        captureViewContainer = findViewById(R.id.capture_view_container)
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
        // Additionally, CaptureView instance should only be created, after ProductCatalog.update()
        // method is called, which fetches the necessary config fot CaptureView.
        val captureView = CaptureView(this)
        captureViewContainer.addView(captureView, ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))

        viewModel.initPriceCheck(captureView, getDefaultOverlay(), getDefaultViewfinderConfiguration())
        viewModel.resumePriceCheck()
    }

    override fun onCameraPermissionDenied() {
        // Display short message on the screen that the camera permission has been rejected.
        // In your app, you might want to provide additional explanation to the user,
        // explaining why the permission is required, and request the permission again
        // or gracefully handle the permission denial.
        findViewById<TextView>(R.id.status_text_view).text = getString(R.string.camera_permission_denied)
    }

    private fun getDefaultOverlay(): PriceCheckOverlay =
        AdvancedPriceCheckOverlay(DefaultPriceCheckAdvancedOverlayListener())

    private fun getDefaultViewfinderConfiguration(): ViewfinderConfiguration =
        ViewfinderConfiguration(getDefaultViewfinder(), getDefaultLocationSelection())

    private fun getDefaultViewfinder(): Viewfinder =
        RectangularViewfinder(RectangularViewfinderStyle.ROUNDED).apply {
            setSize(
                SizeWithUnit(
                    FloatWithUnit(0.9f, MeasureUnit.FRACTION),
                    FloatWithUnit(0.3f, MeasureUnit.FRACTION)
                )
            )
            dimming = 0.6f
        }

    private fun getDefaultLocationSelection(): LocationSelection =
        RectangularLocationSelection.withSize(
            SizeWithUnit(
                FloatWithUnit(0.9f, MeasureUnit.FRACTION),
                FloatWithUnit(0.3f, MeasureUnit.FRACTION)
            )
        )

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
                    Status.CATALOG_UPDATE_FAILED -> setStatus(R.string.catalog_update_failed)
                }
            }
        }

        lifecycleScope.launch {
            // Collect the Flow that emits messages to be displayed on a snackbar.
            viewModel.snackbarFlow.filterNotNull().collectLatest { showMessage(it) }
        }

        lifecycleScope.launch {
            // Collect the Flow that emits current store to set ActionBar title.
            viewModel.currentStoreFlow.filterNotNull().collectLatest {
                supportActionBar?.title = it.name
            }
        }
    }

    private fun onStatusReady() {
        // Check for camera permission and request it, if it hasn't yet been granted.
        // Once we have the permission the onCameraPermissionGranted() method will be called.
        requestCameraPermission()
    }

    private fun showMessage(snackbarData: SnackbarData) {
        Snackbar.make(root.findViewById(R.id.top_snackbar), snackbarData.message, Snackbar.LENGTH_LONG).apply {
            setBackgroundTint(ContextCompat.getColor(this@MainActivity, snackbarData.backgroundColorResId))
            view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 3
            show()
        }
    }

    private fun setStatus(@StringRes message: Int) {
        status.setText(message)
    }
}
