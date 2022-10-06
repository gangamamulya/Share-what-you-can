package com.example.sharewhatyoucanproject

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.sharewhatyoucanproject.databinding.ActivityDonorBinding
import com.example.sharewhatyoucanproject.utils.PERMISSION_ALL
import com.example.sharewhatyoucanproject.utils.foodTypes
import com.example.sharewhatyoucanproject.utils.hasPermissions
import com.example.sharewhatyoucanproject.utils.isGPSEnabled
import com.example.sharewhatyoucanproject.utils.locationPermissions
import com.example.sharewhatyoucanproject.utils.showToast
import com.google.android.gms.location.LocationRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DonorActivity : AppCompatActivity() {

    lateinit var donorViewModel: DonorViewModel
    private lateinit var binding: ActivityDonorBinding
    lateinit var db: FirebaseFirestore
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var locationRequest: LocationRequest
    lateinit var arrayAdapter: ArrayAdapter<String>
    lateinit var circularProgressIndicator: com.google.android.material.progressindicator.CircularProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        circularProgressIndicator = findViewById(R.id.progress_circular)
        locationRequest = LocationRequest.create()
        supportActionBar?.hide()
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        db = FirebaseFirestore.getInstance()

        donorViewModel = ViewModelProvider(
            this,
            DonorViewModelFactory(applicationContext, db, storageReference),
        )[DonorViewModel::class.java]

        donorViewModel.currentLocation.observe(this) {
            it?.let {
                donorViewModel.uploadImage()
            }
        }
        donorViewModel.imageUrl.observe(this) {
            it?.let { donorViewModel.saveData() }
        }
        donorViewModel.saveData.observe(this) {
            it?.let {
                if (it == "success") {
                    applicationContext.showToast("Post Added")
                    finish()
                } else {
                    circularProgressIndicator.visibility = View.GONE
                    applicationContext.showToast(it)
                }
            }
        }

        binding.foodimg.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0)
        }

        arrayAdapter = ArrayAdapter<String>(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            foodTypes,
        )
        binding.spinner.adapter = arrayAdapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long,
            ) {
                foodTypes[position].also {
                    donorViewModel.foodType = it
                    applicationContext.showToast(it)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.uploadbtn.setOnClickListener {
            donorViewModel.title = binding.titleet.text.toString()
            donorViewModel.description = binding.descet.text.toString()
            donorViewModel.hourSet = binding.hourset.text.toString()

            if (!donorViewModel.isDataCompleted()) {
                applicationContext.showToast("Please Fill All Fields")
                return@setOnClickListener
            }

            if (donorViewModel.isImageNotSelected()) {
                applicationContext.showToast("Choose a image")
                return@setOnClickListener
            }

            if (!hasPermissions(applicationContext, *locationPermissions)) {
                applicationContext.showToast("Please Allow Location To Post")
                ActivityCompat.requestPermissions(
                    this@DonorActivity,
                    locationPermissions,
                    PERMISSION_ALL,
                )
                return@setOnClickListener
            }

            if (!isGPSEnabled(applicationContext)) {
                applicationContext.showToast("Open Your GPS")
                return@setOnClickListener
            }

            circularProgressIndicator.visibility = View.VISIBLE
            if (donorViewModel.isFoodNotValid()) {
                circularProgressIndicator.visibility = View.GONE
                applicationContext.showToast("You Cannot Upload Food")
                return@setOnClickListener
            }

            donorViewModel.getCurrentLocation()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    donorViewModel.filepath = data.data
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data)
                    binding.foodimg.setImageBitmap(bitmap)
                }
            } else if (resultCode == RESULT_CANCELED) {
                applicationContext.showToast("Canceled")
            }
        }
    }
}
