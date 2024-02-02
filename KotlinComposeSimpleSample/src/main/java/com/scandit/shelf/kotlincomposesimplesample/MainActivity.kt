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

package com.scandit.shelf.kotlincomposesimplesample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.scandit.shelf.kotlincomposesimplesample.ui.theme.SampleTheme
import com.scandit.shelf.sdk.core.area.LocationSelection
import com.scandit.shelf.sdk.core.area.RectangularLocationSelection
import com.scandit.shelf.sdk.core.common.geometry.FloatWithUnit
import com.scandit.shelf.sdk.core.common.geometry.MeasureUnit
import com.scandit.shelf.sdk.core.common.geometry.SizeWithUnit
import com.scandit.shelf.sdk.core.ui.viewfinder.RectangularViewfinder
import com.scandit.shelf.sdk.core.ui.viewfinder.RectangularViewfinderStyle
import com.scandit.shelf.sdk.core.ui.viewfinder.Viewfinder
import com.scandit.shelf.sdk.core.ui.viewfinder.ViewfinderConfiguration
import com.scandit.shelf.sdk.price.ui.AdvancedPriceCheckOverlay
import com.scandit.shelf.sdk.price.ui.DefaultPriceCheckAdvancedOverlayListener
import com.scandit.shelf.sdk.price.ui.PriceCheckOverlay

/**
 * The main Activity that will setup a CaptureView within UI build
 * with Jetpack Compose and run price check.
 * Most of the work is delegated to the corresponding ViewModel.
 */
class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        setContent {
            SampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        getOverlay = ::getDefaultOverlay,
                        getViewfinderConfiguration = ::getDefaultViewfinderConfiguration
                    )
                }
            }
        }

        viewModel.authenticateAndFetchData()
    }

    private fun getDefaultOverlay(): PriceCheckOverlay =
        AdvancedPriceCheckOverlay(DefaultPriceCheckAdvancedOverlayListener())

    private fun getDefaultViewfinderConfiguration(): ViewfinderConfiguration =
        ViewfinderConfiguration(getDefaultViewfinder(), getDefaultLocationSelection())

    private fun getDefaultViewfinder(): Viewfinder = RectangularViewfinder(RectangularViewfinderStyle.ROUNDED).apply {
        setSize(
            SizeWithUnit(
                FloatWithUnit(0.9f, MeasureUnit.FRACTION), FloatWithUnit(0.3f, MeasureUnit.FRACTION)
            )
        )
        dimming = 0.6f
    }

    private fun getDefaultLocationSelection(): LocationSelection = RectangularLocationSelection.withSize(
        SizeWithUnit(
            FloatWithUnit(0.9f, MeasureUnit.FRACTION), FloatWithUnit(0.3f, MeasureUnit.FRACTION)
        )
    )
}
