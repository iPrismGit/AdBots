package com.iprism.adbots.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.iprism.adbots.databinding.ActivitySplashBinding
import com.iprism.adbots.utils.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : ComponentActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch {
            delay(1500)
            val user = User(this@SplashActivity)
            Log.d("userDetails", user.getUserDetails().toString() + user.isUserLoggedIn() + user.isAddress())
            if (user.isUserLoggedIn()) {
                startActivity(Intent(this@SplashActivity, VideoPlayerActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashActivity, LoginActiivty::class.java))
                finish()
            }
        }
    }
}