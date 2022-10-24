package com.example.sharewhatyoucanproject.rdashboard.rdetailedPosts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sharewhatyoucanproject.models.PostModel
import com.google.firebase.firestore.FirebaseFirestore

class PostDetailsViewModel(
    private val db: FirebaseFirestore,
) : ViewModel()

class PostDetailsViewModelFactory(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PostDetailsViewModel(db) as T
    }
}

sealed class DetailsResult {
    data class Success(val arrayList: ArrayList<PostModel>) : DetailsResult()
    object Error : DetailsResult()
}
