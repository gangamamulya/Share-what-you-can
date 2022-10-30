package com.example.sharewhatyoucanproject.deliverfood

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sharewhatyoucanproject.models.GeoPoint
import com.example.sharewhatyoucanproject.models.PostModel
import com.example.sharewhatyoucanproject.models.RequestModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class DeliverViewModel(
    private val db: FirebaseFirestore,
) : ViewModel() {

    private val _deliverResult = MutableLiveData<DeliverResult>()
    val deliverResult: MutableLiveData<DeliverResult> = _deliverResult

    private val _donationResult = MutableLiveData<DonationResult>()
    val donationResult: MutableLiveData<DonationResult> = _donationResult

    fun getItems() {
        db.collection("posts")
            .whereEqualTo("uid", FirebaseAuth.getInstance().currentUser!!.uid)
            .whereNotEqualTo("status", 2)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<PostModel>()
                    for (ds in task.result) {
                        val postModel = ds.getGeoPoint("location")?.let {
                            PostModel(
                                ds.getString("imageUrl")!!,
                                "" + ds.getString("title"),
                                "" + ds.getString("description"),
                                ds.getString("uid")!!,
                                ds.getString("name")!!,
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
        val donationmap: MutableMap<String, Any?> = HashMap()
        donationmap["doner"] = FirebaseAuth.getInstance().currentUser!!.displayName
        donationmap["donatedTo"] = requestModel.uid + ""
        donationmap["receiverName"] = requestModel.name + ""
        donationmap["time"] = FieldValue.serverTimestamp()
        donationmap["title"] = postModel.title
        donationmap["img"] = postModel.image
        donationmap["desc"] = postModel.desc
        donationmap["uid"] = FirebaseAuth.getInstance().currentUser!!.uid

        val sref = db.collection("donations").document(randomRoom())
        val uref =
            db.collection("posts").document(postModel.postid)

        val uref2 = db.collection("requests").document(requestModel.updateid)
        db.runBatch { batch ->
            batch.set(sref, donationmap)
            batch.update(uref, "status", 2)
            batch.update(uref2, "status", 2)
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _donationResult.value = DonationResult.Success
            } else {
                _donationResult.value = DonationResult.Error
            }
        }
    }
}

private fun randomRoom(): String {
    val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
    val sb = StringBuilder()
    val random = Random()
    val length = 20
    for (i in 0 until length) {
        val index = random.nextInt(alphabet.length)
        val randomChar = alphabet[index]

        sb.append(randomChar)
    }
    return sb.toString()
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
