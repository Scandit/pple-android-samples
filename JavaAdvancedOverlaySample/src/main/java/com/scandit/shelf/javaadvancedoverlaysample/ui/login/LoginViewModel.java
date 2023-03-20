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

package com.scandit.shelf.javaadvancedoverlaysample.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scandit.shelf.sdk.authentication.Authentication;
import com.scandit.shelf.sdk.common.CompletionHandler;

import kotlin.Unit;

/**
 * A ViewModel responsible for handling user login to organization.
 */
public class LoginViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isRefreshingLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginSucceededLiveData = new MutableLiveData<>();

    // Reports whether login is in progress or not.
    public LiveData<Boolean> isRefreshing() {
        return isRefreshingLiveData;
    }

    // Reports whether or not login operation succeeded.
    public LiveData<Boolean> hasLoginSucceeded() {
        return loginSucceededLiveData;
    }

    public void login(String email, String password) {
        setRefreshing(true);
        // Use the PPLE Authentication singleton to log in the user to an organization.
        Authentication.login(
                email,
                password,
                new CompletionHandler<Unit>() {
                    @Override
                    public void success(Unit result) {
                        loginSucceededLiveData.postValue(true);
                        setRefreshing(false);
                    }

                    @Override
                    public void failure(@NonNull Exception error) {
                        loginSucceededLiveData.postValue(false);
                        setRefreshing(false);
                    }
                }
        );
    }

    private void setRefreshing(boolean isRefreshing) {
        isRefreshingLiveData.postValue(isRefreshing);
    }
}
