package com.example.sharewhatyoucanproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity()
{
    companion object {
        const val ARG_USER_TYPE = "type"
        fun navigate(activity: AppCompatActivity, userType: Int) {
            val intent = Intent(activity, DashboardActivity::class.java).apply {
                putExtra(ARG_USER_TYPE, userType)
            }
            activity.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
    }
}
