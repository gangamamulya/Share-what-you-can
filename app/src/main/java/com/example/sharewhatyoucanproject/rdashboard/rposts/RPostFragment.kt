package com.example.sharewhatyoucanproject.rdashboard.rposts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.sharewhatyoucanproject.Adapters.PostAdapter
import com.example.sharewhatyoucanproject.databinding.FragmentRpostBinding
import com.example.sharewhatyoucanproject.rposts.RPostViewModel
import com.example.sharewhatyoucanproject.rposts.RPostViewModelFactory
import com.example.sharewhatyoucanproject.rposts.TaskResult
import com.example.sharewhatyoucanproject.utils.showToast
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RPostFragment : Fragment() {
    private var _binding: FragmentRpostBinding? = null
    private val binding get() = _binding!!
    private lateinit var foodList: RecyclerView
    private lateinit var postAdapter: PostAdapter
    lateinit var circularProgressIndicator: CircularProgressIndicator
    private lateinit var rPostViewModel: RPostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rPostViewModel = ViewModelProvider(
            this,
            RPostViewModelFactory(),
        )[RPostViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRpostBinding.inflate(inflater, container, false)
        circularProgressIndicator = binding.progressCircular

        binding.progressCircular.visibility = View.VISIBLE
        foodList = binding.foodlist
        postAdapter = PostAdapter(requireContext()) { postModel ->
            val action =
                RPostFragmentDirections.actionDashboardFragmentToRDetailsFragment(
                    Json.encodeToString(
                        postModel,
                    ),
                )
            findNavController().navigate(action)
        }
        foodList.adapter = postAdapter
        foodList.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        rPostViewModel.getlist()

        rPostViewModel.taskResult.observe(viewLifecycleOwner) { taskResult ->
            when (taskResult) {
                is TaskResult.Success -> {
                    binding.progressCircular.visibility = View.GONE
                    postAdapter.setPostsList(taskResult.arrayList)
                }
                is TaskResult.Error -> {
                    circularProgressIndicator.visibility = View.GONE
                    requireContext().showToast("Failed")
                }
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
