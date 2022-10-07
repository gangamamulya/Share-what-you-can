package com.example.sharewhatyoucanproject.login

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sharewhatyoucanproject.DonorDrawerActivity
import com.example.sharewhatyoucanproject.R
import com.example.sharewhatyoucanproject.SelectUserActivity
import com.example.sharewhatyoucanproject.databinding.ActivityLoginBinding
import com.example.sharewhatyoucanproject.models.UserType
import com.example.sharewhatyoucanproject.utils.getUserType
import com.example.sharewhatyoucanproject.utils.showToast
import com.google.android.material.progressindicator.CircularProgressIndicator

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var loginViewModel: LoginViewModel
    lateinit var circularProgressIndicator: CircularProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        circularProgressIndicator = findViewById(R.id.progress_circular)
        supportActionBar?.hide()
        loginViewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(this.application),
        )[LoginViewModel::class.java]
        val typevar1 = getUserType(intent)

        loginViewModel.type = typevar1

        loginViewModel.authenticationResult.observe(this) { result ->
            when (result) {
                is AuthenticationResult.Fail -> {
                    circularProgressIndicator.visibility = GONE
                    showToast(result.message)
                }
                is AuthenticationResult.LoginSuccess -> {
                    circularProgressIndicator.visibility = GONE
                    val intent = Intent(this@LoginActivity, DonorDrawerActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
                is AuthenticationResult.SignUpSuccess -> {
                    showToast("Created")
                }

                is AuthenticationResult.SaveDataSuccess -> {
                    val intent = if (loginViewModel.type == UserType.DONOR) {
                        Intent(this@LoginActivity, SelectUserActivity::class.java)
                    } else {
                        Intent(this, DonorDrawerActivity::class.java)
                    }
                    startActivity(intent)
                    finishAffinity()
                }
            }
        }

        if (loginViewModel.type == UserType.DONOR) {
            binding.mytv.text = "Donor"
        } else if (loginViewModel.type == UserType.RECEIVER) {
            binding.mytv.text = "Receiver"
        }

        binding.submitdevice.setOnClickListener {
            val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
            loginViewModel.deviceId = deviceId
            circularProgressIndicator.visibility = VISIBLE
            loginViewModel.checkUser(deviceId)
        }
    }
}
