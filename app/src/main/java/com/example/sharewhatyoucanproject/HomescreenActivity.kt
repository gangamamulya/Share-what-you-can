package com.example.sharewhatyoucanproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.sharewhatyoucanproject.login.LoginActivity
import com.example.sharewhatyoucanproject.models.UserType

class HomescreenActivity : AppCompatActivity() {
    companion object {
        fun navigate(activity: AppCompatActivity) {
            val intent = Intent(activity, HomescreenActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homescreen)
        // donor action
        val button = findViewById<View>(R.id.donerimg) as ImageView
        button.setOnClickListener { // Do something in response to button click
            LoginActivity.navigate(this@HomescreenActivity, UserType.DONOR.ordinal)

        }

        // receiver action
        val button2 = findViewById<View>(R.id.receiverimg) as ImageView
        button2.setOnClickListener { // Do something in response to button click
            DashboardActivity.navigate(this@HomescreenActivity, UserType.RECEIVER.ordinal)
        }
    }
}
