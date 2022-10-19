package com.example.sharewhatyoucanproject.auth.SelectUser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.sharewhatyoucanproject.databinding.FragmentSelectUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SelectUserFragment : Fragment() {
    private var _binding: FragmentSelectUserBinding? = null
    private val binding get() = _binding!!

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth

    private lateinit var selectUserViewModel: SelectUserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectUserViewModel = ViewModelProvider(this)[SelectUserViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSelectUserBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)?.supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        selectUserViewModel.saveTypeResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                SaveTypeResult.Success -> {
                    val action =
                        SelectUserFragmentDirections.actionSelectUserFragmentToHomeFragment()
                    findNavController().navigate(action)
                }
                is SaveTypeResult.Error -> {
                    Toast.makeText(requireContext(), "Failed to update", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        binding.pantry.setOnClickListener { selectUserViewModel.saveUserType("pantry") }
        binding.individual.setOnClickListener { selectUserViewModel.saveUserType("individual") }

        return binding.root
    }
}
