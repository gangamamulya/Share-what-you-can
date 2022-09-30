package com.example.sharewhatyoucanproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.nextButton)
        button.setOnClickListener {
            val intent = Intent(this@MainActivity, HomescreenActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        val value: String? = null
    }
}
