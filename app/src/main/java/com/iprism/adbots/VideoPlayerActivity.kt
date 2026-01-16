package com.iprism.adbots

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class VideoPlayerActivity : ComponentActivity() {

    private var player: ExoPlayer? = null
    private lateinit var playerView: PlayerView

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        playerView = findViewById(R.id.player_view)

        // Detect if device is TV
        val isTV = packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK)

        // Adjust rotation and scale for Mobile vs TV
        if (!isTV) {
            playerView.rotation = 0f
        } else {
            // Assuming 270 is your TV requirement based on previous edits
            playerView.rotation = 270f
            playerView.scaleX = 1.8f
            playerView.scaleY = 1.8f
        }

        initializePlayer(isTV)
    }

    @OptIn(UnstableApi::class)
    private fun initializePlayer(isTV: Boolean) {
        val videoList = listOf(
            /*"https://littlepebbles.co.in/videos/video1.mp4",
            "https://littlepebbles.co.in/videos/video2.mp4",
            "https://littlepebbles.co.in/videos/video3.mp4"*/
            "https://kaamhaina.in/assets/admin/newvideos/1.mp4",
            "https://kaamhaina.in/assets/admin/newvideos/2.mp4",
            "https://kaamhaina.in/assets/admin/newvideos/3.mp4"
        )

        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(30000, 60000, 5000, 10000)
            .build()

        player = ExoPlayer.Builder(this)
            .setLoadControl(loadControl)
            .build()
            .apply {
                val mediaItems = videoList.map { url -> MediaItem.fromUri(url) }
                setMediaItems(mediaItems)
                repeatMode = ExoPlayer.REPEAT_MODE_ALL

                videoScalingMode = if (isTV) {
                    C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                } else {
                    C.VIDEO_SCALING_MODE_SCALE_TO_FIT
                }

                prepare()
                playWhenReady = true
            }

        playerView.player = player
        playerView.keepScreenOn = true

        if (isTV) {
            playerView.requestFocus()
        }
    }

    override fun onStart() {
        super.onStart()
        player?.play()
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
    }
}
