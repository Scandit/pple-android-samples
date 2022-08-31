package com.scandit.shelf.kotlinapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.scandit.shelf.kotlinapp.ui.login.LoginFragment
import com.scandit.shelf.kotlinapp.ui.storeselection.StoreSelectionFragment
import com.scandit.shelf.sdk.authentication.Authentication

/**
 * The main Activity that hosts all Fragments in the sample app.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            val initFragment = if (Authentication.isAuthenticated) {
                StoreSelectionFragment.newInstance()
            } else {
                LoginFragment.newInstance()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, initFragment)
                .commitNow()
        }
    }
}
