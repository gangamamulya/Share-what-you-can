package com.example.sharewhatyoucanproject.home

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class HomeViewModel() : ViewModel() {

    private val currentUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    fun isUserNotLogin(): Boolean {
        return currentUser == null
    }

    fun getUserName(): String {
        return currentUser?.displayName ?: ""
    }
}
