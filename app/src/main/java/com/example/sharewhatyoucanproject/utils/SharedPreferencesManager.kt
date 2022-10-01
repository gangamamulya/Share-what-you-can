package com.example.sharewhatyoucanproject.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.sharewhatyoucanproject.models.UserType

fun editSharedPreferencesSelector(context: Context, type: UserType) {
    val sharedPreferences =
        context.getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
    val editType: SharedPreferences.Editor = sharedPreferences.edit()
    editType.putInt("selector", type.ordinal)
    editType.commit()
}
