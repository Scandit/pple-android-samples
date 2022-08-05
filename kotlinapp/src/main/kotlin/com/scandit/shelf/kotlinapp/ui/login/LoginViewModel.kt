package com.scandit.shelf.kotlinapp.ui.login

import androidx.lifecycle.ViewModel
import com.scandit.shelf.authentication.Authentication
import com.scandit.shelf.common.CompletionHandler
import kotlinx.coroutines.flow.MutableStateFlow

class LoginViewModel : ViewModel() {

    val isRefreshingStateFlow = MutableStateFlow(false)
    val loginSucceededFlow = MutableStateFlow<Boolean?>(null)

    fun login(email: String, password: String) {
        setRefreshing(true)
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
