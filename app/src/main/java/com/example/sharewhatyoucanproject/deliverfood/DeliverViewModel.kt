package com.example.sharewhatyoucanproject.deliverfood

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sharewhatyoucanproject.models.GeoPoint
import com.example.sharewhatyoucanproject.models.PostModel
import com.example.sharewhatyoucanproject.models.RequestModel
import com.example.sharewhatyoucanproject.utils.randomRoom
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class DeliverViewModel(
    private val db: FirebaseFirestore?,
) : ViewModel() {

    private val _deliverResult = MutableLiveData<DeliverResult>()
    val deliverResult: MutableLiveData<DeliverResult> = _deliverResult

    private val _donationResult = MutableLiveData<DonationResult>()
    val donationResult: MutableLiveData<DonationResult> = _donationResult


    fun getItems() {
        db?.collection("posts")
            ?.whereEqualTo("uid", FirebaseAuth.getInstance().currentUser!!.uid)
            ?.whereNotEqualTo("status", 2)
            ?.get()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<PostModel>()
                    for (ds in task.result) {
                        val postModel = ds.getGeoPoint("location")?.let {
                            PostModel(
                                ds.getString("imageUrl").toString(),
                                "" + ds.getString("title"),
                                "" + ds.getString("description"),
                                ds.getString("uid").toString(),
                                ds.getString("name").toString(),
                                ("" + ds["status"]).toInt(),
                                ds.id,
                                GeoPoint(it.latitude, it.longitude),
                            )
                        }

                        if (postModel != null) {
                            list.add(postModel)
                        }
                    }
                    _deliverResult.value = DeliverResult.DeliveredSuccess(list)
                } else {
                    _deliverResult.value = DeliverResult.Error
                }
            }
    }

    fun donate(postModel: PostModel, requestModel: RequestModel) {
        val donationMap: MutableMap<String, Any?> = getDonationMap(
            postModel, requestModel,
            FirebaseAuth.getInstance().currentUser!!.displayName,
            FirebaseAuth.getInstance().currentUser!!.uid,
        )
        db?.let {
            val sref = db.collection("donations").document(randomRoom())
            val uref =
                db.collection("posts").document(postModel.postid)

            val uref2 = db.collection("requests").document(requestModel.updateid)
            db.runBatch { batch ->
                batch.set(sref, donationMap)
                batch.update(uref, "status", FoodDonationCompleted)
                batch.update(uref2, "status", FoodDonationCompleted)
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _donationResult.value = DonationResult.Success
                } else {
                    _donationResult.value = DonationResult.Error
                }
            }
        }
    }

    fun getDonationMap(
        postModel: PostModel,
        requestModel: RequestModel,
        displayName: String?,
        uid: String?,
    ): MutableMap<String, Any?> {
        val donationMap: MutableMap<String, Any?> = HashMap()

        donationMap["donor"] = displayName
        donationMap["donatedTo"] = requestModel.uid + ""
        donationMap["receiverName"] = requestModel.name + ""
        donationMap["time"] = FieldValue.serverTimestamp()
        donationMap["title"] = postModel.title
        donationMap["img"] = postModel.image
        donationMap["desc"] = postModel.desc
        donationMap["uid"] = uid
        return donationMap
    }

    companion object {
        const val FoodDonationCompleted = 2
    }
}


class DeliverViewModelFactory(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DeliverViewModel(db) as T
    }
}

sealed class DeliverResult {
    data class DeliveredSuccess(val List: List<PostModel>) : DeliverResult()
    object Error : DeliverResult()
}

sealed class DonationResult {
    object Success : DonationResult()
    object Error : DonationResult()
}
