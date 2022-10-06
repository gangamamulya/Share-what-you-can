package com.example.sharewhatyoucanproject

import android.content.Context
import android.net.Uri
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sharewhatyoucanproject.utils.showToast
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.*

class DonorViewModel(
    val context: Context?,
    var db: FirebaseFirestore?,
    var storageReference: StorageReference?,
) : ViewModel() {
    private lateinit var uploadTask: UploadTask
    private var locationRequest: LocationRequest = LocationRequest.create()

    private val _currentLocation = MutableLiveData<GeoPoint>()
    val currentLocation: LiveData<GeoPoint> get() = _currentLocation

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> get() = _imageUrl

    private val _saveData = MutableLiveData<String>()
    val saveData: LiveData<String> get() = _saveData

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
                    _saveData.value = "success"
                } else {
                    _saveData.value = "Failed ${task.exception}"
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

    fun getCurrentLocation() {
        LocationServices.getFusedLocationProviderClient(context!!)
            .requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationresult: LocationResult) {
                        super.onLocationResult(locationresult)
                        if (locationresult.locations.size > 0) {
                            val index: Int = locationresult.locations.size - 1
                            val latitude = locationresult.locations[index].latitude
                            val longitude = locationresult.locations[index].longitude
                            _currentLocation.value = GeoPoint(latitude, longitude)
                        } else {
                            context.showToast("Some Technical Error. Please Retry")
                        }
                    }
                },
                Looper.getMainLooper(),
            )
    }
}

class DonorViewModelFactory(
    private val mContext: Context,
    private val db: FirebaseFirestore,
    private val storageReference: StorageReference,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DonorViewModel(mContext, db, storageReference) as T
    }
}
