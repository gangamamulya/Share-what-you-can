package com.example.sharewhatyoucanproject.auth.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.sharewhatyoucanproject.databinding.FragmentOnBoardingBinding
import com.example.sharewhatyoucanproject.models.UserType

class OnBoardingFragment : Fragment() {

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    private lateinit var onBoardingViewModel: OnBoardingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBoardingViewModel = ViewModelProvider(this)[OnBoardingViewModel::class.java]
        onBoardingViewModel.logout()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity?)?.supportActionBar?.hide()

        binding.donerimg.setOnClickListener {
            val action =
                OnBoardingFragmentDirections.actionOnBoardingFragmentToLoginFragment(UserType.DONOR.ordinal)
            findNavController().navigate(action)
        }

        binding.receiverimg.setOnClickListener {
            val action =
                OnBoardingFragmentDirections.actionOnBoardingFragmentToLoginFragment(UserType.RECEIVER.ordinal)
            findNavController().navigate(action)
        }

        return binding.root
    }
}
