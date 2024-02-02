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

package com.scandit.shelf.kotlincomposesimplesample

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.scandit.shelf.sdk.catalog.Store
import com.scandit.shelf.sdk.core.ui.CaptureView
import com.scandit.shelf.sdk.core.ui.viewfinder.ViewfinderConfiguration
import com.scandit.shelf.sdk.price.ui.PriceCheckOverlay

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)

@Composable
fun MainScreen(
    viewModel: MainActivityViewModel = viewModel(),
    getOverlay: () -> PriceCheckOverlay,
    getViewfinderConfiguration: () -> ViewfinderConfiguration
) {
    val cameraPermissionState: PermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    val status: Status by viewModel.statusFlow.collectAsState()
    val snackbar: SnackbarData? by viewModel.snackbarFlow.collectAsState()
    val store: Store? by viewModel.currentStoreFlow.collectAsState()

    Scaffold(topBar = {
        TopAppBar(title = { Text(store?.name.orEmpty()) })
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (status) {
                Status.INIT -> Text(text = stringResource(id = R.string.init))
                Status.AUTH_FAILED -> Text(text = stringResource(id = R.string.authentication_failed))
                Status.STORE_DOWNLOAD_FAILED -> Text(text = stringResource(id = R.string.store_download_failed))
                Status.STORES_EMPTY -> Text(text = stringResource(id = R.string.stores_empty))
                Status.CATALOG_UPDATE_FAILED -> Text(text = stringResource(id = R.string.catalog_update_failed))
                Status.READY -> PriceCheckScreen(
                    cameraPermissionState = cameraPermissionState,
                    snackbar = snackbar,
                    onCreate = { view: CaptureView ->
                        viewModel.initPriceCheck(view, getOverlay(), getViewfinderConfiguration())
                        viewModel.resumePriceCheck()
                    },
                    onResume = { viewModel.resumePriceCheck() },
                    onPause = { viewModel.pausePriceCheck() },
                    onDestroy = { viewModel.onDestroyPriceCheck() }
                )
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PriceCheckScreen(
    cameraPermissionState: PermissionState,
    snackbar: SnackbarData?,
    onCreate: (CaptureView) -> Unit,
    onResume: () -> Unit,
    onPause: () -> Unit,
    onDestroy: () -> Unit
) {
    Box(modifier = Modifier) {
        PriceCheckView(
            cameraPermissionState = cameraPermissionState,
            onCreate = onCreate,
            onResume = onResume,
            onPause = onPause,
            onDestroy = onDestroy
        )
        snackbar?.let { Snackbar(it.message, it.backgroundColor) }
    }
}

@Composable
private fun Snackbar(message: String, snackBarColor: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = snackBarColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 32.dp)
    ) {
        Text(modifier = Modifier.padding(16.dp), text = message, fontSize = 12.sp)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PriceCheckView(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    cameraPermissionState: PermissionState,
    onCreate: (CaptureView) -> Unit,
    onResume: () -> Unit,
    onPause: () -> Unit,
    onDestroy: () -> Unit
) {
    // As price check requires the camera feed, it can only be started once the camera
    // permission has been granted.
    if (cameraPermissionState.status.isGranted) {
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> onResume() // Resume price checking when Activity resumes.
                    Lifecycle.Event.ON_PAUSE -> onPause() // Pause price checking when Activity pauses.
                    Lifecycle.Event.ON_DESTROY -> onDestroy() // Destroy price check to release all resources.
                    else -> Unit
                }
            }

            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context -> CaptureView(context).apply(onCreate) }
        )
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.camera_permission_denied))
            cameraPermissionState.launchPermissionRequest()
        }
    }
}
