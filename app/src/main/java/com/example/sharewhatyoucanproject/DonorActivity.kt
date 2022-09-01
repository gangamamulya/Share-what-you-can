package com.example.sharewhatyoucanproject

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.*
import kotlin.collections.HashMap

class DonorActivity : AppCompatActivity() {
    lateinit var foodimg: ImageView
    var filepath: Uri? = null
    lateinit var uploadbtn: Button
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var uploadTask: UploadTask
    lateinit var titleet: TextView
    lateinit var descet: TextView
    private lateinit var hourset: EditText
    lateinit var pd: ProgressDialog
    lateinit var locationRequest: com.google.android.gms.location.LocationRequest
    var longitute: Double = 0.0
    var latitude: Double = 0.0
    private lateinit var spin: Spinner
    var typestr: String = "Cooked Food"
    var types = arrayOf(
        "Cooked Food",
        "Ingredients",
        "Packaged food",
        "Groceries"
    )

    lateinit var arrayAdapter: ArrayAdapter<String>
    val permission_all = 1
    val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        supportActionBar?.hide()
        pd = ProgressDialog(this)
        pd.setTitle("Please Wait")
        pd.setCancelable(false)
        locationRequest = com.google.android.gms.location.LocationRequest.create()
        supportActionBar?.hide()
        hourset = findViewById(R.id.hourset)
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        spin = findViewById(R.id.spinner)
        uploadbtn = findViewById(R.id.uploadbtn)
        titleet = findViewById(R.id.titleet)
        descet = findViewById(R.id.descet)
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        foodimg = findViewById(R.id.foodimg)
        foodimg.setOnClickListener(
            View.OnClickListener {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0)
            }
        )
        arrayAdapter = ArrayAdapter<String>(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            types
        )
        spin.adapter = arrayAdapter
        spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                typestr = types[position]
                Toast.makeText(applicationContext, "" + typestr, Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        uploadbtn.setOnClickListener(
            View.OnClickListener {
            if (titleet.text.toString().isEmpty() || descet.text.toString().isEmpty() || hourset.text.toString().isEmpty()
            ) {
                Toast.makeText(applicationContext, "Please Fill All Fields", Toast.LENGTH_SHORT)
                    .show()
            } else {
                if (!hasPermissions(
                        applicationContext,
                        *permissions
                    )
                ) {
                    Toast.makeText(
                        applicationContext,
                        "Please Allow Location To Post",
                        Toast.LENGTH_SHORT
                    ).show()
                    ActivityCompat.requestPermissions(
                        this@DonorActivity,
                        permissions,
                        permission_all
                    )
                } else {
                    if (isGPSEnabled(applicationContext)) {
                        if (filepath != null) {
                            pd.show()
                            if (typestr.equals("Cooked Food") && Integer.parseInt(hourset.text.toString()) > 48) {
                                pd.dismiss()
                                Toast.makeText(
                                    applicationContext,
                                    "You Cannot Upload Food",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (typestr.equals("Groceries") && Integer.parseInt(hourset.text.toString()) > 1460) {
                                Toast.makeText(
                                    applicationContext,
                                    "You Cannot Upload Food",
                                    Toast.LENGTH_SHORT
                                ).show()
                                pd.dismiss()
                            } else {
                                uploadImage()
                            }
                        } else {
                            Toast.makeText(applicationContext, "Choose a image", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(applicationContext, "Open Your GPS", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
        )
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    filepath = data.data
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data)
                    foodimg.setImageBitmap(bitmap)
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(applicationContext, "Canceled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImage() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(this@DonorActivity, permissions, permission_all)
            return
        }

        LocationServices.getFusedLocationProviderClient(applicationContext)
            .requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationresult: LocationResult) {
                    super.onLocationResult(locationresult)
                    if (locationresult.locations != null) {
                        if (locationresult.locations.size > 0) {
                            val index: Int = locationresult.locations.size - 1
                            latitude = locationresult.locations.get(index).latitude
                            longitute = locationresult.locations.get(index).longitude
                            val point = GeoPoint(latitude, longitute)
                            val fpvar = filepath
                            if (fpvar != null) {
                                val ref = storageReference.child("images/" + UUID.randomUUID())
                                uploadTask = ref.putFile(fpvar)
                                uploadTask.continueWithTask { task ->
                                    if (!task.isSuccessful) {
                                        pd.show()
                                        val excep = task.exception
                                        if (excep != null) {
                                            throw excep
                                        }
                                    }
                                    ref.downloadUrl
                                }.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val downloadUri = task.result
                                        val postmap: MutableMap<String, Any?> = HashMap()
                                        postmap["imageUrl"] = downloadUri.toString()
                                        postmap["date"] = FieldValue.serverTimestamp()
                                        postmap["title"] = titleet.text.toString()
                                        postmap["description"] = descet.text.toString()
                                        postmap["status"] = 0
                                        postmap["type"] = typestr
                                        postmap["location"] = point
                                        db.collection("posts")
                                            .add(postmap)
                                            .addOnCompleteListener { task ->
                                                pd.show()
                                                if (task.isSuccessful) {
                                                    Toast.makeText(
                                                        this@DonorActivity,
                                                        "Post Added",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    finish()
                                                } else {
                                                    pd.dismiss()
                                                    Toast.makeText(
                                                        this@DonorActivity,
                                                        "Failed " + task.exception,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                    } else {
                                        pd.dismiss()
                                        Toast.makeText(
                                            this@DonorActivity,
                                            "Failed " + task.exception,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Please Select a Image First",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Some Technical Error. Please Retry",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }, Looper.getMainLooper())
    }

    private fun isGPSEnabled(context: Context): Boolean {
        var locationManager: LocationManager? = null
        var isenable = false
        if (locationManager == null) {
            locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
        }
        isenable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return isenable
    }

    fun hasPermissions(context: Context?, vararg permissions: String?): Boolean {
        if (context != null) {
            for (permission in permissions) {
                if (permission?.let {
                        ActivityCompat.checkSelfPermission(
                            context,
                            it
                        )
                    } != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }
}
