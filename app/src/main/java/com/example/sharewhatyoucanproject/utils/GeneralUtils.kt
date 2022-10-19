package com.example.sharewhatyoucanproject.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.sharewhatyoucanproject.auth.login.LoginFragmentArgs
import com.example.sharewhatyoucanproject.models.UserType
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.GeoPoint

fun Context.showToast(message: String) {
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_SHORT,
    ).show()
}

fun isGPSEnabled(context: Context): Boolean {
    val locationManager: LocationManager =
        context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun hasPermissions(context: Context?, vararg permissions: String?): Boolean {
    if (context != null) {
        for (permission in permissions) {
            if (permission?.let {
                ActivityCompat.checkSelfPermission(context, it)
            } != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
    }
    return true
}

fun getRandomName(): String {
    val random = (1111 until 9999).random()
    val nameIndex = (0..9).random()
    return nameArray[nameIndex] + random
}

fun getUserType(args: LoginFragmentArgs): UserType {
    return UserType.values()[args.type]
}

@RequiresPermission(anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
fun getCurrentLocation(activity: Activity, onGetLocation: (GeoPoint) -> Unit) {
    LocationServices.getFusedLocationProviderClient(activity).lastLocation
        .addOnSuccessListener { location: Location? ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                onGetLocation(GeoPoint(latitude, longitude))
            }
        }
}

fun generatePassword(email: String): String {
    return "Test@1${email.substring(2, 9)}"
}
