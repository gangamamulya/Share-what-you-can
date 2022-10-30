package com.example.sharewhatyoucanproject

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.UUID
import kotlin.collections.HashMap

class AddDonorViewModel(
    private val db: FirebaseFirestore,
    private val storageReference: StorageReference,
    private var auth: FirebaseAuth = FirebaseAuth.getInstance(),
) : ViewModel() {
    private lateinit var uploadTask: UploadTask

    private lateinit var currentLocation: GeoPoint

    private var imageUrl: String = ""

    private val _donorResult = MutableLiveData<DonorResult>()
    val donorResult: LiveData<DonorResult> = _donorResult

    private var foodType: String = "Cooked Food"
    private var title = ""
    private var description = ""
    private var hourSet = ""
    private var filepath: Uri? = null

    fun setFoodType(foodType: String) {
        this.foodType = foodType
    }

    fun setFilepath(filepath: Uri?) {
        this.filepath = filepath
    }

    fun setFoodData(title: String, description: String, hourSet: String) {
        this.title = title
        this.description = description
        this.hourSet = hourSet
    }

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

    fun setCurrentLocation(geoPoint: GeoPoint) {
        currentLocation = geoPoint
        uploadImage()
    }

    fun saveData() {
        val postMap: MutableMap<String, Any?> = HashMap()
        postMap["imageUrl"] = imageUrl
        postMap["date"] = FieldValue.serverTimestamp()
        postMap["title"] = title
        postMap["description"] = description
        postMap["status"] = 0
        postMap["type"] = foodType
        postMap["location"] = currentLocation
        postMap["uid"] = auth.currentUser!!.uid
        postMap["name"] = auth.currentUser!!.displayName
        db.collection("posts")
            .add(postMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _donorResult.value = DonorResult.Success
                } else {
                    _donorResult.value = DonorResult.Error("Failed ${task.exception}")
                }
            }
    }

    fun uploadImage() {
        storageReference.let {
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
                imageUrl = task.result.toString()
                saveData()
            }
        }
    }
}

class AddDonorViewModelFactory(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddDonorViewModel(db, storageReference) as T
    }
}

sealed class DonorResult {
    object Success : DonorResult()
    data class Error(val message: String) : DonorResult()
}
