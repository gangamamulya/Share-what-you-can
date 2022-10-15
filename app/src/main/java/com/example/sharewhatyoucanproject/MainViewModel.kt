package com.example.sharewhatyoucanproject

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class MainViewModel : ViewModel()
{
    private val currentUser = FirebaseAuth.getInstance().currentUser

    fun getUserName(): String {
        return currentUser?.displayName ?: ""

}
    fun getUserId(): String {
        return currentUser?.uid ?: ""
    }

}

