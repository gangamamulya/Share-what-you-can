package com.example.sharewhatyoucanproject.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sharewhatyoucanproject.MainActivity
import com.example.sharewhatyoucanproject.R
import com.example.sharewhatyoucanproject.databinding.FragmentSelectUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SelectUserFragment : Fragment() {
    private var _binding: FragmentSelectUserBinding? = null
    private val binding get() = _binding!!

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSelectUserBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar!!.hide()

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.pantry.setOnClickListener { sendtodb("pantry") }
        binding.individual.setOnClickListener { sendtodb("individual") }

        return binding.root
    }

    fun sendtodb(type: String) {
        db.collection("users")
            .document(auth.currentUser!!.uid)
            .update("type", type)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val action =
                        SelectUserFragmentDirections.actionSelectUserFragmentToHomeFragment()
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(requireContext(), "Failed to update", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

}

