package com.example.sharewhatyoucanproject.rdashboard.rdetailedPosts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sharewhatyoucanproject.models.PostModel
import com.google.firebase.firestore.FirebaseFirestore

class RDetailsViewModel(
    private val db: FirebaseFirestore,
) : ViewModel()

class RDetailsViewModelFactory(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RDetailsViewModel(db) as T
    }
}

sealed class DetailsResult {
    data class Success(val arrayList: ArrayList<PostModel>) : DetailsResult()
    object Error : DetailsResult()
}
