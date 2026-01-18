package com.iprism.adbots

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.iprism.adbots.databinding.ActivityLoginActiivtyBinding

class LoginActiivty : ComponentActivity() {

    private lateinit var binding: ActivityLoginActiivtyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginActiivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleConfirmBtn()
    }

    private fun handleConfirmBtn() {
        binding.confirmBtn.setOnClickListener{
            if (getCode() == "604020") {
                val user = User(this)
                user.loginUser()
                val intent = Intent(this, VideoPlayerActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please Enter Valid Code", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getCode() : String {
        return binding.codeEt.text.toString().trim()
    }
}