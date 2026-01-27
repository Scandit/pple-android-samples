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

package com.scandit.shelf.javasettingssample;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.scandit.shelf.javasettingssample.ui.base.NavigationFragment;
import com.scandit.shelf.javasettingssample.ui.login.LoginFragment;
import com.scandit.shelf.javasettingssample.ui.storeselection.StoreSelectionFragment;
import com.scandit.shelf.sdk.authentication.Authentication;

/**
 * The main Activity that hosts all Fragments in the sample app.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        View fragmentContainer = findViewById(R.id.fragment_container);

        // Handle edge-to-edge insets for Android 15+ compatibility - this ensures the ActionBar is below the status bar
        ViewCompat.setOnApplyWindowInsetsListener(fragmentContainer, (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(view.getPaddingLeft(), insets.top, view.getPaddingRight(), view.getPaddingBottom());
            return windowInsets;
        });

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
