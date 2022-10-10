package com.example.sharewhatyoucanproject

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.UUID
import kotlin.collections.HashMap

class DonorViewModel(
    private val db: FirebaseFirestore,
    private val storageReference: StorageReference,

    ) : ViewModel() {
    private lateinit var uploadTask: UploadTask
    private var locationRequest: LocationRequest = LocationRequest.create()

    private val _currentLocation = MutableLiveData<GeoPoint>()
    val currentLocation: LiveData<GeoPoint> get() = _currentLocation

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> get() = _imageUrl

    private val _donorResult = MutableLiveData<DonorResult>()
    val donorResult: LiveData<DonorResult> = _donorResult

    var foodType: String = "Cooked Food"
    var title = ""
    var description = ""
    var hourSet = ""
    var filepath: Uri? = null

    fun isDataCompleted(): Boolean {
        return title.isNotEmpty() && description.isNotEmpty() && hourSet.isNotEmpty()
    }

    fun isImageNotSelected(): Boolean {
        return filepath == null
    }

    fun isFoodNotValid(): Boolean {
        val hours = Integer.parseInt(hourSet)
        return foodType == "Cooked Food" && hours > 48 || foodType == "Groceries" && hours > 1460
    }

    fun saveData() {
        val postMap: MutableMap<String, Any?> = HashMap()
        postMap["imageUrl"] = imageUrl
        postMap["date"] = FieldValue.serverTimestamp()
        postMap["title"] = title
        postMap["description"] = description
        postMap["status"] = 0
        postMap["type"] = foodType
        postMap["location"] = currentLocation.value
        db?.collection("posts")
            ?.add(postMap)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _donorResult.value = DonorResult.Success
                } else {
                    _donorResult.value = DonorResult.Error("Failed ${task.exception}")
                }
            }
    }

    fun uploadImage() {
        storageReference?.let {
            val ref = it.child("images/" + UUID.randomUUID())
            uploadTask = ref.putFile(filepath!!)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    val excep = task.exception
                    if (excep != null) {
                        throw excep
                    }
                }
                ref.downloadUrl
            }.addOnCompleteListener { task ->
                _imageUrl.value = task.result.toString()
            }
        }
    }

    fun setCurrentLocation(geoPoint: GeoPoint) {
        _currentLocation.value = geoPoint
    }

}

class DonorViewModelFactory(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference,
) : ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DonorViewModel(db, storageReference) as T
    }
}

sealed class DonorResult {
    object Success : DonorResult()
    data class Error(val message: String) : DonorResult()
}
