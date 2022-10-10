package com.example.sharewhatyoucanproject

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sharewhatyoucanproject.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.nameofuser.text = "" + FirebaseAuth.getInstance().currentUser?.displayName
        binding.uploadlayout.setOnClickListener {
            val i = Intent(activity, DonorActivity::class.java)
            startActivity(i)
        }
        return view
    }
}
