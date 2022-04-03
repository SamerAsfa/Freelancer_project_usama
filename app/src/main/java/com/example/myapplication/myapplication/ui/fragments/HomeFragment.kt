package com.example.myapplication.myapplication.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.models.UserModel
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*


class HomeFragment : Fragment(R.layout.fragment_home) {

    val PERMISSION_ID = 42
    var userModel: UserModel? = null
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    protected var mainView: View? = null


    companion object {
        fun getInstance() = HomeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userModel = LongTermManager.getInstance().userModel
        mFusedLocationClient =
            requireActivity().let { LocationServices.getFusedLocationProviderClient(it) }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        getLastLocation()
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        mainView = view
        try {
            requireActivity().runOnUiThread {
                userModel?.actionModel?.forEach {
                    if (it.name == "PIN") {
                        if (it.active == true) {
                            view.mainRippleBackground.startRippleAnimation()
                        }
//                        if (it.diable == false) {
//                            view.mainRippleBackground.setBackgroundResource(R.drawable.disro)
//                        }
                        view.mainRippleBackground.isEnabled = !it.diable!!
                    } else if (it.name == "POUT") {
                        if (it.active == true) {
                            view.punchOut.startRippleAnimation()
                        }
//                        if (it.diable == false) {
//                            view.punchOut.setBackgroundResource(R.drawable.disro)
//                         }
                        view.punchOut.isEnabled = !it.diable!!
                    } else if (it.name == "BREAK") {
                        if (it.active == true) {
                            view.breaks.startRippleAnimation()
                        }
//                        if (it.diable == false) {
//                            view.breaks.setBackgroundResource(R.drawable.disro)
//                        }
                        view.breaks.isEnabled = !it.diable!!
                    } else if (it.name == "LEAVE") {
                        if (it.active == true) {
                            view.leave.startRippleAnimation()
                        }
//                        if (it.diable == false) {
//                            view.leave.setBackgroundResource(R.drawable.disro)
//                        }
                        view.leave.isEnabled = !it.diable!!
                    }
                }
            }
        } catch (ex: Exception) {
            ex.message
        }
        if (!userModel?.profile_photo_path?.trim()?.isBlank()!!) {
            Glide.with(view.context)
                .load(userModel?.profile_photo_path).into(view.userImage)

        }
        view.punchInTextView.setOnClickListener {
            getLastLocation()
        }
        view.userNameTextView?.text = userModel?.name
        view.timeTextView?.text = DateFormat.format("hh:mm", Date())
        view.dateTextView?.text = DateFormat.format("EEEE,MMMyy", Date())
        return view
    }


    fun myLocation(location: Location) {
        mainView?.locationNameTextView?.text = "x"
        var lat = userModel?.location?.lat
        var lon = userModel?.location?.lng
        var radius = userModel?.location?.radius
        var state = radius?.toDouble()?.let { isMarkerOutsideCircle(location, lat, lon, it) }
        if (state == true) {
            mainView?.locationNameTextView?.text = "You are out Side Organization"
            context?.resources?.getColor(R.color.read)
                ?.let { mainView?.locationNameTextView?.setTextColor(it) }
        } else {
            mainView?.locationNameTextView?.text = "You are in Side Organization"
            context?.resources?.getColor(R.color.black)
                ?.let { mainView?.locationNameTextView?.setTextColor(it) }
        }
    }

    private fun isMarkerOutsideCircle(
        centerLatLng: Location,
        lat: String?,
        logs: String?,
        radius: Double
    ): Boolean {
        val distances = FloatArray(1)
        lat?.toDouble()?.let { latitude ->
            logs?.toDouble()?.let { longitude ->
                Location.distanceBetween(
                    centerLatLng.latitude,
                    centerLatLng.longitude,
                    latitude,
                    longitude, distances
                )
            }
        }
        return radius < distances[0]
    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    requestNewLocationData()
                }
            } else {
                Toast.makeText(requireActivity(), "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                Looper.myLooper()?.let {
                    mFusedLocationClient.requestLocationUpdates(
                        mLocationRequest, mLocationCallback,
                        it
                    )
                }
                mainHandler.postDelayed(this, 1000)
            }
        })
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            myLocation(mLastLocation)
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (activity?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }


    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }
}