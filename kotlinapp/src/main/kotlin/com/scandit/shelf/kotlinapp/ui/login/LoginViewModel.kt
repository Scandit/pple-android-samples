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
