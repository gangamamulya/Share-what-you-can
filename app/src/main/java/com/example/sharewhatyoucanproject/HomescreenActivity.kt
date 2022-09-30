package com.example.sharewhatyoucanproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sharewhatyoucanproject.databinding.ActivityHomescreenBinding
import com.example.sharewhatyoucanproject.donor.DrawerActivity
import com.example.sharewhatyoucanproject.login.LoginActivity
import com.example.sharewhatyoucanproject.models.UserType
import com.google.firebase.auth.FirebaseAuth

class HomescreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomescreenBinding

    var firebaseAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomescreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.donerimg.setOnClickListener {
            val i = Intent(this@HomescreenActivity, LoginActivity::class.java)
            i.putExtra("type", UserType.DONER.ordinal)
            startActivity(i)
        }
        binding.receiverimg.setOnClickListener {
            val i = Intent(this@HomescreenActivity, LoginActivity::class.java)
            i.putExtra("type", UserType.RECEIVER.ordinal)
            startActivity(i)
        }
    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth?.currentUser != null) {
            val sh = getSharedPreferences("MySharedPref", MODE_PRIVATE)
            val value = sh.getInt("selector", 0)
            val typevar = UserType.values()[value]
            if (typevar == UserType.DONER) {
                val i = Intent(this@HomescreenActivity, DrawerActivity::class.java)
                startActivity(i)
                finishAffinity()
            } else if (typevar == UserType.RECEIVER) {
                val i = Intent(this@HomescreenActivity, DrawerActivity2::class.java)
                startActivity(i)
                finishAffinity()
            }
        }
    }
}
