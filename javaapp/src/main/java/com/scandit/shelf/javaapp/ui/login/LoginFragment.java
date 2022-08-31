package com.scandit.shelf.javaapp.ui.login;

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
import com.scandit.shelf.javaapp.R;
import com.scandit.shelf.javaapp.ui.base.NavigationFragment;
import com.scandit.shelf.javaapp.ui.storeselection.StoreSelectionFragment;

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
        viewModel.isRefreshingLiveData.observe(getViewLifecycleOwner(), swipeRefreshLayout::setRefreshing);

        // Observe LiveData that posts login result.
        viewModel.loginSucceededLiveData.observe(getViewLifecycleOwner(), loginSucceeded -> {
            if (loginSucceeded) {
                // After successful login, move user to Store selection screen.
                moveToFragment(StoreSelectionFragment.newInstance(), false, null);
            } else {
                showSnackbar("Login failed");
            }
        });
    }
}
