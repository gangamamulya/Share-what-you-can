package com.example.sharewhatyoucanproject.donor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sharewhatyoucanproject.R

class PostsActivity : AppCompatActivity()
{
    companion object {
        fun navigate(activity: AppCompatActivity) {
            val intent = Intent(activity, PostsActivity::class.java)
            activity.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)
    }
}
