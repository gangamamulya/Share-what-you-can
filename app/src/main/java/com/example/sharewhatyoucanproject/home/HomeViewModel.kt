package com.example.sharewhatyoucanproject.home

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class HomeViewModel(): ViewModel() {

    private val currentUser = FirebaseAuth.getInstance().currentUser

    fun isUserNotLogin(): Boolean {
        return currentUser == null
    }
    fun getUserName(): String {
        return currentUser?.displayName ?: ""
    }

}
