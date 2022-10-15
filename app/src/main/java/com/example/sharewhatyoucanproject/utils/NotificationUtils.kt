package com.example.sharewhatyoucanproject.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.sharewhatyoucanproject.MainActivity
import com.google.firebase.messaging.FirebaseMessaging

fun createNotificationChannel(context: Context, userId:String,) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel =
            NotificationChannel(
                MainActivity.DonorConstants.CHANNEL_1_ID,
                MainActivity.DonorConstants.CHANNEL_2_ID,
                NotificationManager.IMPORTANCE_HIGH,
            )
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
    FirebaseMessaging.getInstance().subscribeToTopic(userId)
}

