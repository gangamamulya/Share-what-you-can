package com.example.sharewhatyoucanproject.rdashboard.rdetailedPosts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.sharewhatyoucanproject.R
import com.example.sharewhatyoucanproject.databinding.FragmentPostDetailsBinding
import com.example.sharewhatyoucanproject.models.PostModel
import com.example.sharewhatyoucanproject.models.RequestModel
import com.example.sharewhatyoucanproject.utils.showToast
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class PostDetailsFragment : Fragment() {

    private var _binding: FragmentPostDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var postDetailsViewModel: PostDetailsViewModel
    lateinit var circularProgressIndicator: CircularProgressIndicator
    private lateinit var requestModel: ArrayList<RequestModel>
    lateinit var name: String
    lateinit var userid: String
    lateinit var id: String
    lateinit var title: String
    lateinit var requestbtn: Button

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
        circularProgressIndicator = binding.progressCircular
        requestbtn = binding.requestbtn
        binding.rtitle.text = post.title
        binding.rdesc.text = post.desc
        binding.rdetailimg.load(post.image)
        postDetailsViewModel.postDetailsViewModel = post
        binding.requestbtn.setOnClickListener {
            postDetailsViewModel.sendFoodRequest()
        }

        postDetailsViewModel.detailsResult.observe(viewLifecycleOwner) { detailsResult ->
            when (detailsResult) {
                is DetailsResult.GetFoodRequestSuccessAlreadySent -> {
                    Log.d("result of detailsResult", detailsResult.toString())
                    binding.requestbtn.isEnabled = false
                    binding.requestbtn.setBackgroundResource(R.drawable.graycurve)
                    binding.requestbtn.text = "Already Sent"

                    binding.progressCircular.visibility = View.GONE
                }
                is DetailsResult.SendFoodRequestSuccess -> {
                    Log.d("SendFoodRequestSuccess", detailsResult.toString())
                    binding.requestbtn.isEnabled = false
                    binding.requestbtn.text = "Request Sent"
                    binding.requestbtn.setBackgroundResource(R.drawable.graycurve)
                    requireContext().showToast("Request Sent")
                    // findNavController().navigateUp()
                }
                is DetailsResult.Error -> {
                    requireContext().showToast("Request Failed")
                }

                is DetailsResult.GetFoodRequestSuccessAccepted -> {
                    Log.d("result of detailsResult", detailsResult.toString())
                    requestbtn.setBackgroundResource(R.drawable.greencurve)
                    requestbtn.text = "Accepted"
                    requestbtn.isEnabled = false
                }
            }
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        postDetailsViewModel.getFoodRequest()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
