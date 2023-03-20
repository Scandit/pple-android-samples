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

package com.scandit.shelf.javasimplesample.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.scandit.shelf.ShelfSdkVersion;
import com.scandit.shelf.javasimplesample.R;
import com.scandit.shelf.javasimplesample.ui.base.NavigationFragment;
import com.scandit.shelf.javasimplesample.ui.storeselection.StoreSelectionFragment;

/**
 * A Fragment that allows user to input email and password required to login to its organization.
 */
public class LoginFragment extends NavigationFragment {

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    private LoginViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = (new ViewModelProvider(this)).get(LoginViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = rootView.findViewById(R.id.swipe_layout);

        TextView emailTextView = rootView.findViewById(R.id.email_text);
        TextView passwordTextView = rootView.findViewById(R.id.password_text);

        TextView versionTextView = rootView.findViewById(R.id.sdk_version);
        versionTextView.setText(ShelfSdkVersion.VERSION_STRING);

        Button loginButton = rootView.findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> {
            viewModel.login(emailTextView.getText().toString(), passwordTextView.getText().toString());
        });

        observeLiveData();
    }

    private void observeLiveData() {
        // Observe LiveData that posts login progress and update the SwipeRefreshLayout widget state.
        viewModel.isRefreshing().observe(getViewLifecycleOwner(), swipeRefreshLayout::setRefreshing);

        // Observe LiveData that posts login result.
        viewModel.hasLoginSucceeded().observe(getViewLifecycleOwner(), loginSucceeded -> {
            if (loginSucceeded) {
                // After successful login, move user to Store selection screen.
                moveToFragment(StoreSelectionFragment.newInstance(), false, null);
            } else {
                showSnackbar("Login failed");
            }
        });
    }
}
