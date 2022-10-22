package com.example.sharewhatyoucanproject.rdashboard.rdetailedPosts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.sharewhatyoucanproject.databinding.FragmentRDetailsBinding
import com.example.sharewhatyoucanproject.models.PostModel
import com.example.sharewhatyoucanproject.rposts.RPostViewModel
import com.example.sharewhatyoucanproject.rposts.RPostViewModelFactory
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class RDetailsFragment : Fragment() {

    private var _binding: FragmentRDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var rDetailsViewModel: RDetailsViewModel

    private val args: RDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rDetailsViewModel = ViewModelProvider(
            this,
            RDetailsViewModelFactory(),
        )[RDetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRDetailsBinding.inflate(inflater, container, false)
        val post = Json.decodeFromString<PostModel>(args.postModel)

        binding.rtitle.text = post.title
        binding.rdesc.text = post.desc

        Glide.with(requireContext())
            .load(post.image)
            .into(binding.rdetailimg)

        return binding.root
    }
}
