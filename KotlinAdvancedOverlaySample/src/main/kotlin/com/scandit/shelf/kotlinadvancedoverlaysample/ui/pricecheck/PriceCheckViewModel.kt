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

package com.scandit.shelf.kotlinadvancedoverlaysample.ui.pricecheck

import androidx.lifecycle.ViewModel
import com.scandit.shelf.kotlinadvancedoverlaysample.catalog.CatalogStore
import com.scandit.shelf.sdk.authentication.Authentication
import com.scandit.shelf.sdk.common.CompletionHandler
import com.scandit.shelf.sdk.core.ui.CaptureView
import com.scandit.shelf.sdk.core.ui.viewfinder.ViewfinderConfiguration
import com.scandit.shelf.sdk.price.PriceCheck
import com.scandit.shelf.sdk.price.PriceCheckListener
import com.scandit.shelf.sdk.price.PriceCheckResult
import com.scandit.shelf.sdk.price.ui.PriceCheckOverlay
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * A ViewModel that initializes and pauses price checking. It also allows to log the user out
 * from the organization.
 */
class PriceCheckViewModel : ViewModel(), PriceCheckListener {

    private var priceCheck: PriceCheck? = null

    // Emits the price check result.
    val resultFlow = MutableStateFlow<PriceCheckResult?>(null)

    // Emits a Boolean when user is successfully logged out.
    val logoutSucceededFlow = MutableStateFlow<Boolean?>(null)

    fun initPriceCheck(
        view: CaptureView,
        overlay: PriceCheckOverlay,
        viewfinderConfiguration: ViewfinderConfiguration
    ) {
        // Get the ProductCatalog object previously stored in CatalogStore
        CatalogStore.productCatalog?.let {
            // Initialize the PriceCheck object
            priceCheck = PriceCheck(view, it).apply {
                addListener(this@PriceCheckViewModel)
                // Add a PriceCheckOverlay created in PriceCheckFragment.
                // By default, price labels are sought on the whole capture view. If you want to limit the scan area,
                // pass a non-null LocationSelection to PriceCheckOverlay's constructor.
                addOverlay(overlay)
                setViewfinderConfiguration(viewfinderConfiguration)
            }
        }
    }

    fun resumePriceCheck() {
        priceCheck?.enable(object : CompletionHandler<Unit> {
            override fun success(result: Unit) {
                // Handle price checking enable success
            }

            override fun failure(error: Exception) {
                // Gracefully handle price checking enable failure
            }
        })
    }

    fun pausePriceCheck(onSuccess: () -> Unit = {}, onFailure: () -> Unit = {}) {
        priceCheck?.disable(object : CompletionHandler<Unit> {
            override fun success(result: Unit) {
                onSuccess()
            }

            override fun failure(error: Exception) {
                onFailure()
            }
        })
    }

    fun pauseAndLogout() {
        pausePriceCheck(onSuccess = this::logout, onFailure = this::logout)
    }

    private fun logout() {
        // Use the Authentication singleton to log user out of organization
        Authentication.logout(object : CompletionHandler<Unit> {
            override fun success(result: Unit) {
                // User was logged out successfully. Communicate with PriceCheckFragment.
                logoutSucceededFlow.tryEmit(true)
            }

            override fun failure(error: Exception) {
                // Logout failed. Communicate with PriceCheckFragment.
                logoutSucceededFlow.tryEmit(false)
            }
        })
    }

    override fun onCorrectPrice(priceCheckResult: PriceCheckResult) {
        // Handle result that a Product label was scanned with correct price
        resultFlow.tryEmit(priceCheckResult)
    }

    override fun onWrongPrice(priceCheckResult: PriceCheckResult) {
        // Handle result that a Product label was scanned with wrong price
        resultFlow.tryEmit(priceCheckResult)
    }

    override fun onUnknownProduct(priceCheckResult: PriceCheckResult) {
        // Handle result that a Product label was scanned for an unknown Product
        resultFlow.tryEmit(priceCheckResult)
    }
}
