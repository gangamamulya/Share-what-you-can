package com.example.sharewhatyoucanproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sharewhatyoucanproject.R
import android.content.Intent
import com.example.sharewhatyoucanproject.HomescreenActivity
import com.example.sharewhatyoucanproject.MainActivity
import com.example.sharewhatyoucanproject.DonorActivity

import android.widget.EditText
import com.google.firebase.storage.StorageReference
import com.google.firebase.database.DatabaseReference
import android.app.ProgressDialog
import android.Manifest.permission
import android.os.Build

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.database.FirebaseDatabase
import android.widget.Toast
import android.content.pm.PackageManager
import android.annotation.TargetApi
import android.content.DialogInterface
import android.app.Activity
import android.graphics.Bitmap
import android.provider.MediaStore
import android.content.ContentResolver
import android.webkit.MimeTypeMap
import com.google.android.gms.tasks.OnSuccessListener

import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.storage.OnProgressListener
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import android.os.IBinder
import android.view.View
import android.widget.Button


class HomescreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homescreen)
        //donor action
        val donorbtn = findViewById<View>(R.id.donor_btn) as Button
        donorbtn.setOnClickListener { // Do something in response to button click
            startActivity(Intent(this@HomescreenActivity, DonorActivity::class.java))

        }

        //receiver action
        val receiverbtn = findViewById<View>(R.id.receiver_btn) as Button
        receiverbtn.setOnClickListener {

            // TODO: Implement retreival of images from firebase and display it in dashboard

        }
    }
}
