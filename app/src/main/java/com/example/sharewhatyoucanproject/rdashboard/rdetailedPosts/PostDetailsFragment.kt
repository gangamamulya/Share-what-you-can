package com.example.sharewhatyoucanproject.rdashboard.rdetailedPosts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.sharewhatyoucanproject.databinding.FragmentPostDetailsBinding
import com.example.sharewhatyoucanproject.models.PostModel

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class PostDetailsFragment : Fragment() {

    private var _binding: FragmentPostDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var postDetailsViewModel: PostDetailsViewModel

    private val args: PostDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postDetailsViewModel = ViewModelProvider(
            this,
            PostDetailsViewModelFactory(),
        )[PostDetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPostDetailsBinding.inflate(inflater, container, false)
        val post = Json.decodeFromString<PostModel>(args.postModel)

        binding.rtitle.text = post.title
        binding.rdesc.text = post.desc
        binding.rdetailimg.load(post.image)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
