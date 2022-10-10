package com.example.sharewhatyoucanproject.donor

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class DonorDrawerViewModel : ViewModel()
{
    private val currentUser = FirebaseAuth.getInstance().currentUser

    fun getUserName(): String {
        return currentUser?.displayName ?: ""

}
    fun getUserId(): String {
        return currentUser?.uid ?: ""
    }

}

