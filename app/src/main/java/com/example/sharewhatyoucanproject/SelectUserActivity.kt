package com.example.sharewhatyoucanproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SelectUserActivity : AppCompatActivity() {
    lateinit var pantry: LinearLayout
    lateinit var individual: LinearLayout
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_user)

        pantry = findViewById(R.id.pantry)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        individual = findViewById(R.id.individual)

        pantry.setOnClickListener(
            View.OnClickListener {
                sendtodb("pantry")
            },
        )

        individual.setOnClickListener(
            View.OnClickListener {
                sendtodb("indivtypeidual")
            },
        )
    }

    fun sendtodb(type: String) {
        db.collection("users")
            .document(auth.currentUser!!.uid)
            .update("type", type)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val i = Intent(this, DonorDrawerActivity::class.java)
                    startActivity(i)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Failed to update", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
