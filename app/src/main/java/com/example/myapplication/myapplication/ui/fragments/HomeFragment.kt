package com.example.myapplication.myapplication.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
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
import com.android.volley.error.VolleyError
import com.bumptech.glide.Glide
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.base.ResponseApi
import com.example.myapplication.myapplication.data.BaseRequest
import com.example.myapplication.myapplication.data.BaseRequest.Companion.BREAKApi
import com.example.myapplication.myapplication.data.BaseRequest.Companion.LEAVEApi
import com.example.myapplication.myapplication.data.BaseRequest.Companion.PINApi
import com.example.myapplication.myapplication.data.BaseRequest.Companion.POUTApi
import com.example.myapplication.myapplication.data.DateUtils
import com.example.myapplication.myapplication.data.POSTMediasTask
import com.example.myapplication.myapplication.models.UserModel
import com.example.myapplication.myapplication.ui.PunchCamDetectionActivity
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*

private val PERMISSIONS = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)
private val RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM = 0x01 shl 9

class HomeFragment : Fragment(R.layout.fragment_home) {
    var state: ButtonState = ButtonState.PUNCH_IN
    val PERMISSION_ID = 42
    var userModel: UserModel? = null
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    protected var mainView: View? = null
    var location: Location? = null

    companion object {
        val fragmentName : String = "HomeFragment"


        val homeFragment: HomeFragment? = null
        fun getInstance(): HomeFragment {
            return homeFragment ?: HomeFragment()
        }
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
        getDashBoard()
        callButtonRippleState(view)
        if (!userModel?.profile_photo_path?.trim()?.isBlank()!!) {
            Glide.with(view.context)
                .load(userModel?.profile_photo_path).into(view.userImage)

        }
        listeners()
        view.userNameTextView?.text = userModel?.name
        view.timeTextView?.text = DateFormat.format("hh:mm", Date())
        view.dateTextView?.text = DateFormat.format("EEEE,MMMyy", Date())
        return view
    }

