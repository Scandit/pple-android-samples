package com.scandit.shelf.javaapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.scandit.shelf.javaapp.ui.login.LoginFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, LoginFragment.newInstance())
                    .commitNow();
        }
    }
}
