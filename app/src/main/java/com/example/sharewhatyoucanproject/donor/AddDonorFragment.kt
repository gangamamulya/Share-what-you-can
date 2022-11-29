package com.example.sharewhatyoucanproject.donor

import android.Manifest
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.sharewhatyoucanproject.databinding.FragmentAddDonorBinding
import com.example.sharewhatyoucanproject.utils.PERMISSION_ALL
import com.example.sharewhatyoucanproject.utils.foodTypes
import com.example.sharewhatyoucanproject.utils.getCurrentLocation
import com.example.sharewhatyoucanproject.utils.hasPermissions
import com.example.sharewhatyoucanproject.utils.isGPSEnabled
import com.example.sharewhatyoucanproject.utils.locationPermissions
import com.example.sharewhatyoucanproject.utils.showToast
import com.google.android.gms.location.LocationRequest
import com.google.android.material.progressindicator.CircularProgressIndicator

class AddDonorFragment : Fragment() {
    private var _binding: FragmentAddDonorBinding? = null
    val binding get() = _binding!!

    lateinit var addDonorViewModel: AddDonorViewModel
    lateinit var locationRequest: LocationRequest
    lateinit var arrayAdapter: ArrayAdapter<String>

    lateinit var circularProgressIndicator: CircularProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addDonorViewModel = ViewModelProvider(
            this,
            AddDonorViewModelFactory(),
        )[AddDonorViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAddDonorBinding.inflate(inflater, container, false)

        circularProgressIndicator = binding.progressCircular
        locationRequest = LocationRequest.create()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()

        addDonorViewModel.donorResult.observe(viewLifecycleOwner) { donorResult ->
            when (donorResult) {
                is DonorResult.Success -> {
                    requireContext().showToast("Post Added")
                    findNavController().navigateUp()
                }
                is DonorResult.Error -> {
                    circularProgressIndicator.visibility = View.GONE
                    requireContext().showToast(donorResult.message)
                }
            }
        }

        binding.foodimg.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0)
        }

        arrayAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            foodTypes,
        )
        binding.spinner.adapter = arrayAdapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long,
            ) {
                foodTypes[position].also {
                    addDonorViewModel.setFoodType(it)
                    requireContext().showToast(it)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                addDonorViewModel.setFoodType(null.toString())
            }
        }

        binding.uploadbtn.setOnClickListener {
            addDonorViewModel.setFoodData(
                title = binding.titleet.text.toString(),
                description = binding.descet.text.toString(),
                hourSet = binding.hourset.text.toString(),
            )

            if (!addDonorViewModel.isDataCompleted()) {
                requireContext().showToast("Please Fill All Fields")
                return@setOnClickListener
            }

            if (addDonorViewModel.isImageNotSelected()) {
                requireContext().showToast("Choose a image")
                return@setOnClickListener
            }

            if (!hasPermissions(activity?.applicationContext, *locationPermissions)) {
                requireContext().showToast("Please Allow Location To Post")
                activity?.let {
                    ActivityCompat.requestPermissions(
                        it,
                        locationPermissions,
                        PERMISSION_ALL,
                    )
                }
                return@setOnClickListener
            }

            if (!isGPSEnabled(requireActivity())) {
                requireContext().showToast("Open Your GPS")
                return@setOnClickListener
            }

            circularProgressIndicator.visibility = View.VISIBLE
            if (addDonorViewModel.isFoodNotValid()) {
                circularProgressIndicator.visibility = View.GONE
                requireContext().showToast("You Cannot Upload Food")
                return@setOnClickListener
            }

            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION,
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling ActivityCompat#requestPermissions
            }

            getCurrentLocation(requireActivity()) { currentLocation ->
                addDonorViewModel.setCurrentLocation(currentLocation)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    addDonorViewModel.setFilepath(data.data)
                    binding.foodimg.load(data.data)
                }
            } else if (resultCode == RESULT_CANCELED) {
                requireContext().showToast("Canceled")
            }
        }
    }
}
