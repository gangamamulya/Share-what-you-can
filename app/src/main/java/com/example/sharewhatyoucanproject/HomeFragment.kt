package com.example.sharewhatyoucanproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.sharewhatyoucanproject.DonorActivity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import com.google.type.LatLng

class HomeFragment : Fragment() {
    var uploadlayout: Button? = null
    var nameofuser: TextView? = null
    lateinit var checklocation: Button
    var latitude: Double = 44.5581222

    // -123.2734361
    var longitute: Double = -123.2734361
    var mytest: LatLng? = null
    var test: Long = 0L
    lateinit var convertbtn: Button
    lateinit var locationRequest: com.google.android.gms.location.LocationRequest
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        uploadlayout = view.findViewById(R.id.uploadlayout)
        val nameofuser = view.findViewById<TextView>(R.id.nameofuser)
        nameofuser.text = "" + FirebaseAuth.getInstance().currentUser!!.displayName
        checklocation = view.findViewById(R.id.checklocation)

        convertbtn = view.findViewById(R.id.convertbtn)
        locationRequest = com.google.android.gms.location.LocationRequest.create()
        checklocation.setOnClickListener(View.OnClickListener {

            if (context?.let { it1 ->
                    ActivityCompat.checkSelfPermission(
                        it1,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                } != PackageManager.PERMISSION_GRANTED && context?.let { it1 ->
                    ActivityCompat.checkSelfPermission(
                        it1,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                } != PackageManager.PERMISSION_GRANTED
            ) {


            }
            activity?.let { it1 ->
                LocationServices.getFusedLocationProviderClient(it1)
                    .requestLocationUpdates(locationRequest, object : LocationCallback() {
                        override fun onLocationResult(locationresult: LocationResult) {
                            super.onLocationResult(locationresult)

                            if (locationresult.locations != null) {
                                if (locationresult.getLocations().size > 0) {

                                    val index: Int = locationresult.getLocations().size - 1

                                    latitude =
                                        locationresult.getLocations().get(index).getLatitude()
                                    longitute =
                                        locationresult.getLocations().get(index).getLongitude()

                                    Toast.makeText(
                                        context,
                                        "latitude:- " + latitude + " " + "longitute: " + longitute,
                                        Toast.LENGTH_SHORT
                                    ).show()


                                } else {
                                    Toast.makeText(
                                        context,
                                        "size is 0: " + locationresult.locations.size,
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                            } else {
                                Toast.makeText(context, "it is null", Toast.LENGTH_SHORT).show()

                            }

                        }

                    }, Looper.getMainLooper()!!)
            }


        })


        convertbtn.setOnClickListener(View.OnClickListener {


//            val latitude1: Double = 44.5581222
//            val longitude1: Double = -123.2734361

            val point: GeoPoint = GeoPoint(latitude, longitute)

            Toast.makeText(context, "Converted Sucessful: " + point, Toast.LENGTH_SHORT).show()
        })

        uploadlayout!!.setOnClickListener(View.OnClickListener {
            // val mynew = LatLng(45.0, 123.0)


            // mytest.latitude = latitude;
//            val location = LatLng(latitude, longitude)
            val i = Intent(activity, DonorActivity::class.java)
            startActivity(i)
        })
        return view
    }
}