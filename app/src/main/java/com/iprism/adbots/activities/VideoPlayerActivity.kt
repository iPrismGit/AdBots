package com.iprism.adbots.activities

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.iprism.adbots.R
import com.iprism.adbots.databinding.ActivityVideoPlayerBinding
import com.iprism.adbots.models.Ads
import com.iprism.adbots.models.ResponseItem
import com.iprism.adbots.models.ViewAdsRequest
import com.iprism.adbots.models.updateadreputation.UpdateAdReputationRequest
import com.iprism.adbots.models.updatedevicestatus.UpdateDeviceStatusRequest
import com.iprism.adbots.repository.AdsRepository
import com.iprism.adbots.utils.Constants
import com.iprism.adbots.utils.DeviceStatusWorker
import com.iprism.adbots.utils.UiState
import com.iprism.adbots.utils.User
import com.iprism.adbots.utils.getUserDetails
import com.iprism.adbots.utils.hideProgress
import com.iprism.adbots.utils.showProgress
import com.iprism.adbots.viewmodels.ViewModelFactory
import com.iprism.adbots.viewmodels.AdsViewModel
import java.util.concurrent.TimeUnit

class VideoPlayerActivity : ComponentActivity() {

    private var player: ExoPlayer? = null
    private lateinit var binding: ActivityVideoPlayerBinding
    private lateinit var viewModel: AdsViewModel

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel()
        observeResponse()
        observeUpdateDeviceResponseResponse()
        fetchViewAds()
        updateDeviceStatus("online")
    }

    @OptIn(UnstableApi::class)
    private fun initializePlayer(isTV: Boolean, videos: List<Ads>) {
        player?.release()
        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(30000, 60000, 5000, 10000)
            .build()
        player = ExoPlayer.Builder(this)
            .setLoadControl(loadControl)
            .build()
            .apply {
                addListener(object : Player.Listener {

                    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                        super.onMediaItemTransition(mediaItem, reason)
                        mediaItem?.mediaId?.let { adId ->
                            updateAdReputation(adId)
                        }
                    }

                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        if (isPlaying) {
                            //updateDeviceStatus("online")
                        } else {
                            //updateDeviceStatus("offline")
                        }
                    }

                    override fun onPlaybackStateChanged(state: Int) {
                        if (state == Player.STATE_ENDED) {
                            fetchViewAds()
                        }
                    }
                })
                val mediaItems =
                    videos.map { response ->
                        MediaItem.Builder()
                            .setUri(Constants.IMAGES_BASE_URL + response.adLink)
                            .setMediaId(response.id)
                            .build()
                    }
                setMediaItems(mediaItems)
                repeatMode = ExoPlayer.REPEAT_MODE_OFF
                videoScalingMode = if (isTV) {
                    C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                } else {
                    C.VIDEO_SCALING_MODE_SCALE_TO_FIT
                }
                prepare()
                playWhenReady = true
            }
        binding.playerView.player = player
        binding.playerView.keepScreenOn = true
        if (isTV) {
            binding.playerView.requestFocus()
        }
        addWatermark(isTV)
    }

    @OptIn(UnstableApi::class)
    private fun addWatermark(isTV: Boolean) {
        val logo = ImageView(this).apply {
            setImageResource(R.drawable.adbots2)
            layoutParams = FrameLayout.LayoutParams(200, 200).apply {
                if (isTV) {
                    gravity = Gravity.TOP or Gravity.CENTER
                    setMargins(0, 12, 0, 0)
                } else{
                    gravity = Gravity.TOP or Gravity.END
                    setMargins(0, 200, 32, 0)
                }
            }
        }

        binding.playerView.post {
            binding.playerView.overlayFrameLayout?.removeAllViews()
            binding.playerView.overlayFrameLayout?.addView(logo)

           /* if (isTV) {
                // overlay does NOT inherit rotation
                logo.rotation = binding.playerView.rotation
            }*/
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("VideoPlayerActivity", "onResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("VideoPlayerActivity", "onRestart")
        updateDeviceStatus("online")
    }

    override fun onPause() {
        super.onPause()
        Log.d("VideoPlayerActivity", "onPause")
    }

    override fun onStart() {
        super.onStart()
        player?.play()
        Log.d("VideoPlayerActivity", "onStart")
    }

    override fun onStop() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<DeviceStatusWorker>()
            .setConstraints(constraints)
            .build()
        /*val workRequest = OneTimeWorkRequestBuilder<DeviceStatusWorker>()
            .setInitialDelay(15, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                30,
                TimeUnit.SECONDS
            )
            .build()*/

        WorkManager.getInstance(applicationContext).enqueue(workRequest)
        Log.d("VideoPlayerActivity", "onStop")
        super.onStop()
        player?.pause()
    }

    override fun onDestroy() {
        Log.d("VideoPlayerActivity", "onDestroy")
        super.onDestroy()
        player?.release()
        player = null
    }

    private fun initViewModel() {
        val repository = AdsRepository()
        val factory = ViewModelFactory { AdsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[AdsViewModel::class.java]
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
                    initializePlayer(isTV, result.data.response.ads)
                    binding.progress.hideProgress()
                }

                is UiState.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                    binding.progress.hideProgress()
                    if (result.message.contains("Your account is inactive") || result.message.contains("token not verified")) {
                        val user = User(applicationContext)
                        user.logoutUser()
                    }
                }
            }
        }
    }

    private fun fetchViewAds() {
        val userDetails = getUserDetails()
        val request = ViewAdsRequest(userDetails[User.ID].toString(), userDetails[User.AUTH_TOKEN].toString())
        NetworkRetryHelper.checkAndCallWithRetry(this) {
            viewModel.fetchAds(request)
        }
        Log.d("requestLoading", request.toString())
    }

    @SuppressLint("SetTextI18n")
    private fun observeUpdateDeviceResponseResponse() {
        viewModel.updateDeviceStatusResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                }

                is UiState.Success -> {
                    Log.d("updateDeviceStatusResponse", result.data.toString())
                }

                is UiState.Error -> {
                    Log.d("updateDeviceStatusResponse", result.message)
                }
            }
        }
    }

    private fun updateDeviceStatus(status: String) {
        val userDetails = getUserDetails()
        val request = UpdateDeviceStatusRequest(status, userDetails[User.ID].toString())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) {
            viewModel.updateDeviceStatus(request)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun updateAdReputation(adId : String) {
        val userDetails = getUserDetails()
        val request = UpdateAdReputationRequest(adId, userDetails[User.ID].toString())
        NetworkRetryHelper.checkAndCallWithRetry(this, request) {
            viewModel.updateAdReputation(request)
        }
        Log.d("requestLoading", request.toString())
    }
}
