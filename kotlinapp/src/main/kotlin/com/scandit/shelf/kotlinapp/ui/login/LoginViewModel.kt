package com.scandit.shelf.kotlinapp.ui.login

import androidx.lifecycle.ViewModel
import com.scandit.shelf.sdk.authentication.Authentication
import com.scandit.shelf.sdk.common.CompletionHandler
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * A ViewModel responsible for handling user login to organization.
 */
class LoginViewModel : ViewModel() {

    // Reports whether login is in progress or not.
    val isRefreshingStateFlow = MutableStateFlow(false)

    // Reports whether or not login operation succeeded.
    val loginSucceededFlow = MutableStateFlow<Boolean?>(null)

    fun login(email: String, password: String) {
        setRefreshing(true)
        // Use the PPLE Authentication singleton to log in the user to an organization.
        Authentication.login(
            email,
            password,
            object : CompletionHandler<Unit> {
                override fun success(result: Unit) {
                    loginSucceededFlow.tryEmit(true)
                    setRefreshing(false)
                }

                override fun failure(error: Exception) {
                    loginSucceededFlow.tryEmit(false)
                    setRefreshing(false)
                }
            }
        )
    }

    private fun setRefreshing(isRefreshing: Boolean) {
        isRefreshingStateFlow.tryEmit(isRefreshing)
    }
}
