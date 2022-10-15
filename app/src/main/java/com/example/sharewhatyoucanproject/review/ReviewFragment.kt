package com.example.sharewhatyoucanproject.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sharewhatyoucanproject.R
import com.example.sharewhatyoucanproject.databinding.FragmentReviewBinding

class ReviewFragment : Fragment() {
    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)

        return binding.root    }
}
