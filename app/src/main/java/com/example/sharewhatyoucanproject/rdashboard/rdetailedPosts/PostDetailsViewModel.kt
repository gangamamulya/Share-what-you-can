package com.example.sharewhatyoucanproject.rdashboard.rdetailedPosts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sharewhatyoucanproject.models.PostModel
import com.example.sharewhatyoucanproject.models.RequestModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class PostDetailsViewModel(
    private val db: FirebaseFirestore?,
    private val auth: FirebaseAuth?,
) :
    ViewModel() {

    lateinit var postDetailsViewModel: PostModel

    private val _detailsResult = MutableLiveData<DetailsResult>()
    val detailsResult: MutableLiveData<DetailsResult> = _detailsResult

    fun sendFoodRequest() {
        db?.collection("requests")
            ?.add(getRequestMap())
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _detailsResult.value = DetailsResult.SendFoodRequestSuccess
                } else {
                    _detailsResult.value = DetailsResult.Error
                }
            }
    }

    fun getRequestMap(): MutableMap<String, Any?> {
        val myPoint = GeoPoint(
            postDetailsViewModel.location?.latitude!!,
            postDetailsViewModel.location?.longitude!!,
        )
        val requestMap: MutableMap<String, Any?> = HashMap()
        requestMap["name"] = auth?.currentUser?.displayName ?: ""
        requestMap["uid"] = auth?.currentUser?.uid ?: ""
        requestMap["status"] = 0
        requestMap["postId"] = postDetailsViewModel.postid
        requestMap["ownerId"] = postDetailsViewModel.uid
        requestMap["for"] = postDetailsViewModel.title
        requestMap["time"] = FieldValue.serverTimestamp()
        requestMap["ownerName"] = postDetailsViewModel.name
        requestMap["location"] = myPoint
        return requestMap
    }

    fun getFoodRequest() {
        db?.collection("requests")
            ?.whereEqualTo("uid", auth?.currentUser?.uid)
            ?.whereEqualTo("postId", postDetailsViewModel.postid)
            ?.get()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.size() > 0) {
                        var mynum: Int =
                            Integer.parseInt(task.result.documents[0].get("status").toString())

                        if (mynum == 0) {
                            _detailsResult.value =
                                DetailsResult.GetFoodRequestSuccessAlreadySent
                        } else if (mynum == 1) {
                            _detailsResult.value = DetailsResult.GetFoodRequestSuccessAccepted
                        }
                    }
                } else {
                    _detailsResult.value = DetailsResult.Error
                }
            }
    }
}

class PostDetailsViewModelFactory(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private var auth: FirebaseAuth = FirebaseAuth.getInstance(),
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PostDetailsViewModel(db, auth) as T
    }
}

sealed class DetailsResult {
    object SendFoodRequestSuccess : DetailsResult()
    object GetFoodRequestSuccessAlreadySent : DetailsResult()
    object GetFoodRequestSuccessAccepted : DetailsResult()
    object Error : DetailsResult()
}
