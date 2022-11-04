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
    db: FirebaseFirestore,
    auth: FirebaseAuth,
) :
    ViewModel() {

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var postDetailsViewModel: PostModel
    lateinit var userid: String
    lateinit var name: String
    lateinit var id: String
    lateinit var title: String
    lateinit var imgurl: String
    lateinit var desc: String
    var postlocationlat: Double? = null
    var postlocationlong: Double? = null

    private val arrayList: ArrayList<RequestModel> =
        mutableListOf<RequestModel>() as ArrayList<RequestModel>

    private val _detailsResult = MutableLiveData<DetailsResult>()
    val detailsResult: MutableLiveData<DetailsResult> = _detailsResult

    fun sendFoodRequest() {
        val mypoint = GeoPoint(postDetailsViewModel.location?.latitude!!,
            postDetailsViewModel.location?.longitude!!,
        )
        val requestmap: MutableMap<String, Any?> = HashMap()
        requestmap["name"] = auth.currentUser!!.displayName
        requestmap["uid"] = auth.currentUser!!.uid
        requestmap["status"] = 0
        requestmap["postId"] = postDetailsViewModel.postid
        requestmap["ownerId"] = postDetailsViewModel.uid
        requestmap["for"] = postDetailsViewModel.title
        requestmap["time"] = FieldValue.serverTimestamp()
        requestmap["ownerName"] = postDetailsViewModel.name
        requestmap["location"] = mypoint
        db.collection("requests")
            .add(requestmap)
            .addOnCompleteListener(
                OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _detailsResult.value = DetailsResult.SendFoodRequestSuccess
                    } else {
                        _detailsResult.value = DetailsResult.Error
                    }
                },
            )
    }

    fun getFoodRequest() {
        db.collection("requests")
            .whereEqualTo("uid", auth.currentUser?.uid)
            .whereEqualTo("postId", postDetailsViewModel.postid)
            .get()
            .addOnCompleteListener(
                OnCompleteListener { task ->

                    if (task.isSuccessful) {
                        if (task.result.size() > 0) {
                            var mynum: Int = 0
                            mynum =
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
                },
            )
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
