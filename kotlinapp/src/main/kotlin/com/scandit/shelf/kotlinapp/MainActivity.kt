package com.scandit.shelf.kotlinapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.scandit.shelf.kotlinapp.ui.login.LoginFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment.newInstance())
                .commitNow()
        }
    }
}
