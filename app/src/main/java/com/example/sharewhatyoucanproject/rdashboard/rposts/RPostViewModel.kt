package com.example.sharewhatyoucanproject.rposts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sharewhatyoucanproject.models.GeoPoint
import com.example.sharewhatyoucanproject.models.PostModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.ArrayList

class RPostViewModel(
    private val db: FirebaseFirestore,
) : ViewModel() {
    var arrayList: ArrayList<PostModel> = mutableListOf<PostModel>() as ArrayList<PostModel>
    private val _taskResult = MutableLiveData<TaskResult>()
    val taskResult: LiveData<TaskResult> = _taskResult

    fun getlist() {
        db.collection("posts")
            .whereEqualTo("status", 0)
            .get()
            .addOnCompleteListener(
                OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (ds in task.getResult()) {
                            val postModel = ds.getGeoPoint("location")?.let {
                                PostModel(
                                    ds.get("imageUrl").toString(),
                                    "" + ds.getString("title"),
                                    "" + ds.getString("description"),
                                    ds.getString("uid").toString(),
                                    ds.getString("name").toString(),
                                    ("" + ds.get("status")).toInt(),
                                    ds.getId(),
                                    GeoPoint(it.latitude, it.longitude),
                                )
                            }
                            if (postModel != null) {
                                arrayList.add(postModel)
                            }
                            _taskResult.value = TaskResult.Success(arrayList)
                        }
                    } else {
                        _taskResult.value = TaskResult.Error
                    }
                },
            )
    }
}

class RPostViewModelFactory(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RPostViewModel(db) as T
    }
}

sealed class TaskResult {
    data class Success(val arrayList: ArrayList<PostModel>) : TaskResult()
    object Error : TaskResult()
}
