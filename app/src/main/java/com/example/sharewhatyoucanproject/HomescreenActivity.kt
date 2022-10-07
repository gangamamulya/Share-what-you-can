package com.example.sharewhatyoucanproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.sharewhatyoucanproject.login.LoginActivity

class HomescreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homescreen)
        // donor action
        val button = findViewById<View>(R.id.donerimg) as ImageView
        button.setOnClickListener { // Do something in response to button click
            startActivity(Intent(this@HomescreenActivity, LoginActivity::class.java))
        }

        // receiver action
        val button2 = findViewById<View>(R.id.receiverimg) as ImageView
        button2.setOnClickListener { // Do something in response to button click
            startActivity(Intent(this@HomescreenActivity, DashboardActivity::class.java))
        }
    }
}
