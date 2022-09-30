package com.example.sharewhatyoucanproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.sharewhatyoucanproject.databinding.FragmentHomeBinding
import com.example.sharewhatyoucanproject.donor.DonorActivity
import com.example.sharewhatyoucanproject.utils.showToast
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    var latitude: Double = 44.5581222
    var longitude: Double = -123.2734361
    lateinit var locationRequest: LocationRequest
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.nameofuser.text = "" + FirebaseAuth.getInstance().currentUser?.displayName
        locationRequest = LocationRequest.create()
        binding.checklocation.setOnClickListener {
            if (context?.let { it1 ->
                ActivityCompat.checkSelfPermission(
                        it1,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                    )
            } != PackageManager.PERMISSION_GRANTED && context?.let { it1 ->
                    ActivityCompat.checkSelfPermission(
                            it1,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                        )
                } != PackageManager.PERMISSION_GRANTED
            ) {
            }
            activity?.let { it1 ->
                LocationServices.getFusedLocationProviderClient(it1)
                    .requestLocationUpdates(
                        locationRequest,
                        object : LocationCallback() {
                            override fun onLocationResult(locationresult: LocationResult) {
                                super.onLocationResult(locationresult)
                                if (locationresult.locations != null) {
                                    if (locationresult.locations.size > 0) {
                                        val index: Int = locationresult.locations.size - 1
                                        latitude =
                                            locationresult.locations[index].latitude
                                        longitude =
                                            locationresult.locations[index].longitude
                                        context?.showToast("latitude:- $latitude longitude: $longitude")
                                    } else {
                                        context?.showToast("size is 0: ${locationresult.locations.size}")
                                    }
                                } else {
                                    context?.showToast("it is null")
                                }
                            }
                        },
                        Looper.getMainLooper(),
                    )
            }
        }
        binding.convertbtn.setOnClickListener {
            val point = GeoPoint(latitude, longitude)
            context?.showToast("Converted Successful: $point")
        }

        binding.uploadlayout.setOnClickListener {
            val i = Intent(activity, DonorActivity::class.java)
            startActivity(i)
        }
        return view
    }
}