    fun listeners() {
        mainView?.punchIn?.setOnClickListener {
            state = ButtonState.PUNCH_IN
            loginBy()
        }
        mainView?.punchOut?.setOnClickListener {
            state = ButtonState.PUNCH_OUT
            loginBy()
        }
        mainView?.breaks?.setOnClickListener {
            state = ButtonState.BREAK
            loginBy()
        }
        mainView?.leave?.setOnClickListener {
            state = ButtonState.LEAVE
            loginBy()
        }
        mainView?.punchInImageView?.setOnClickListener {

            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=${location?.latitude},${location?.longitude}&daddr=${location?.latitude},${location?.longitude}")
            )
            startActivity(intent)
        }
    }

    private fun loginBy() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCustomActivity(getUrl(state))
        }
        ActivityCompat.requestPermissions(
            requireActivity(),
            PERMISSIONS,
            RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM,
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == 44 && resultCode == 44) {
            loginBy()
            Toast.makeText(
                requireContext(),
                "Face is not recognized",
                Toast.LENGTH_LONG
            ).show()
        } else {
            mainView?.let { callButtonRippleState(it) }
        }
    }

    private fun startCustomActivity(url: String) {
        val intent = Intent(requireContext(), PunchCamDetectionActivity::class.java)
        intent.putExtra("urlPath", url)
        this.startActivityForResult(intent, 44)
    }


    fun getDashBoard() {
        POSTMediasTask().get(
            requireActivity(),
            BaseRequest.dashboardApi,
            "",
            object : ResponseApi {
                override fun onSuccessCall(response: String?) {
                    response?.let {
                        val userModelObject = UserModel().parse(it)
                        val userModel: UserModel = LongTermManager.getInstance().userModel
                        val mainUserModel = UserModel(
                            userModel.id,
                            userModel.name,
                            userModel.email,
                            userModel.profile_photo_path,
                            userModel.token,
                            userModelObject?.inSide,
                            userModelObject?.out,
                            userModel.location,
                            userModelObject?.actionModel
                        )
                        LongTermManager.getInstance().userModel = mainUserModel
                        mainView?.let { callButtonRippleState(it) }
                    }
                }

                override fun onErrorCall(error: VolleyError?) {
                    print("")
                }
            }
        )
    }


    private fun callButtonRippleState(view: View) {
        try {
            userModel = LongTermManager.getInstance().userModel
            view.punchInSTextView.text = userModel?.inSide?.toLong()
                ?.let { DateUtils().getTime(it) }
            userModel?.out?.isBlank().let {
                if (it == true) {
                    view.punchOutSateTextView.text = "--:--"
                } else {
                    view.punchOutSateTextView.text = userModel?.out
                }
            }
            requireActivity().runOnUiThread {
                changeMainTo(ButtonState.DEFAULT)
                userModel?.actionModel?.forEach {
                    if (it?.name == "PIN") {
                        view.punchIn.startRippleAnimation()
                        if (it.diable == false) {
                            view.punchInImage.setBackgroundResource(R.drawable.disro)
                            view.punchIn.stopRippleAnimation()
                        }
                        view.punchInImage.isEnabled = !it.diable!!
                        if (it.active == true) {
                            view.punchIn.stopRippleAnimation()
                            changeMainTo(ButtonState.PUNCH_IN)
                            view.punchInImage?.setBackgroundResource(R.drawable.disro)
                            view.punchInImage?.isEnabled = false
                        }
                    } else if (it?.name == "POUT") {
                        view.punchOut.startRippleAnimation()
                        if (it.diable == false) {
                            view.punchOut.stopRippleAnimation()
                            view.punchOutImage.setBackgroundResource(R.drawable.disro)
                        }
                        view.punchOutImage.isEnabled = !it.diable!!
                        if (it.active == true) {
                            view.punchOut.stopRippleAnimation()
                            changeMainTo(ButtonState.PUNCH_OUT)
                            view.punchOutImage?.setBackgroundResource(R.drawable.disro)
                            view.punchOutImage?.isEnabled = false
                        }
                    } else if (it?.name == "BREAK") {
                        view.breaks.startRippleAnimation()
                        if (it.diable == false) {
                            view.breaks.stopRippleAnimation()
                            view.breakImage.setBackgroundResource(R.drawable.disro)
                        }
                        view.breakImage.isEnabled = !it.diable!!
                        if (it.active == true) {
                            view.breaks.stopRippleAnimation()
                            changeMainTo(ButtonState.BREAK)
                            view.breakImage?.setBackgroundResource(R.drawable.disro)
                            view.breakImage?.isEnabled = false
                        }
                    } else if (it?.name == "LEAVE") {
                        view.leave.startRippleAnimation()
                        if (it.diable == false) {
                            view.leave.stopRippleAnimation()
                            view.leaveImage.setBackgroundResource(R.drawable.disro)
                        }
                        view.leaveImage.isEnabled = !it.diable!!
                        if (it.active == true) {
                            view.leave.stopRippleAnimation()
                            changeMainTo(ButtonState.LEAVE)
                            view.leaveImage?.setBackgroundResource(R.drawable.disro)
                            view.leaveImage?.isEnabled = false
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            ex.message
        }
    }

    private fun getUrl(state: ButtonState): String {
        if (state == ButtonState.PUNCH_IN) {
            return PINApi
        } else if (state == ButtonState.PUNCH_OUT) {
            return POUTApi
        } else if (state == ButtonState.BREAK) {
            return BREAKApi
        } else {
            return LEAVEApi
        }
    }


    fun changeMainTo(state: ButtonState) {
        if (state == ButtonState.PUNCH_IN) {
            mainView?.mainInRippleBackground?.startRippleAnimation()
            mainView?.mainInRippleBackground?.visibility = View.VISIBLE
            mainView?.mainOutRippleBackground?.visibility = View.INVISIBLE
            mainView?.mainBreakRippleBackground?.visibility = View.INVISIBLE
            mainView?.mainLeaveRippleBackground?.visibility = View.INVISIBLE
        } else if (state == ButtonState.PUNCH_OUT) {
            mainView?.mainOutRippleBackground?.startRippleAnimation()
            mainView?.mainOutRippleBackground?.visibility = View.VISIBLE
            mainView?.mainInRippleBackground?.visibility = View.INVISIBLE
            mainView?.mainBreakRippleBackground?.visibility = View.INVISIBLE
            mainView?.mainLeaveRippleBackground?.visibility = View.INVISIBLE
        } else if (state == ButtonState.BREAK) {
            mainView?.mainBreakRippleBackground?.startRippleAnimation()
            mainView?.mainBreakRippleBackground?.visibility = View.VISIBLE
            mainView?.mainInRippleBackground?.visibility = View.INVISIBLE
            mainView?.mainOutRippleBackground?.visibility = View.INVISIBLE
            mainView?.mainLeaveRippleBackground?.visibility = View.INVISIBLE
        } else if (state == ButtonState.LEAVE) {
            mainView?.mainLeaveRippleBackground?.startRippleAnimation()
            mainView?.mainLeaveRippleBackground?.visibility = View.VISIBLE
            mainView?.mainInRippleBackground?.visibility = View.INVISIBLE
            mainView?.mainOutRippleBackground?.visibility = View.INVISIBLE
            mainView?.mainBreakRippleBackground?.visibility = View.INVISIBLE
        } else {
            mainView?.mainLeaveRippleBackground?.stopRippleAnimation()
            mainView?.mainInRippleBackground?.stopRippleAnimation()
            mainView?.mainOutRippleBackground?.stopRippleAnimation()
            mainView?.mainBreakRippleBackground?.stopRippleAnimation()
            mainView?.mainLeaveRippleBackground?.visibility = View.INVISIBLE
            mainView?.mainInRippleBackground?.visibility = View.INVISIBLE
            mainView?.mainOutRippleBackground?.visibility = View.INVISIBLE
            mainView?.mainBreakRippleBackground?.visibility = View.INVISIBLE
        }
    }


    enum class ButtonState {
        PUNCH_IN,
        PUNCH_OUT,
        BREAK,
        LEAVE,
        DEFAULT,
    }

    fun myLocation(location: Location) {
        this.location = location
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
        } else if (requestCode == RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCustomActivity(getUrl(state))
        }
    }
}