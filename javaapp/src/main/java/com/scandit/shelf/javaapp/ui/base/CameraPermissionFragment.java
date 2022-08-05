package com.scandit.shelf.javaapp.ui.base;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

/**
 * A fragment to request the camera permission.
 */
public abstract class CameraPermissionFragment extends NavigationFragment {

    /**
     * The launcher to request the user permission to use their device's camera.
     */
    private final ActivityResultLauncher<String> requestCameraPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted && isResumed()) {
                    onCameraPermissionGranted();
                }
            });

    protected void requestCameraPermission() {
        /*
         * Check for camera permission and request it, if it hasn't yet been granted.
         * Once we have the permission start the capture process.
         */
        if (isCameraPermissionGranted()) {
            onCameraPermissionGranted();
        } else {
            requestCameraPermission.launch(Manifest.permission.CAMERA);
        }
    }

    private boolean isCameraPermissionGranted() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    public abstract void onCameraPermissionGranted();
}
