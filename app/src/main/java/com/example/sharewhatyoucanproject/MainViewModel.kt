package com.example.sharewhatyoucanproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainViewModel : ViewModel() {
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val currentUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    init {
        updateUserName()
    }

    private fun updateUserName() {
        _username.value = currentUser?.displayName
    }

    fun getUserId(): String {
        return currentUser?.uid ?: ""
    }
}
