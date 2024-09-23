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

package com.scandit.shelf.javasettingssample.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import com.google.android.material.snackbar.Snackbar;
import com.scandit.shelf.javasettingssample.R;

/**
 * Base Fragment that handles all basic setup operations such as setting up the toolbar, setting
 * up navigation from/to the Fragment, inflating options menu and showing a Snackbar message.
 */
public class NavigationFragment extends Fragment implements MenuProvider {

    protected View root;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        root = view;
    }

    protected void setUpToolbar(Toolbar toolbar, String toolbarTitle, Boolean showBackButton) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
                activity.addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
                actionBar.setDisplayHomeAsUpEnabled(showBackButton);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_toolbar_back);
                actionBar.setTitle(toolbarTitle);
            }
        }
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        // empty menu
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        }
        return true;
    }

    protected void clearBackStack() {
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        int entryCount = fm.getBackStackEntryCount();
        while (entryCount-- > 0) {
            fm.popBackStack();
        }
    }

    protected void moveToFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }

    protected void dismissKeyboard(View focusedView) {
        try {
            InputMethodManager imm =
                    (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        } catch (Exception e) {
            Log.d(getClass().getSimpleName(), "Error closing the keyboard", e);
        }
    }

    protected void showSnackbar(String message) {
        if (message != null) {
            Snackbar.make(root, message, Snackbar.LENGTH_LONG).show();
        }
    }
}
