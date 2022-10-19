package com.example.sharewhatyoucanproject.auth.SelectUser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SelectUserViewModel : ViewModel() {
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _saveTypeResult = MutableLiveData<SaveTypeResult>()
    val saveTypeResult: LiveData<SaveTypeResult> = _saveTypeResult

    fun saveUserType(type: String) {
        db.collection("users")
            .document(auth.currentUser!!.uid)
            .update("type", type)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _saveTypeResult.value = SaveTypeResult.Success
                } else {
                    _saveTypeResult.value = SaveTypeResult.Error
                }
            }
    }
}

sealed class SaveTypeResult {
    object Error : SaveTypeResult()
    object Success : SaveTypeResult()
}
