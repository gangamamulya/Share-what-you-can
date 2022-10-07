package com.example.sharewhatyoucanproject.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sharewhatyoucanproject.models.UserType
import com.example.sharewhatyoucanproject.utils.editSharedPreferencesSelector
import com.example.sharewhatyoucanproject.utils.generatePassword
import com.example.sharewhatyoucanproject.utils.getRandomName
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.HashMap
import kotlin.collections.MutableMap
import kotlin.collections.set

class LoginViewModel(
    private val app: Application,
    private var auth: FirebaseAuth,
    private val db: FirebaseFirestore,

    ) : ViewModel() {

    private val _authenticationResult = MutableLiveData<AuthenticationResult>()
    val authenticationResult: LiveData<AuthenticationResult> = _authenticationResult
    var type: UserType = UserType.UNKNOWN
    var deviceId: String = ""
    private var userId: String = ""
    private var userEmail: String = ""


    fun checkUser(deviceId: String) {
        val email = "s$deviceId@gmail.com"
        generatePassword(email)
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
                    _authenticationResult.value =
                        AuthenticationResult.Fail("Failed ${task.exception}")
                }
            }
    }

    private fun signUp(email: String) {
        auth.createUserWithEmailAndPassword(
            email,
            generatePassword(email),
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                userEmail = email
                userId = it.result.user?.uid ?: ""
                _authenticationResult.value = AuthenticationResult.SignUpSuccess
            } else {
                _authenticationResult.value = AuthenticationResult.Fail("Failed ${it.exception}")
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
                    _authenticationResult.value =
                        AuthenticationResult.Fail("Failed ${task.exception}")
                }
            }
    }

    private fun updateUser(userName: String) {
        val user: FirebaseUser? = auth.currentUser
        val profileUpdates: UserProfileChangeRequest =
            UserProfileChangeRequest.Builder()
                .setDisplayName("" + userName)
                .build()
        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    editSharedPreferencesSelector(app, type)
                    _authenticationResult.value = AuthenticationResult.SaveDataSuccess
                } else {
                    _authenticationResult.value =
                        AuthenticationResult.Fail("Failed ${task.exception}")
                }
            }
    }

    private fun loginUser(email: String) {
        auth.signInWithEmailAndPassword(email, generatePassword(email))
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (type == UserType.DONOR || type == UserType.RECEIVER) {
                        editSharedPreferencesSelector(app, type)
                        _authenticationResult.value = AuthenticationResult.LoginSuccess
                    }
                } else {
                    _authenticationResult.value =
                        AuthenticationResult.Fail("Failed ${task.exception}")
                }
            }
    }
}

class LoginViewModelFactory(
    private val app: Application,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
     private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(app, auth, db) as T
    }
}

sealed class AuthenticationResult {
    data class Fail(val message: String) : AuthenticationResult()
    object LoginSuccess : AuthenticationResult()
    object SignUpSuccess : AuthenticationResult()
    object SaveDataSuccess : AuthenticationResult()
}
