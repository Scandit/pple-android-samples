package com.scandit.shelf.javaapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.scandit.shelf.sdk.authentication.Authentication;
import com.scandit.shelf.javaapp.ui.base.NavigationFragment;
import com.scandit.shelf.javaapp.ui.login.LoginFragment;
import com.scandit.shelf.javaapp.ui.storeselection.StoreSelectionFragment;

/**
 * The main Activity that hosts all Fragments in the sample app.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            NavigationFragment initFragment = Authentication.isAuthenticated()
                    ? StoreSelectionFragment.newInstance()
                    : LoginFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, initFragment)
                    .commitNow();
        }
    }
}
