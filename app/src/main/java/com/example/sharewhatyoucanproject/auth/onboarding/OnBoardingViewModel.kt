package com.example.sharewhatyoucanproject.auth.onboarding

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class OnBoardingViewModel : ViewModel() {
    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}
