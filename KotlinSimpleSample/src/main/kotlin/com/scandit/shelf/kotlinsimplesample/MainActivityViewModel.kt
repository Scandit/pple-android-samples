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

import androidx.lifecycle.ViewModel
import com.scandit.shelf.sdk.authentication.Authentication
import com.scandit.shelf.sdk.catalog.Catalog
import com.scandit.shelf.sdk.catalog.ProductCatalog
import com.scandit.shelf.sdk.catalog.Store
import com.scandit.shelf.sdk.common.CompletionHandler
import com.scandit.shelf.sdk.core.ui.CaptureView
import com.scandit.shelf.sdk.core.ui.viewfinder.ViewfinderConfiguration
import com.scandit.shelf.sdk.price.PriceCheck
import com.scandit.shelf.sdk.price.PriceCheckListener
import com.scandit.shelf.sdk.price.PriceCheckResult
import com.scandit.shelf.sdk.price.ui.PriceCheckOverlay
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * A ViewModel for the MainActivity.
 */
class MainActivityViewModel : ViewModel(), PriceCheckListener {

    private var productCatalog: ProductCatalog? = null
    private var priceCheck: PriceCheck? = null

    // Emits the status of the price check setup.
    val statusFlow: MutableStateFlow<Status> = MutableStateFlow(Status.INIT)

    // Emits the price check result.
    val resultFlow: MutableStateFlow<PriceCheckResult?> = MutableStateFlow(null)

    // Emits the price check result.
    val currentStore: MutableStateFlow<Store?> = MutableStateFlow(null)

    // Perform the initial steps required for the price checking process, including:
    // - ShelfView authentication,
    // - fetching the list of stores belonging to your organization,
    // - preparing the ProductCatalog
    fun authenticateAndFetchData() {
        authenticate()
    }

    // Prepare the PriceCheck instance. Should only be called after the ProductCatalog is created,
    // otherwise the method will have no effect.
    fun initPriceCheck(
        view: CaptureView,
        overlay: PriceCheckOverlay,
        viewfinderConfiguration: ViewfinderConfiguration
    ) {
        productCatalog?.let {
            // Initialize the PriceCheck object
            priceCheck = PriceCheck(view, it).apply {
                addListener(this@MainActivityViewModel)
                // Add a PriceCheckOverlay created in MainActivity.
                // By default, price labels are sought on the whole capture view. If you want to limit the scan area,
                // pass a non-null LocationSelection to PriceCheckOverlay's constructor.
                addOverlay(overlay)
                setViewfinderConfiguration(viewfinderConfiguration)
            }
        }
    }

    // Enables the price check, which will start the camera and begin processing the camera frames.
    // Should only be called after the priceCheck instance has been initialized,
    // otherwise the method will have no effect.
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

    // Disables the price check, which will stop the camera and the processing of the camera frames.
    // Should only be called after the priceCheck instance has been initialized,
    // otherwise the method will have no effect.
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

    // Disposes price check to release resources consumed by it.
    fun onDestroyPriceCheck() {
        priceCheck?.dispose()
        priceCheck = null
        statusFlow.value = Status.INIT
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

    private fun authenticate() {
        // Use the PPLE Authentication singleton to log in the user to an organization.
        Authentication.login(
            PPLE_USERNAME,
            PPLE_PASSWORD,
            object : CompletionHandler<Unit> {
                override fun success(result: Unit) {
                    getStores()
                }

                override fun failure(error: Exception) {
                    statusFlow.tryEmit(Status.AUTH_FAILED)
                }
            }
        )
    }

    private fun getStores() {
        // Get/Update the list of stores by using the Catalog singleton object of the PPLE SDK.
        // Pass a CompletionHandler to the getStores method for handling API result.
        Catalog.getStores(
            object : CompletionHandler<List<Store>> {
                override fun success(result: List<Store>) {
                    if (result.isEmpty()) {
                        statusFlow.tryEmit(Status.STORES_EMPTY)
                    } else {
                        // Get products for a selected store from your organization.
                        // For simplicity reasons, below we select the first store on the list.
                        val store = result[0]
                        currentStore.tryEmit(store)
                        getProducts(store)
                    }
                }

                override fun failure(error: Exception) {
                    statusFlow.tryEmit(Status.STORE_DOWNLOAD_FAILED)
                }
            }
        )
    }

    private fun getProducts(store: Store) {
        // Get/Update the Product items for a given Store.

        // First create the ProductCatalog object.
        //
        // If you are using the ShelfView backend as your product catalog provider, you only need to specify the Store,
        // for which you will perform the Price Check - just like in the code below.
        //
        // If on the other hand, you would like to use a different source of data for the ProductCatalog,
        // you should should pass your custom implementation of the ProductProvider interface, as the second argument
        // for the Catalog.getProductCatalog method - check the docs for more details.
        productCatalog = Catalog.getProductCatalog(store).also {
            it.update(
                object : CompletionHandler<Unit> {
                    override fun success(result: Unit) {
                        statusFlow.tryEmit(Status.READY)
                    }

                    override fun failure(error: Exception) {
                        statusFlow.tryEmit(Status.CATALOG_DOWNLOAD_FAILED)
                    }
                }
            )
        }
    }

    private companion object {
        // Enter your Scandit ShelfView credentials here.
        private const val PPLE_USERNAME: String = "-- ENTER YOUR SCANDIT SHELFVIEW USERNAME HERE --"
        private const val PPLE_PASSWORD: String = "-- ENTER YOUR SCANDIT SHELFVIEW PASSWORD HERE --"
    }
}
