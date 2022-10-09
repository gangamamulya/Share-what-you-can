package com.example.sharewhatyoucanproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class RequestActivity : AppCompatActivity() {
    companion object {
        private const val ARG_USER_TYPE = "ARG_USER"
        fun navigate(activity: AppCompatActivity, user: String) {
            val intent = Intent(activity, RequestActivity::class.java).apply {
                putExtra(ARG_USER_TYPE, user)
            }
            activity.startActivity(intent)
        }
    }
}
