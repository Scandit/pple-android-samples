package com.scandit.shelf.kotlinapp.ui.base

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat

/**
 * A fragment to request the camera permission.
 */
abstract class CameraPermissionFragment : NavigationFragment() {

    /**
     * The launcher to request the user permission to use their device's camera.
     */
    private val requestCameraPermission: ActivityResultLauncher<String?> =
        registerForActivityResult(RequestPermission()) { isGranted: Boolean ->
            if (isGranted && isResumed) {
                onCameraPermissionGranted()
            }
        }

    protected fun requestCameraPermission() {
        /*
         * Check for camera permission and request it, if it hasn't yet been granted.
         * Once we have the permission start the capture process.
         */
        if (isCameraPermissionGranted()) {
            onCameraPermissionGranted()
        } else {
            requestCameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private fun isCameraPermissionGranted() =
        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED

    abstract fun onCameraPermissionGranted()
}
