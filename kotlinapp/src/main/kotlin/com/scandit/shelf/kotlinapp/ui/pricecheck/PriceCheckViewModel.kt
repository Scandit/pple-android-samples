package com.scandit.shelf.kotlinapp.ui.pricecheck

import androidx.lifecycle.ViewModel
import com.scandit.shelf.kotlinapp.catalog.CatalogStore
import com.scandit.shelf.sdk.authentication.Authentication
import com.scandit.shelf.sdk.common.CompletionHandler
import com.scandit.shelf.sdk.core.ui.CaptureView
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

    fun initPriceCheck(view: CaptureView, overlay: PriceCheckOverlay) {
        // Get the ProductCatalog object previously stored in CatalogStore
        CatalogStore.productCatalog?.let {
            // Initialize the PriceCheck object
            priceCheck = PriceCheck(view, it).apply {
                addListener(this@PriceCheckViewModel)
                // Add a PriceCheckOverlay created in PriceCheckFragment
                setOverlay(overlay)
                enable(object : CompletionHandler<Unit> {
                    override fun success(result: Unit) {
                        // Handle price checking enable success
                    }

                    override fun failure(error: Exception) {
                        // Gracefully handle price checking enable failure
                    }
                })
            }
        }
    }

    fun pausePriceCheck(onSuccess: () -> Unit = {}, onFailure: () -> Unit = {}) {
        priceCheck?.apply {
            disable(object : CompletionHandler<Unit> {
                override fun success(result: Unit) {
                    onSuccess()
                }

                override fun failure(error: Exception) {
                    onFailure()
                }
            })
        }
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
