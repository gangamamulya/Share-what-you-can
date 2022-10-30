package com.example.sharewhatyoucanproject.deliverfood

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharewhatyoucanproject.R
import com.example.sharewhatyoucanproject.adapters.DeliverAdapter
import com.example.sharewhatyoucanproject.databinding.FragmentDeliverBinding
import com.example.sharewhatyoucanproject.models.RequestModel
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class DeliverFragment : Fragment() {
    private var _binding: FragmentDeliverBinding? = null
    private val binding get() = _binding!!
    lateinit var circularProgressIndicator: CircularProgressIndicator
    private lateinit var donatelist: RecyclerView
    private lateinit var deliverAdapter: DeliverAdapter
    private lateinit var deliverViewModel: DeliverViewModel

    private val args: DeliverFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deliverViewModel = ViewModelProvider(
            this,
            DeliverViewModelFactory(),
        )[DeliverViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDeliverBinding.inflate(inflater, container, false)
        circularProgressIndicator = binding.progressCircular
        val requestModel = Json.decodeFromString<RequestModel>(args.requestModel)

        deliverAdapter = DeliverAdapter { postModel ->
            AlertDialog.Builder(activity)
                .setTitle("Donate Food")
                .setMessage("Are you sure you want to Donate this Food?")
                .setPositiveButton(
                    "yes",
                ) { dialog, which ->
                    deliverViewModel.donate(postModel, requestModel)
                }
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.donateimg)
                .show()
        }

        donatelist = binding.donatelist
        donatelist.layoutManager = LinearLayoutManager(requireContext())
        donatelist.adapter = deliverAdapter
        deliverViewModel.getItems()

        deliverViewModel.deliverResult.observe(viewLifecycleOwner) {
            it?.let { deliverResult ->
                when (deliverResult) {
                    is DeliverResult.DeliveredSuccess -> {
                        deliverAdapter.setDeliverList(deliverResult.List)
                    }
                    is DeliverResult.Error -> {}
                }
            }
        }

        deliverViewModel.donationResult.observe(viewLifecycleOwner) {
            it?.let { donationResult ->
                when (donationResult) {
                    is DonationResult.Success -> {
                        findNavController().navigateUp()
                    }
                }
            }
        }

        return binding.root
    }
}
