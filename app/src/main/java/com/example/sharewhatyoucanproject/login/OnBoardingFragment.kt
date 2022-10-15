package com.example.sharewhatyoucanproject.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sharewhatyoucanproject.databinding.FragmentOnBoardingBinding
import com.example.sharewhatyoucanproject.models.UserType
import com.google.firebase.auth.FirebaseAuth

class OnBoardingFragment : Fragment() {

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        FirebaseAuth.getInstance().signOut()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        binding.donerimg.setOnClickListener {
            val action =
                OnBoardingFragmentDirections.actionOnBoardingFragmentToLoginFragment(UserType.DONOR.ordinal)
            findNavController().navigate(action)
        }

        binding.receiverimg.setOnClickListener {
            val action =
                OnBoardingFragmentDirections.actionOnBoardingFragmentToDashboardFragment(UserType.RECEIVER.ordinal)
            findNavController().navigate(action)
        }

        return binding.root
    }

}
