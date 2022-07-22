package com.example.sharewhatyoucanproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.ImageView

class HomescreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homescreen)
        //donor action
        val button = findViewById<View>(R.id.donerimg) as ImageView
        button.setOnClickListener { // Do something in response to button click
            startActivity(Intent(this@HomescreenActivity, DonorActivity::class.java))
        }

        //receiver action
        val button2 = findViewById<View>(R.id.receiverimg) as ImageView
        button2.setOnClickListener { // Do something in response to button click
            startActivity(Intent(this@HomescreenActivity, DashboardActivity::class.java))
        }
    }
}