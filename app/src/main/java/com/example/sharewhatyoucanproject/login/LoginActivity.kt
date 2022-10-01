package com.example.sharewhatyoucanproject.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sharewhatyoucanproject.DrawerActivity
import com.example.sharewhatyoucanproject.SelectUserActivity
import com.example.sharewhatyoucanproject.databinding.ActivityLoginBinding
import com.example.sharewhatyoucanproject.models.UserType
import com.example.sharewhatyoucanproject.utils.editSharedPreferencesSelector
import com.example.sharewhatyoucanproject.utils.showToast

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var loginViewModel: LoginViewModel
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        supportActionBar?.hide()

        loginViewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(),
        )[LoginViewModel::class.java]

        val type = intent.getIntExtra("type", 0)

        val typevar1 = UserType.values()[type]

        loginViewModel.type = typevar1

        loginViewModel.checkResult.observe(this) { result ->
            result?.let {
                progressDialog.dismiss()
                // this.showToast(result)
                // TODO show toast
                loginViewModel.checkResult.value = null
            }
        }

        loginViewModel.loginResult.observe(this) { loginResult ->
            loginResult?.let {
                progressDialog.dismiss()
                if (loginResult == "success") {
                    editSharedPreferencesSelector(this, loginViewModel.type)
                    val intent = Intent(this@LoginActivity, DrawerActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                } else {
                    this.showToast(loginResult)
                }
                loginViewModel.loginResult.value = null
            }
        }

        loginViewModel.createUserResult.observe(this) { createUserResult ->
            createUserResult?.let {
                if (createUserResult == "success") {
                    this.showToast("Created")
                    loginViewModel.saveUserData()
                } else {
                    progressDialog.dismiss()
                    this.showToast(createUserResult)
                }
                loginViewModel.createUserResult.value = null
            }
        }

        loginViewModel.saveUserResult.observe(this) { saveUserResult ->
            saveUserResult?.let {
                progressDialog.dismiss()
                if (saveUserResult == "success") {
                    editSharedPreferencesSelector(this, loginViewModel.type)
                    val intent = if (loginViewModel.type == UserType.DONER) {
                        Intent(this@LoginActivity, SelectUserActivity::class.java)
                    } else {
                        Intent(this, DrawerActivity::class.java)
                    }
                    startActivity(intent)
                    finishAffinity()
                } else {
                    this.showToast(saveUserResult)
                }
                loginViewModel.saveUserResult.value = null
            }
        }

        if (loginViewModel.type == UserType.DONER) {
            binding.mytv.text = "Donor"
        } else if (loginViewModel.type == UserType.RECEIVER) {
            binding.mytv.text = "Receiver"
        }

        binding.submitdevice.setOnClickListener {
            val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
            loginViewModel.deviceId = deviceId
            val email = "s$deviceId@gmail.com"
            progressDialog.show()
            loginViewModel.checkUser(email)
        }
    }
}
