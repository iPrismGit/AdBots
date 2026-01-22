package com.iprism.adbots.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.iprism.adbots.databinding.ActivityLoginActiivtyBinding
import com.iprism.adbots.models.login.LoginRequest
import com.iprism.adbots.repository.AdsRepository
import com.iprism.adbots.repository.AuthRepository
import com.iprism.adbots.utils.UiState
import com.iprism.adbots.utils.User
import com.iprism.adbots.utils.hideProgress
import com.iprism.adbots.utils.showProgress
import com.iprism.adbots.viewmodels.AdsViewModel
import com.iprism.adbots.viewmodels.LoginViewModel
import com.iprism.adbots.viewmodels.ViewModelFactory

class LoginActiivty : ComponentActivity() {

    private lateinit var binding: ActivityLoginActiivtyBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginActiivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleConfirmBtn()
        initViewModel()
        observeResponse()
    }

    private fun handleConfirmBtn() {
        binding.confirmBtn.setOnClickListener{
            if (getId().isEmpty()) {
                Toast.makeText(this, "Please Enter Id", Toast.LENGTH_LONG).show()
            } else if (getCode() != "604020") {
                Toast.makeText(this, "Please Enter Valid Password", Toast.LENGTH_LONG).show()
            } else {
                login()
            }
        }
    }

    private fun getCode() : String {
        return binding.codeEt.text.toString().trim()
    }

    private fun getId() : String {
        return binding.idEt.text.toString().trim()
    }

    private fun initViewModel() {
        val repository = AuthRepository()
        val factory = ViewModelFactory { LoginViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }

    @SuppressLint("SetTextI18n")
    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    val user = User(this)
                    user.loginUser()
                    val intent = Intent(this, VideoPlayerActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun login() {
        val req = LoginRequest(getId(), getCode())
        NetworkRetryHelper.checkAndCallWithRetry(this, req) {
            viewModel.login(req)
        }
    }
}