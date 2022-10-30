package com.example.sharewhatyoucanproject.foodrequest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sharewhatyoucanproject.models.RequestModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class RequestViewModel(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : ViewModel() {

    lateinit var user: String
    private val arrayList: ArrayList<RequestModel> =
        mutableListOf<RequestModel>() as ArrayList<RequestModel>

    private val _requestResult = MutableLiveData<RequestResult>()
    val requestResult: MutableLiveData<RequestResult> = _requestResult

    private val _approveFoodResult = MutableLiveData<ApproveResult?>()
    val approveFoodResult: LiveData<ApproveResult?> = _approveFoodResult

    fun getrequestbyuser() {
        arrayList.clear()
        db.collection("requests")
            .whereEqualTo("ownerId", auth.currentUser!!.uid)
            .whereEqualTo("uid", user)
            .get()
            .addOnCompleteListener { task1 ->
                if (task1.isSuccessful) {
                    for (ds in task1.result) {
                        val model = RequestModel(
                            ds.getString("name").toString(),
                            ds.getString("uid").toString(),
                            ds.id,
                            Integer.parseInt(ds.get("status").toString()),
                            ds.getString("postId").toString(),
                        )
                        arrayList.add(model)
                    }
                    _requestResult.value = RequestResult.GetRequestListSuccess(arrayList)
                } else {
                    _requestResult.value = RequestResult.Error
                }
            }
    }

    fun resetApproveFood() {
        _approveFoodResult.value = null
    }

    fun getrequests() {
        arrayList.clear()
        db.collection("requests")
            .whereEqualTo("ownerId", auth.currentUser!!.uid)
            .orderBy("time", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener(
                OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (ds in task.result) {
                            val model = RequestModel(
                                ds.getString("name").toString(),
                                ds.getString("uid").toString(),
                                ds.id,
                                Integer.parseInt(ds.get("status").toString()),
                                ds.getString("postId").toString(),
                            )
                            arrayList.add(model)
                        }
                        _requestResult.value = RequestResult.GetRequestInOrderSuccess(arrayList)
                    } else {
                        _requestResult.value = RequestResult.Error
                    }
                },
            )
    }

    fun approveFood(type: String, requestModel: RequestModel) {
        val requestref =
            db.collection("requests").document(requestModel.updateid)
        val postref = db.collection("posts").document(requestModel.postid)

        if (type == "Approve") {
            _approveFoodResult.value = ApproveResult.ChangeButton(requestModel.uid)

            db.runBatch { batch ->
                batch.update(requestref, "status", 1)
                batch.update(postref, "status", 1)
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _approveFoodResult.value = ApproveResult.ChangeButton(requestModel.uid)
                }
            }
        } else if (type == "Deliver") {
            _approveFoodResult.value = ApproveResult.NavigateToDeliverScreen(requestModel)
        }
    }
}

class RequestViewModelFactory(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private var auth: FirebaseAuth = FirebaseAuth.getInstance(),
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RequestViewModel(db, auth) as T
    }
}

sealed class RequestResult {
    data class GetRequestListSuccess(val arrayList: ArrayList<RequestModel>) : RequestResult()
    data class GetRequestInOrderSuccess(val arrayList: ArrayList<RequestModel>) : RequestResult()
    object Error : RequestResult()
}

sealed class ApproveResult {
    data class NavigateToDeliverScreen(val requestModel: RequestModel) : ApproveResult()
    data class ChangeButton(val uid: String) : ApproveResult()
}
