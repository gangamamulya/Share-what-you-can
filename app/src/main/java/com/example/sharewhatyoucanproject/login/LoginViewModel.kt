package com.example.sharewhatyoucanproject.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sharewhatyoucanproject.models.UserType
import com.example.sharewhatyoucanproject.utils.DEFAULT_PASSWORD
import com.example.sharewhatyoucanproject.utils.getRandomName
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),

) : ViewModel() {

    val checkResult = MutableLiveData<String>()
    val loginResult = MutableLiveData<String>()
    val createUserResult = MutableLiveData<String>()
    val saveUserResult = MutableLiveData<String>()

    var type: UserType = UserType.UNKNOWN
    var deviceId: String = ""
    private var userId: String = ""
    private var userEmail: String = ""

    fun checkUser(email: String) {
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val size = task.result.size()
                    if (size == 0) {
                        signUp(email)
                    } else {
                        loginUser(email)
                    }
                } else {
                    checkResult.value = "Failed ${task.exception}"
                }
            }
    }

    private fun signUp(email: String) {
        auth.createUserWithEmailAndPassword(
            email,
            DEFAULT_PASSWORD,
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                userEmail = email
                userId = it.result.user?.uid ?: ""
                createUserResult.value = "success"
            } else {
                createUserResult.value = "Failed ${it.exception}"
            }
        }
    }

    fun saveUserData() {
        val userMap: MutableMap<String, String> = HashMap()
        val userName = getRandomName()
        userMap["name"] = userName
        userMap["email"] = userEmail
        userMap["uuid"] = userId
        userMap["deviceId"] = deviceId
        db.collection("users")
            .document(userId)
            .set(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUser(userName)
                } else {
                    saveUserResult.value = "Failed ${task.exception}"
                }
            }
    }

    private fun updateUser(userName: String) {
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val profileUpdates: UserProfileChangeRequest =
            UserProfileChangeRequest.Builder()
                .setDisplayName("" + userName)
                .build()
        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveUserResult.value = "success"
                } else {
                    saveUserResult.value = "Failed ${task.exception}"
                }
            }
    }

    private fun loginUser(email: String) {
        auth.signInWithEmailAndPassword(email, DEFAULT_PASSWORD).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (type == UserType.DONER || type == UserType.RECEIVER) {
                    loginResult.value = "success"
                }
            } else {
                loginResult.value = "Failed ${task.exception}"
            }
        }
    }
}

class LoginViewModelFactory(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(auth, db) as T
    }
}
