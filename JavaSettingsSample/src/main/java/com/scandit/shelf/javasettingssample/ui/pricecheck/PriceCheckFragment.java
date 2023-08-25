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

package com.scandit.shelf.javasettingssample.ui.pricecheck;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.scandit.shelf.javasettingssample.R;
import com.scandit.shelf.javasettingssample.ui.base.CameraPermissionFragment;
import com.scandit.shelf.javasettingssample.ui.overlay.CustomOverlayView;
import com.scandit.shelf.javasettingssample.ui.settings.SettingsOverviewFragment;
import com.scandit.shelf.sdk.core.ui.CaptureView;

/**
 * Price checking of Product labels happen in this Fragment. It takes a Store object that was
 * selected in the StoreSelectionFragment. Additionally, there's an option to click a
 * button in order to discontinue price checking and log user out of the organization.
 */
public class PriceCheckFragment extends CameraPermissionFragment {

    private static final String ARG_STORE_NAME = "store_name";

    public static PriceCheckFragment newInstance(String storeName) {
        // Create a PriceCheckFragment instance by passing as argument name of the user-selected Store.
        Bundle args = new Bundle();
        args.putString(ARG_STORE_NAME, storeName);
        PriceCheckFragment fragment = new PriceCheckFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private PriceCheckViewModel viewModel;

    private View pausedOverlayView;
    private CaptureView captureView;
    private CustomOverlayView customOverlayView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(PriceCheckViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.price_check_fragment, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        // Get the Store name that was passed to this Fragment as argument.
        String storeName = args == null ? "" : args.getString(ARG_STORE_NAME);
        setUpToolbar(rootView.findViewById(R.id.toolbar), storeName, true);

        rootView.findViewById(R.id.settings_button).setOnClickListener(
                view1 -> moveToFragment(SettingsOverviewFragment.newInstance(), true, null)
        );

        captureView = rootView.findViewById(R.id.capture_view);

        pausedOverlayView = rootView.findViewById(R.id.paused_overlay_view);
        pausedOverlayView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                viewModel.resumePriceCheck();
            }
            return true;
        });

        customOverlayView = rootView.findViewById(R.id.custom_overlay);
        customOverlayView.setVisibility(viewModel.isCustomOverlayEnabled() ? View.VISIBLE : View.GONE);

        observeLiveData();

        viewModel.initPriceCheck(captureView, requireContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        // Good time to request user for camera permission for price label scanning.
        requestCameraPermission();
    }

    @Override
    public void onCameraPermissionGranted() {
        viewModel.resumePriceCheck();
    }

    @Override
    public void onPause() {
        // Pause price checking when Fragment pauses.
        viewModel.pausePriceCheck();
        super.onPause();
    }

    private void observeLiveData() {
        // Observe the LivaData that posts messages to be displayed on a snackbar.
        viewModel.getSnackbarMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) showTopSnackbar(message);
        });

        // Observe the LivaData that posts information whether non-continuous price check flow is paused.
        viewModel.getIsNonContinuousFlowPaused().observe(getViewLifecycleOwner(), isPaused -> {
            if (isPaused != null) pausedOverlayView.setVisibility(isPaused ? View.VISIBLE : View.GONE);
        });

        // Observe the LivaData that posts PriceLabelSessions, which need to be passed to the custom overlay view.
        viewModel.getSession().observe(getViewLifecycleOwner(), session -> {
            customOverlayView.updateOverlay(session, captureView);
        });
    }

    private void showTopSnackbar(String message) {
        View snackbar = rootView.findViewById(R.id.top_snackbar);
        Snackbar.make(snackbar, message, Snackbar.LENGTH_LONG).show();
    }
}
