package com.iprism.adbots

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.iprism.adbots.ui.theme.AdbotsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val videoList = listOf(
            "https://littlepebbles.co.in/videos/video1.mp4",
            "https://littlepebbles.co.in/videos/video2.mp4",
            "https://littlepebbles.co.in/videos/video3.mp4"
        )

        setContent {
            AdbotsTheme {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
                    VideoPlayer(videoUrls = videoList)
                }
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(videoUrls: List<String>) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Initialize ExoPlayer
    val exoPlayer = remember {
        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(15000, 50000, 1500, 2000)
            .build()

        ExoPlayer.Builder(context)
            .setLoadControl(loadControl)
            .build().apply {
                val mediaItems = videoUrls.map { url -> MediaItem.fromUri(url) }
                setMediaItems(mediaItems)
                repeatMode = ExoPlayer.REPEAT_MODE_ALL
                videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                prepare()
                playWhenReady = true
            }
    }

    // Lifecycle management to handle play/pause correctly
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> exoPlayer.play()
                Lifecycle.Event.ON_STOP -> exoPlayer.pause()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { ctx ->
            // Create the PlayerView programmatically (Completely Compose/Kotlin)
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = true
                
                // Set the shutter background color to transparent to help with video visibility
                setShutterBackgroundColor(android.graphics.Color.TRANSPARENT)
                
                // Hardware and TV optimization
                keepScreenOn = true
                isFocusable = true
                isFocusableInTouchMode = true
                requestFocus()
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
