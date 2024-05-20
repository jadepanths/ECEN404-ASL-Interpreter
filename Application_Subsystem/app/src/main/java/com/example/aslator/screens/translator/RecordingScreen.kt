@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.aslator.screens.translator

import android.Manifest
import android.net.Uri
import android.util.Size
import androidx.camera.core.CameraInfo
import androidx.compose.material.Text
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.aslator.MainViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.example.aslator.R
import com.example.aslator.navigateTo
import com.example.aslator.shared.PreviewState
import com.example.aslator.shared.composables.*
import com.example.aslator.shared.composables.RequestPermission
import com.example.aslator.shared.utils.LocalVideoCaptureManager
import com.example.aslator.shared.utils.VideoCaptureManager

@Composable
internal fun RecordingScreen(
    navController: NavHostController,
    factory: ViewModelProvider.Factory,
    viewModel : MainViewModel,
    recordingViewModel: RecordingViewModel = viewModel(factory = factory),
    onShowMessage: (message: Int) -> Unit
) {
    val state by recordingViewModel.state.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val listener = remember(recordingViewModel) {
        object : VideoCaptureManager.Listener {
            override fun onInitialised(cameraLensInfo: HashMap<Int, CameraInfo>) {
                recordingViewModel.onEvent(RecordingViewModel.Event.CameraInitialized(cameraLensInfo))
            }
            override fun onProgress(progress: Int) {
                recordingViewModel.onEvent(RecordingViewModel.Event.OnProgress(progress))
            }
            override fun recordingCompleted(outputUri: Uri) {
                recordingViewModel.onEvent(RecordingViewModel.Event.RecordingEnded(outputUri))
            }
            override fun onError(throwable: Throwable?) {
                recordingViewModel.onEvent(RecordingViewModel.Event.Error(throwable))
            }
        }
    }

    val captureManager = remember(recordingViewModel) {
        VideoCaptureManager.Builder(context)
            .registerLifecycleOwner(lifecycleOwner)
            .create()
            .apply { this.listener = listener }
    }

    val permissions = remember { listOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO) }
    HandlePermissionsRequest(permissions = permissions, permissionsHandler = recordingViewModel.permissionsHandler)

    CompositionLocalProvider(LocalVideoCaptureManager provides captureManager) {
        VideoScreenContent(
            allPermissionsGranted = state.multiplePermissionsState?.allPermissionsGranted ?: false,
            cameraLens = state.lens,
            recordedLength = state.recordedLength,
            recordingStatus = state.recordingStatus,
            onEvent = recordingViewModel::onEvent,
            viewModel = viewModel
        )
    }

    LaunchedEffect(recordingViewModel) {
        recordingViewModel.effect.collect {
            when (it) {
                is RecordingViewModel.Effect.NavigateTo -> navController.navigateTo(it.route)
                is RecordingViewModel.Effect.ShowMessage -> onShowMessage(it.message)
                is RecordingViewModel.Effect.RecordVideo -> captureManager.startRecording(it.filePath)
                RecordingViewModel.Effect.StopRecording -> captureManager.stopRecording()
            }
        }
    }
}

@Composable
private fun VideoScreenContent(
    allPermissionsGranted: Boolean,
    cameraLens: Int?,
    recordedLength: Int,
    recordingStatus: RecordingViewModel.RecordingStatus,
    onEvent: (RecordingViewModel.Event) -> Unit,
    viewModel : MainViewModel
) {
    if (!allPermissionsGranted) {
        RequestPermission(message = R.string.request_permissions) {
            onEvent(RecordingViewModel.Event.PermissionRequired)
        }
    } else {
        if (viewModel.userIsAuthenticated) {
            Box(modifier = Modifier.fillMaxWidth().padding(bottom = 50.dp ) ) {
                cameraLens?.let {
                    CameraPreview(lens = it)
                    if (recordingStatus == RecordingViewModel.RecordingStatus.Idle) {

                    }
                    if (recordedLength > 0) {
                        Timer(
                            modifier = Modifier.align(Alignment.TopCenter),
                            seconds = recordedLength
                        )
                    }
                    RecordFooter(
                        modifier = Modifier.align(Alignment.BottomStart),
                        recordingStatus = recordingStatus,
                        onRecordTapped = { onEvent(RecordingViewModel.Event.RecordTapped) },
                        onStopTapped = { onEvent(RecordingViewModel.Event.StopTapped) }
                    )
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxWidth().padding(bottom = 50.dp ) ) {
                Spacer( modifier = Modifier.height( 400.dp ) )
                Text(
                    text = "Please sign into your account to use the translation feature.",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.align( Alignment.Center ),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
internal fun RecordFooter(
    modifier: Modifier = Modifier,
    recordingStatus: RecordingViewModel.RecordingStatus,
    onRecordTapped: () -> Unit,
    onStopTapped: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 24.dp)
            .then(modifier)
    ) {
        when (recordingStatus) {
            RecordingViewModel.RecordingStatus.Idle -> {
                CameraRecordIcon(modifier = Modifier.align(Alignment.Center), onTapped = onRecordTapped)
            }
            RecordingViewModel.RecordingStatus.InProgress -> {
                CameraStopIcon(modifier = Modifier.align(Alignment.Center), onTapped = onStopTapped)
            }
        }
    }
}

@Composable
private fun CameraPreview(lens: Int) {
    val captureManager = LocalVideoCaptureManager.current
    BoxWithConstraints {
        AndroidView(
            factory = {
                captureManager.showPreview(
                    PreviewState(
                        cameraLens = lens,
                        size = Size(this.minWidth.value.toInt(), this.maxHeight.value.toInt())
                    )
                )
            },
            modifier = Modifier.fillMaxSize(),
            update = {
                captureManager.updatePreview(
                    PreviewState(cameraLens = lens),
                    it
                )
            }
        )
    }
}