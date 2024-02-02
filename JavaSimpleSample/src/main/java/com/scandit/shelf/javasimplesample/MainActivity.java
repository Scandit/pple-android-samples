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

package com.scandit.shelf.javasimplesample;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.scandit.shelf.javasimplesample.base.CameraPermissionActivity;
import com.scandit.shelf.sdk.core.area.LocationSelection;
import com.scandit.shelf.sdk.core.area.RectangularLocationSelection;
import com.scandit.shelf.sdk.core.common.geometry.FloatWithUnit;
import com.scandit.shelf.sdk.core.common.geometry.MeasureUnit;
import com.scandit.shelf.sdk.core.common.geometry.SizeWithUnit;
import com.scandit.shelf.sdk.core.ui.CaptureView;
import com.scandit.shelf.sdk.core.ui.viewfinder.RectangularViewfinder;
import com.scandit.shelf.sdk.core.ui.viewfinder.RectangularViewfinderStyle;
import com.scandit.shelf.sdk.core.ui.viewfinder.Viewfinder;
import com.scandit.shelf.sdk.core.ui.viewfinder.ViewfinderConfiguration;
import com.scandit.shelf.sdk.price.ui.AdvancedPriceCheckOverlay;
import com.scandit.shelf.sdk.price.ui.DefaultPriceCheckAdvancedOverlayListener;
import com.scandit.shelf.sdk.price.ui.PriceCheckOverlay;

/**
 * The main Activity that will setup a CaptureView and run price check.
 * Most of the work is delegated to the corresponding ViewModel.
 */
public class MainActivity extends CameraPermissionActivity {

    private MainActivityViewModel viewModel;
    private ConstraintLayout root;
    private CaptureView captureView;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = (new ViewModelProvider(this)).get(MainActivityViewModel.class);
        setContentView(R.layout.main_activity);
        root = findViewById(R.id.container);
        captureView = findViewById(R.id.capture_view);
        status = findViewById(R.id.status_text_view);
        observeLiveData();
        viewModel.authenticateAndFetchData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume price checking when Activity resumes.
        viewModel.resumePriceCheck();
    }

    @Override
    protected void onPause() {
        // Pause price checking when Activity pauses.
        viewModel.pausePriceCheck();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // Destroy price check to release all resources.
        viewModel.onDestroyPriceCheck();
        super.onDestroy();
    }

    @Override
    public void onCameraPermissionGranted() {
        captureView.setVisibility(View.VISIBLE);
        // As price check requires the camera feed, it can only be started once the camera
        // permission has been granted.
        viewModel.initPriceCheck(captureView, getDefaultOverlay(), getDefaultViewfinderConfiguration());
        viewModel.resumePriceCheck();
    }

    @Override
    public void onCameraPermissionDenied() {
        // Display short message on the screen that the camera permission has been rejected.
        // In your app, you might want to provide additional explanation to the user,
        // explaining why the permission is required, and request the permission again
        // or gracefully handle the permission denial.
        setStatus(R.string.camera_permission_denied);
    }

    private PriceCheckOverlay getDefaultOverlay() {
        return new AdvancedPriceCheckOverlay(new DefaultPriceCheckAdvancedOverlayListener());
    }

    private ViewfinderConfiguration getDefaultViewfinderConfiguration() {
        return new ViewfinderConfiguration(getDefaultViewfinder(), getDefaultLocationSelection());
    }

    private Viewfinder getDefaultViewfinder() {
        RectangularViewfinder viewfinder = new RectangularViewfinder(RectangularViewfinderStyle.ROUNDED);
        viewfinder.setSize(
                new SizeWithUnit(
                        new FloatWithUnit(0.9f, MeasureUnit.FRACTION),
                        new FloatWithUnit(0.3f, MeasureUnit.FRACTION)
                )
        );
        viewfinder.setDimming(0.6f);
        return viewfinder;
    }

    private LocationSelection getDefaultLocationSelection() {
        return RectangularLocationSelection.withSize(
                new SizeWithUnit(
                        new FloatWithUnit(0.9f, MeasureUnit.FRACTION),
                        new FloatWithUnit(0.3f, MeasureUnit.FRACTION)
                )
        );
    }

    private void observeLiveData() {
        // Observe the LivaData that posts messages to be displayed on a snackbar.
        viewModel.getSnackbarData().observe(this, snackbarData -> {
            if (snackbarData != null) showMessage(snackbarData);
        });

        viewModel.getStatus().observe(this, newStatus -> {
            // Observe the LiveData that holds price checking results.
            switch (newStatus) {
                case INIT:
                    setStatus(R.string.init);
                    break;
                case READY:
                    onStatusReady();
                    break;
                case AUTH_FAILED:
                    setStatus(R.string.authentication_failed);
                    break;
                case STORE_DOWNLOAD_FAILED:
                    setStatus(R.string.store_download_failed);
                    break;
                case STORES_EMPTY:
                    setStatus(R.string.stores_empty);
                    break;
                case CATALOG_DOWNLOAD_FAILED:
                    setStatus(R.string.catalog_update_failed);
                    break;
            }
        });

        // Observe the LiveData that holds current store to set ActionBar title.
        viewModel.getCurrentStore().observe(this, store -> getSupportActionBar().setTitle(store.getName()));
    }

    private void onStatusReady() {
        clearStatus();
        // Check for camera permission and request it, if it hasn't yet been granted.
        // Once we have the permission the onCameraPermissionGranted() method will be called.
        requestCameraPermission();
    }

    private void showMessage(SnackbarData snackbarData) {
        View topSnackbar = root.findViewById(R.id.top_snackbar);
        Snackbar snackbar = Snackbar.make(topSnackbar, snackbarData.getMessage(), Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(ContextCompat.getColor(this, snackbarData.getBackgroundColorResId()));
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setMaxLines(3);
        snackbar.show();
    }

    private void setStatus(@StringRes int message) {
        status.setText(message);
    }

    private void clearStatus() {
        status.setText("");
    }
}
