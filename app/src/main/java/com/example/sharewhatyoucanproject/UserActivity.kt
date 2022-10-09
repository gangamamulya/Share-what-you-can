package com.example.sharewhatyoucanproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class UserActivity : AppCompatActivity()
{
    companion object {
        fun navigate(activity: AppCompatActivity) {
            val intent = Intent(activity, UserActivity::class.java)
            activity.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
    }
}
