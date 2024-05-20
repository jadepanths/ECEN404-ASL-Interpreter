package com.example.aslator.screens.translator

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.aslator.BottomNavItem
import com.example.aslator.MainViewModel
import com.example.aslator.R
import com.example.aslator.navigateTo
import com.example.aslator.shared.composables.CameraPauseIcon
import com.example.aslator.shared.composables.CameraPlayIcon
import com.example.aslator.shared.utils.LocalPlaybackManager
import com.example.aslator.shared.utils.NetUpload
import com.example.aslator.shared.utils.PlaybackManager
import com.example.aslator.shared.utils.StatTracker

@Composable
internal fun PlaybackScreen(
    filePath: String,
    navHostController: NavHostController,
    uploadManager: NetUpload,
    viewModel : MainViewModel,
    userStats : StatTracker,
    playbackViewModel: PlaybackViewModel = viewModel()
) {
    val state by playbackViewModel.state.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val listener = object : PlaybackManager.PlaybackListener {
        override fun onPrepared() {
            playbackViewModel.onEvent(PlaybackViewModel.Event.Prepared)
        }

        override fun onProgress(progress: Int) {
            playbackViewModel.onEvent(PlaybackViewModel.Event.OnProgress(progress))
        }

        override fun onCompleted() {
            playbackViewModel.onEvent(PlaybackViewModel.Event.Completed)
        }
    }

    val playbackManager = remember {
        PlaybackManager.Builder(context)
            .apply {
                this.uri = Uri.parse(filePath)
                this.listener = listener
                this.lifecycleOwner = lifecycleOwner
            }
            .build()
    }

    CompositionLocalProvider(LocalPlaybackManager provides playbackManager) {
        PlaybackScreenContent(state, filePath, navHostController, uploadManager, viewModel, userStats, playbackViewModel::onEvent)
    }

    LaunchedEffect(playbackViewModel) {
        playbackViewModel.effect.collect {
            when (it) {
                PlaybackViewModel.Effect.NavigateUp -> navHostController.navigateTo(
                    BottomNavItem.Profile.screen_route
                )
                PlaybackViewModel.Effect.Pause -> playbackManager.pausePlayback()
                PlaybackViewModel.Effect.Play -> playbackManager.start(state.playbackPosition)
            }
        }
    }
}

@Composable
private fun PlaybackScreenContent(
    state: PlaybackViewModel.State,
    filePath: String,
    navHostController: NavHostController,
    uploadManager: NetUpload,
    viewModel: MainViewModel,
    userStats : StatTracker,
    onEvent: (PlaybackViewModel.Event) -> Unit
) {
    val playbackManager = LocalPlaybackManager.current

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 50.dp)
        .background(color = Color.Black)) {
        AndroidView(modifier = Modifier.fillMaxSize(), factory = { playbackManager.videoView })
        when (state.playbackStatus) {
            PlaybackViewModel.PlaybackStatus.Idle -> {
                IconButton( //Used to reject video and take a new one
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 30.dp),
                    onClick = { navHostController.navigate( BottomNavItem.Video.screen_route ) },
                    content = {
                        Image(
                            modifier = Modifier.size(60.dp),
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = null,
                        )
                    }
                )
                CameraPlayIcon(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 30.dp)) {
                    onEvent(PlaybackViewModel.Event.PlayTapped)
                }
                IconButton( //Used to approve video and send to cloud
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 30.dp),
                    onClick = {
                        navHostController.navigate( BottomNavItem.Upload.screen_route )
                        uploadManager.setAuthUsername( viewModel.user.email )
                        uploadManager.setNavHost( navHostController )
                        uploadManager.setFilePath( filePath )
                        uploadManager.networkUploadVideo()
                    },
                    content = {
                        Image(
                            modifier = Modifier.size(60.dp),
                            painter = painterResource(id = R.drawable.ic_accept),
                            contentDescription = null,
                        )
                    }
                )
            }
            PlaybackViewModel.PlaybackStatus.InProgress -> {
                CameraPauseIcon(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 30.dp)) {
                    onEvent(PlaybackViewModel.Event.PauseTapped)
                }
            }
            else -> {
                CircularProgressIndicator()
            }
        }
    }
}