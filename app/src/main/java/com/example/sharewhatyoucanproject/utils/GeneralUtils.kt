package com.example.sharewhatyoucanproject.utils

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

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
