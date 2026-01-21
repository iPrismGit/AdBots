package com.iprism.adbots.activities

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import com.iprism.adbots.databinding.ActivityVideoPlayerBinding
import com.iprism.adbots.models.ResponseItem
import com.iprism.adbots.repository.AdsRepository
import com.iprism.adbots.utils.Constants
import com.iprism.adbots.utils.UiState
import com.iprism.adbots.utils.hideProgress
import com.iprism.adbots.utils.showProgress
import com.iprism.adbots.viewmodels.ViewModelFactory
import com.iprism.adbots.viewmodels.WalletViewModel

class VideoPlayerActivity : ComponentActivity() {

    private var player: ExoPlayer? = null
    private lateinit var binding: ActivityVideoPlayerBinding
    private lateinit var viewModel: WalletViewModel

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel()
        observeResponse()
        fetchViewAds()
    }



    @OptIn(UnstableApi::class)
    private fun initializePlayer(isTV: Boolean, videos : List<ResponseItem>) {
        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(30000, 60000, 5000, 10000)
            .build()
        player = ExoPlayer.Builder(this)
            .setLoadControl(loadControl)
            .build()
            .apply {
                val mediaItems = videos.map { response -> MediaItem.fromUri(Constants.IMAGES_BASE_URL + response.adLink) }
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
        player!!.addListener(object : Player.Listener {

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    showOnlineStatus()
                } else {
                    showOfflineStatus()
                }
            }

          /*  override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    Player.STATE_READY -> {
                        if (player!!.playWhenReady) showOnlineStatus()
                    }
                    Player.STATE_BUFFERING,
                    Player.STATE_IDLE,
                    Player.STATE_ENDED -> {
                        showOfflineStatus()
                    }
                }
            }*/
        })
        binding.playerView.player = player
        binding.playerView.keepScreenOn = true
        if (isTV) {
            binding.playerView.requestFocus()
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

    private fun showOnlineStatus() {
        Log.d("VIDEO_STATUS", "🟢 ONLINE (Video Playing)")
    }

    private fun showOfflineStatus() {
        Log.d("VIDEO_STATUS", "⚪ OFFLINE (Video Not Playing)")
    }

    private fun initViewModel() {
        val repository = AdsRepository()
        val factory = ViewModelFactory { WalletViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[WalletViewModel::class.java]
    }

    @SuppressLint("SetTextI18n")
    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    Log.d("viewAdsResponse", result.data.response.toString())
                    val isTV = packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK)
                    if (!isTV) {
                        binding.playerView.rotation = 0f
                    } else {
                        binding.playerView.rotation = 270f
                        binding.playerView.scaleX = 1.8f
                        binding.playerView.scaleY = 1.8f
                    }
                    initializePlayer(isTV, result.data.response)
                    binding.progress.hideProgress()
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun fetchViewAds() {
        NetworkRetryHelper.checkAndCallWithRetry(this) {
            viewModel.fetchAds()
        }
    }
}
