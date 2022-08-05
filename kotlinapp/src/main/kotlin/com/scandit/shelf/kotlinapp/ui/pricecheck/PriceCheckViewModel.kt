package com.scandit.shelf.kotlinapp.ui.pricecheck

import androidx.lifecycle.ViewModel
import com.scandit.shelf.authentication.Authentication
import com.scandit.shelf.common.CompletionHandler
import com.scandit.shelf.core.ui.CaptureView
import com.scandit.shelf.kotlinapp.catalog.CatalogStore
import com.scandit.shelf.price.PriceCheck
import com.scandit.shelf.price.PriceCheckListener
import com.scandit.shelf.price.PriceCheckResult
import com.scandit.shelf.price.ui.PriceCheckOverlay
import kotlinx.coroutines.flow.MutableStateFlow

class PriceCheckViewModel : ViewModel(), PriceCheckListener {

    private var priceCheck: PriceCheck? = null

    val resultFlow = MutableStateFlow<PriceCheckResult?>(null)
    val logoutSucceededFlow = MutableStateFlow<Boolean?>(null)

    fun initPriceCheck(view: CaptureView, overlay: PriceCheckOverlay) {
        CatalogStore.productCatalog?.let {
            priceCheck = PriceCheck(view, it).apply {
                addListener(this@PriceCheckViewModel)
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

    fun pausePriceCheck() {
        priceCheck?.apply {
            disable(object : CompletionHandler<Unit> {
                override fun success(result: Unit) {
                    // Handle price checking disable success
                }

                override fun failure(error: Exception) {
                    // Gracefully handle price checking disable failure
                }
            })
        }
    }

    fun logout() {
        Authentication.logout(object : CompletionHandler<Unit> {
            override fun success(result: Unit) {
                logoutSucceededFlow.tryEmit(true)
            }

            override fun failure(error: Exception) {
                logoutSucceededFlow.tryEmit(false)
            }
        })
    }

    override fun onCorrectPrice(priceCheckResult: PriceCheckResult) {
        resultFlow.tryEmit(priceCheckResult)
    }

    override fun onWrongPrice(priceCheckResult: PriceCheckResult) {
        resultFlow.tryEmit(priceCheckResult)
    }

    override fun onUnknownProduct(priceCheckResult: PriceCheckResult) {
        resultFlow.tryEmit(priceCheckResult)
    }
}
