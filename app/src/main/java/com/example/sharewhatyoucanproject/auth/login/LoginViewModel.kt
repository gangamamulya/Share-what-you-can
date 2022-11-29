package com.example.sharewhatyoucanproject.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sharewhatyoucanproject.models.UserType
import com.example.sharewhatyoucanproject.utils.nameArray
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.HashMap
import kotlin.collections.MutableMap
import kotlin.collections.set

class LoginViewModel(

    private val db: FirebaseFirestore?,
    private val auth: FirebaseAuth?,
) : ViewModel() {
    private val _authenticationResult = MutableLiveData<AuthenticationResult>()
    val authenticationResult: LiveData<AuthenticationResult> = _authenticationResult

    private val _selectorResult = MutableLiveData<UserType>()
    val selectorResult: LiveData<UserType> = _selectorResult

    var type: UserType = UserType.UNKNOWN
    var deviceId: String = ""
    var userId: String = ""
    var userEmail: String = ""

    fun checkUser(deviceId: String) {
        val email = generateEmail(deviceId)
        generatePassword(email)
        db?.collection("users")
            ?.whereEqualTo("email", email)
            ?.get()
            ?.addOnCompleteListener { task ->
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
        auth?.createUserWithEmailAndPassword(
            email,
            generatePassword(email),
        )?.addOnCompleteListener {
            if (it.isSuccessful) {
                userEmail = email
                userId = it.result.user?.uid ?: ""
                _authenticationResult.value = AuthenticationResult.SignUpSuccess
                saveUserData()
            } else {
                _authenticationResult.value = AuthenticationResult.Fail("Failed ${it.exception}")
            }
        }
    }

    fun saveUserData() {
        val userName = getRandomName()
        db?.collection("users")
            ?.document(userId)
            ?.set(getUserMap(userName))
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUser(userName)
                } else {
                    _authenticationResult.value =
                        AuthenticationResult.Fail("Failed ${task.exception}")
                }
            }
    }

    fun getUserMap(userName: String): MutableMap<String, String> {
        val userMap: MutableMap<String, String> = HashMap()
        userMap["name"] = userName
        userMap["email"] = userEmail
        userMap["uuid"] = userId
        userMap["deviceId"] = deviceId
        return userMap
    }

    private fun updateUser(userName: String) {
        val user: FirebaseUser? = auth?.currentUser
        val profileUpdates: UserProfileChangeRequest =
            UserProfileChangeRequest.Builder()
                .setDisplayName("" + userName)
                .build()
        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _selectorResult.value = type
                    _authenticationResult.value = AuthenticationResult.SaveDataSuccess
                } else {
                    _authenticationResult.value =
                        AuthenticationResult.Fail("Failed ${task.exception}")
                }
            }
    }

    private fun loginUser(email: String) {
        auth?.signInWithEmailAndPassword(email, generatePassword(email))
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (type == UserType.DONOR || type == UserType.RECEIVER) {
                        _selectorResult.value = type
                        _authenticationResult.value = AuthenticationResult.LoginSuccess
                    }
                } else {
                    _authenticationResult.value =
                        AuthenticationResult.Fail("Failed ${task.exception}")
                }
            }
    }

    fun generatePassword(email: String): String {
        return "Test@1${email.substring(2, 9)}"
    }

    fun generateEmail(deviceId: String): String {
        return "s$deviceId@gmail.com"
    }

    fun getRandomName(): String {
        val random = (1111 until 9999).random()
        val nameIndex = (0..9).random()
        return nameArray[nameIndex] + random
    }
}

class LoginViewModelFactory(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private var auth: FirebaseAuth = FirebaseAuth.getInstance(),
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(db, auth) as T
    }
}

sealed class AuthenticationResult {
    data class Fail(val message: String) : AuthenticationResult()
    object LoginSuccess : AuthenticationResult()
    object SignUpSuccess : AuthenticationResult()
    object SaveDataSuccess : AuthenticationResult()
}
