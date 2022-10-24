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

class PostListViewModel(
    private val db: FirebaseFirestore,
) : ViewModel() {
    private val arrayList: ArrayList<PostModel> = mutableListOf<PostModel>() as ArrayList<PostModel>
    private val _taskResult = MutableLiveData<TaskResult>()
    val taskResult: LiveData<TaskResult> = _taskResult

    fun getlist() {
        db.collection("posts")
            .whereEqualTo("status", 0)
            .get()
            .addOnCompleteListener(
                OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (postResult in task.result) {
                            val postModel = postResult.getGeoPoint("location")?.let {
                                PostModel(
                                    postResult.get("imageUrl").toString(),
                                    "" + postResult.getString("title").toString(),
                                    "" + postResult.getString("description").toString(),
                                    postResult.getString("uid").toString(),
                                    postResult.getString("name").toString(),
                                    ("" + postResult.get("status")).toInt(),
                                    postResult.id,
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

class PostListViewModelFactory(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PostListViewModel(db) as T
    }
}

sealed class TaskResult {
    data class Success(val arrayList: ArrayList<PostModel>) : TaskResult()
    object Error : TaskResult()
}
