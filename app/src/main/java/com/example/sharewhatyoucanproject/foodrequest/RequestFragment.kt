package com.example.sharewhatyoucanproject.foodrequest

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
import com.example.sharewhatyoucanproject.adapters.RequestAdapter
import com.example.sharewhatyoucanproject.databinding.FragmentRequestBinding
import com.example.sharewhatyoucanproject.utils.showToast
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RequestFragment : Fragment() {
    private var _binding: FragmentRequestBinding? = null
    private val binding get() = _binding!!
    lateinit var user: String
    private lateinit var requestList: RecyclerView
    private lateinit var requestAdapter: RequestAdapter
    lateinit var circularProgressIndicator: CircularProgressIndicator
    private lateinit var requestViewModel: RequestViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestViewModel = ViewModelProvider(
            this,
            RequestViewModelFactory(),
        )[RequestViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRequestBinding.inflate(inflater, container, false)
        circularProgressIndicator = binding.progressCircular
        user = activity?.intent?.getStringExtra("user").toString()
        requestViewModel.user = user
        requestAdapter = RequestAdapter { requestModel, type ->
            requestViewModel.approveFood(type, requestModel)
        }

        requestList = binding.requestlist
        requestList.layoutManager = LinearLayoutManager(requireContext())
        requestList.adapter = requestAdapter
        requestList.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        requestViewModel.requestResult.observe(viewLifecycleOwner) { requestResult ->
            when (requestResult) {
                is RequestResult.GetRequestListSuccess -> {
                    binding.progressCircular.visibility = View.GONE
                    requestAdapter.setRequestList(requestResult.arrayList)
                    requestAdapter.notifyDataSetChanged()
                }
                is RequestResult.GetRequestInOrderSuccess -> {
                    binding.progressCircular.visibility = View.GONE
                    requestAdapter.setRequestList(requestResult.arrayList)
                    requestAdapter.notifyDataSetChanged()
                }
                is RequestResult.Error -> {
                    circularProgressIndicator.visibility = View.GONE
                    requireContext().showToast("Failed")
                }
            }
        }

        requestViewModel.approveFoodResult.observe(viewLifecycleOwner) { approveFoodResult ->
            approveFoodResult?.let {
                when (approveFoodResult) {
                    is ApproveResult.NavigateToDeliverScreen -> {
                        val action =
                            RequestFragmentDirections.actionRequestFragmentToDeliverFragment(
                                Json.encodeToString(approveFoodResult.requestModel),
                            )

                        findNavController().navigate(action)
                        requestViewModel.resetApproveFood()
                    }
                    is ApproveResult.ChangeButton -> {
                        requestAdapter.updateRequestListStatus(approveFoodResult.uid)
                        requestAdapter.notifyDataSetChanged()
                        requestViewModel.resetApproveFood()
                    }
                }
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        if (user == "" || user == "null") {
            requestViewModel.getrequests()
        } else {
            requestViewModel.getrequestbyuser()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
