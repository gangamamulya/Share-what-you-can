package com.example.sharewhatyoucanproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth

class HomescreenActivity : AppCompatActivity() {
    var firebaseAuth: FirebaseAuth? = null
    var donerimg: ImageView? = null
    var receiverimg: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homescreen)
        firebaseAuth = FirebaseAuth.getInstance()
        donerimg = findViewById(R.id.donerimg)
        receiverimg = findViewById(R.id.receiverimg)
        donerimg?.setOnClickListener(View.OnClickListener {
            val i = Intent(this@HomescreenActivity, LoginActivity::class.java)
            i.putExtra("type", 1)
            startActivity(i)
        })
        receiverimg?.setOnClickListener(View.OnClickListener {
            val i = Intent(this@HomescreenActivity, LoginActivity::class.java)
            i.putExtra("type", 2)
            startActivity(i)
        })
    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth?.currentUser != null) {
            val sh = getSharedPreferences("MySharedPref", MODE_PRIVATE)
            val value = sh.getInt("selector", 0)
            if (value == 1) {
                val i = Intent(this@HomescreenActivity, DrawerActivity::class.java)
                startActivity(i)
                finishAffinity()
            } else if (value == 2) {
                val i = Intent(this@HomescreenActivity, DrawerActivity2::class.java)
                startActivity(i)
                finishAffinity()
            }
        }
    }
}