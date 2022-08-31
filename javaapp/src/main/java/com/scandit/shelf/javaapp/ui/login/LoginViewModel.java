package com.scandit.shelf.javaapp.ui.login;

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

    private final MutableLiveData<Boolean> _isRefreshingLiveData = new MutableLiveData<>();

    // Reports whether login is in progress or not.
    LiveData<Boolean> isRefreshingLiveData = _isRefreshingLiveData;

    private MutableLiveData<Boolean> _loginSucceededLiveData = new MutableLiveData<>();

    // Reports whether or not login operation succeeded.
    LiveData<Boolean> loginSucceededLiveData = _loginSucceededLiveData;

    public void login(String email, String password) {
        setRefreshing(true);
        // Use the PPLE Authentication singleton to log in the user to an organization.
        Authentication.login(
                email,
                password,
                new CompletionHandler<Unit>() {
                    @Override
                    public void success(Unit result) {
                        _loginSucceededLiveData.postValue(true);
                        setRefreshing(false);
                    }

                    @Override
                    public void failure(@NonNull Exception error) {
                        _loginSucceededLiveData.postValue(false);
                        setRefreshing(false);
                    }
                }
        );
    }

    private void setRefreshing(boolean isRefreshing) {
        _isRefreshingLiveData.postValue(isRefreshing);
    }
}
