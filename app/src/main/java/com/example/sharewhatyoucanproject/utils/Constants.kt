package com.example.sharewhatyoucanproject.utils

import android.Manifest

const val PERMISSION_ALL = 1

const val NOTIFICATION_CHANNEL_ID = "requests_id"
const val NOTIFICATION_CHANNEL_NAME = "FoodRequests"

val locationPermissions = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
)

val foodTypes = arrayOf(
    "Cooked Food",
    "Ingredients",
    "Packaged food",
    "Groceries",
)

val nameArray = arrayOf(
    "Tom",
    "Harry",
    "Tony",
    "Noah",
    "Emma",
    "Oliver",
    "James",
    "William",
    "Henry",
    "Benjamin",
)
