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
import java.util.*

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

fun randomRoom(): String {
    val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
    val sb = StringBuilder()
    val random = Random()
    val length = 20
    for (i in 0 until length) {
        val index = random.nextInt(alphabet.length)
        val randomChar = alphabet[index]

        sb.append(randomChar)
    }
    return sb.toString()
}

fun getNameFromType(type: UserType): String {
    return when (type) {
        UserType.DONOR -> {
            "Donor"
        }
        UserType.RECEIVER -> {
            "Receiver"
        }
        else -> ""
    }
}

