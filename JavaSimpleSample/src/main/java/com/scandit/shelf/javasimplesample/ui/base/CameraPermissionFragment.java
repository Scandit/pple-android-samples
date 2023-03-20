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

package com.scandit.shelf.javasimplesample.ui.base;

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
