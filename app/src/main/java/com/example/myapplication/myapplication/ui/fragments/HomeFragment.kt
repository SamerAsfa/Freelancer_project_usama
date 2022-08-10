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
import android.os.Looper
import android.provider.Settings
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.error.VolleyError
import com.bumptech.glide.Glide
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.BaseFragment
import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.base.ResponseApi
import com.example.myapplication.myapplication.data.APIClient
import com.example.myapplication.myapplication.data.APIInterface
import com.example.myapplication.myapplication.data.AppUtils.actionToTake
import com.example.myapplication.myapplication.data.AppUtils.getTextBody
import com.example.myapplication.myapplication.data.AppUtils.getTextRes
import com.example.myapplication.myapplication.data.BaseRequest.Companion.BREAKInApi
import com.example.myapplication.myapplication.data.BaseRequest.Companion.BREAKOutApi
import com.example.myapplication.myapplication.data.BaseRequest.Companion.LEAVEINApi
import com.example.myapplication.myapplication.data.BaseRequest.Companion.LEAVEOutApi
import com.example.myapplication.myapplication.data.BaseRequest.Companion.PINApi
import com.example.myapplication.myapplication.data.BaseRequest.Companion.POUTApi
import com.example.myapplication.myapplication.data.DateUtils
import com.example.myapplication.myapplication.data.POSTMediasTask
import com.example.myapplication.myapplication.models.ActionModel
import com.example.myapplication.myapplication.models.FaceBundle
import com.example.myapplication.myapplication.models.UserModel
import com.example.myapplication.myapplication.ui.adapters.ButtonsCasesAdapter
import com.example.myapplication.myapplication.ui.face2.FaceDetectionActivity
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*


private val PERMISSIONS = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)
private val RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM = 0x01 shl 9

class HomeFragment : BaseFragment() {
    var state: ButtonState = ButtonState.PUNCH_IN
    val PERMISSION_ID = 42
    var userModel: UserModel? = null

   lateinit var mFusedLocationClient: FusedLocationProviderClient
    protected var mainView: View? = null
    var location: Location? = null
    var apiInterface: APIInterface? = null
    var isInsideOrgnisation = false
    var isFakeLocation = false
    var showDialogOnce = false
    private var mLocationRequest: LocationRequest? = null

    companion object {
        val fragmentName: String = "HomeFragment"
        val homeFragment: HomeFragment? = null
        fun getInstance(): HomeFragment {
            return homeFragment ?: HomeFragment()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userModel = LongTermManager.getInstance().userModel
        startLocationUpdates()
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        try {
            apiInterface = APIClient.client?.create(APIInterface::class.java)
        } catch (ex: Exception) {
            ex.message
        }
//        mFusedLocationClient =
//            requireActivity().let { LocationServices.getFusedLocationProviderClient(it) }
    }

    @SuppressLint("MissingPermission")
    protected fun startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = LocationRequest.create()
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest?.interval = 1
        mLocationRequest?.fastestInterval = 1

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        val locationSettingsRequest = builder.build()

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        val settingsClient = LocationServices.getSettingsClient(requireActivity())
        settingsClient.checkLocationSettings(locationSettingsRequest)

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)

        if (checkPermissions()) {
            if (isLocationEnabled()) {
                startGetLocation()
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
    fun startGetLocation() {


        Looper.myLooper()?.let {
            mFusedLocationClient = getFusedLocationProviderClient(requireActivity())
            mFusedLocationClient.requestLocationUpdates(
                mLocationRequest!!,locationUpdate,
                it
            )
        }
    }


    var locationUpdate = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            myLocation(locationResult.lastLocation)
        }
    }

    override fun onPause() {
        super.onPause()
        stopUsingGPS()
    }


    override fun onDestroy() {
        super.onDestroy()
        stopUsingGPS()
    }

    override fun onStop() {
        super.onStop()
        stopUsingGPS()
    }

    fun stopUsingGPS() {
       mFusedLocationClient.removeLocationUpdates(locationUpdate)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        mainView = view
        getDashBoard()
        if (!userModel?.profile_photo_path?.trim()?.isBlank()!!) {
            Glide.with(view.context)
                .load(userModel?.profile_photo_path).into(view.userImage)
        }
        listeners()
        view.userNameTextView?.text = userModel?.name
        return view
    }



    fun listeners() {
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
        if (requestCode == 44 && resultCode == AppCompatActivity.RESULT_OK) {
            val fileExtra = intent?.getStringExtra(FaceDetectionActivity.RESULT)
            val faceBundle =
                intent?.getParcelableExtra<FaceBundle>(FaceDetectionActivity.FACE_BUNDLE)
            faceBundle?.url?.let {
                if (fileExtra != null) {
                    uploadImage(it, fileExtra)
                }
            }
        }
    }


    fun uploadImage(url: String, filePath: String) {
        toggleProgressDialog(
            show = true,
            requireActivity(),
            requireActivity().resources.getString(R.string.loading)
        )
        val maps: MutableMap<String, String> = HashMap()
        maps.put("Authorization", "Bearer ${userModel?.token}")
        maps.put("Accept", "application/json")
        POSTMediasTask().uploadMediaWithHeader(
            requireActivity(),
            url,
            filePath,
            object : ResponseApi {
                override fun onSuccessCall(response: String?) {
                    toggleProgressDialog(
                        show = false,
                        requireActivity(),
                        requireContext().resources.getString(R.string.loading)
                    )
                    showSuccessDialog()
                    getDashBoard()
                }

                override fun onErrorCall(error: VolleyError?) {
                    toggleProgressDialog(
                        show = false,
                        requireActivity(),
                        requireContext().resources.getString(R.string.loading)
                    )
                    showDialogOneButtonsCustom("Error", error?.message.toString(), "Ok") { dialog ->
                        dialog.dismiss()
                    }
                }
            }, maps
        )
    }


    private fun startCustomActivity(url: String) {
        this.startActivityForResult(
            FaceDetectionActivity.startActivity(
                requireContext(),
                FaceBundle(numberOfActions = 0, uploadAsImage = true, url = url)
            ), 44
        )
    }


    private fun getDashBoard() {
        try {
            val call = apiInterface?.getDashBoardApi()
            call?.enqueue(object : retrofit2.Callback<ActionModel?> {
                override fun onResponse(
                    call: retrofit2.Call<ActionModel?>,
                    response: retrofit2.Response<ActionModel?>
                ) {
                    val actionModel = response.body()

                    // new commit by samer
                  //  setPINPOUTView(actionModel)
                    addTopAdapter(actionModel as ActionModel)
                    addBottomAdapter(actionModel as ActionModel)
                }

                override fun onFailure(call: retrofit2.Call<ActionModel?>, t: Throwable) {
                    call.cancel()
                }
            })
        } catch (ex: Exception) {
            ex.message
        }
    }


    // new commit by samer

/*    fun setPINPOUTView(actionModel: ActionModel?) {
        actionModel?.out.toString().isBlank().let {
            if (it) {
                mainView?.punchInSTextView?.text = "--:--"
            } else {
                mainView?.punchInSTextView?.text = actionModel?.out?.let { it1 ->
                    DateUtils().getTime(
                        it1.toLong()
                    )
                }
            }
        }
        actionModel?.inSide.toString().isBlank().let {
            if (it) {
                mainView?.punchOutSateTextView?.text = "--:--"
            } else {
                mainView?.punchOutSateTextView?.text = actionModel?.inSide?.let { it1 ->
                    DateUtils().getTime(
                        it1.toLong()
                    )
                }
            }
        }
    }*/


    fun addTopAdapter(actionModel: ActionModel) {
        try {
            val buttonsCasesAdapter = ButtonsCasesAdapter(
                handleTopActionsArray(actionModel.action),
                requireContext()
            ) { actionModel ->
                if (isInsideOrgnisation) {
                    callDialogState(actionModel)
                }
            }
            mainView?.topButtonsCaseRecyclerView?.setHasFixedSize(true)
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            mainView?.topButtonsCaseRecyclerView?.layoutManager = linearLayoutManager
            mainView?.topButtonsCaseRecyclerView?.adapter = buttonsCasesAdapter
        } catch (ex: Exception) {
            ex.message
        }
    }


    fun addBottomAdapter(actionModel: ActionModel) {
        try {
            val buttonsCasesAdapter = ButtonsCasesAdapter(
                handleBottomActionsArray(actionModel.action),
                requireContext()
            ) { actionModel ->
                if (isInsideOrgnisation) {
                    callDialogState(actionModel)
                }
            }
            mainView?.bottomButtonsCaseRecyclerView?.setHasFixedSize(true)
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            mainView?.bottomButtonsCaseRecyclerView?.layoutManager = linearLayoutManager
            mainView?.bottomButtonsCaseRecyclerView?.adapter = buttonsCasesAdapter
        } catch (ex: Exception) {
            ex.message
        }
    }

    fun callDialogState(actionModel: ActionModel?) {
        if (actionModel != null) {
            showDialogTowButtonsStateBunch(
                title = requireContext().resources.getString(getTextRes(actionModel.key.toString())),
                body = getTextBody(
                    punchOut = requireContext().resources.getString(getTextRes(actionModel.key.toString())),
                    timeIs = requireContext().resources.getString(R.string.time_is),
                    doYouWantTo = requireContext().resources.getString(R.string.do_you_want),
                    now = requireContext().resources.getString(R.string.now),
                    time = DateFormat.format("hh:mm", Date()).toString()
                ),
                okButtonText = requireContext().resources.getString(R.string.yes),
                canceButtonText = requireContext().resources.getString(R.string.nos),
                actionModel = actionModel,
                ok = { dialog, actionModel ->
                    dialog.dismiss()
                    actionAndRefresh(actionModel)
                },
                cancel = { dialog ->
                    dialog.dismiss()
                }
            )
        }
    }


    fun actionAndRefresh(actionModel: ActionModel?) {
        state = actionToTake(actionModel)
        loginBy()
    }


    fun handleTopActionsArray(actionArrayList: ArrayList<ActionModel>?): ArrayList<ActionModel> {
        val topArrayList: ArrayList<ActionModel> = ArrayList()
        actionArrayList?.forEach { actionModel ->
            if (actionModel.active == true) {
                actionModel.isLargeView = true
                topArrayList.add(actionModel)
            }
        }
        return topArrayList
    }

    fun handleBottomActionsArray(actionArrayList: ArrayList<ActionModel>?): ArrayList<ActionModel> {
        val bottomArrayList: ArrayList<ActionModel> = ArrayList()
        actionArrayList?.forEach { actionModel ->
            if (actionModel.active != true) {
                bottomArrayList.add(actionModel)
            }
        }
        return bottomArrayList
    }


    private fun getUrl(state: ButtonState): String {
        if (state == ButtonState.PUNCH_IN) {
            return PINApi
        } else if (state == ButtonState.PUNCH_OUT) {
            return POUTApi
        } else if (state == ButtonState.BREAK_IN ) {
            return BREAKInApi
        } else if (state == ButtonState.BREAK_OUT) {
            return BREAKOutApi
        } else if (state == ButtonState.LEAVE_IN) {
            return LEAVEINApi
        } else {
            return LEAVEOutApi
        }
    }

    enum class ButtonState {
        PUNCH_IN,
        PUNCH_OUT,
        BREAK_IN,
        BREAK_OUT,
        LEAVE_IN,
        LEAVE_OUT,
//        BREAK_IN,
//        BREAK_IN,
    }


    val METERS_IN_MILE = 1609.344

    fun milesToMeters(miles: Double): Double {
        return miles * METERS_IN_MILE
    }


    private fun distance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double,
        radius: Int
    ): Boolean {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        val inMeters = milesToMeters(dist)
        return inMeters <= radius
    }


    private fun distanceText(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double,
        radius: Int
    ): String {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        val inMeters = milesToMeters(dist)
        return "inMeters = ${inMeters} mils = ${dist} radius=${radius}}"
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }


    fun isFake(): Boolean {
        val extras = location?.isFromMockProvider()
        return extras == true
    }

    fun myLocation(location: Location) {
        this.location = location
        if (isFake()) {
            isFakeLocation = true
            if (!showDialogOnce) {
                showDialogOnce = true
                showDialogOneButtonsCustom(false, "Sorry You Are Using Fake Location", "Ok") {
                    requireActivity().finish()
                }
            }
        }
        if (!isFakeLocation) {
            mainView?.locationNameTextView?.text = "x"
            var lat = userModel?.location?.lat
            var lon = userModel?.location?.lng
            var radius = userModel?.location?.radius
            isInsideOrgnisation = radius?.let { rad ->
                lon?.toDouble()?.let { userLong ->
                    lat?.toDouble()?.let { userLat ->
                        distance(
                            location.latitude, location.longitude, userLat, userLong,
                            rad.toInt()
                        )
                    }
                }
            } == true
            if (!isInsideOrgnisation) {
                mainView?.locationNameTextView?.text = "You are out Side Organization"
                context?.resources?.getColor(R.color.read)
                    ?.let { mainView?.locationNameTextView?.setTextColor(it) }
                locationImage.background = requireContext().getDrawable(R.drawable.vector_location)
            } else {
                locationImage.background =
                    requireContext().getDrawable(R.drawable.vector_black_location)
                mainView?.locationNameTextView?.text = "You are in Side Organization"
                context?.resources?.getColor(R.color.black)
                    ?.let { mainView?.locationNameTextView?.setTextColor(it) }
            }
//        mainView?.locationNameTextView?.text = distanceText(location.latitude, location.longitude, 31.962333, 35.811096, 5)
            mainView?.timeTextView?.text = DateFormat.format("hh:mm", Date())
            mainView?.dateTextView?.text = DateUtils().getDateHome(System.currentTimeMillis())
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
        requestPermissions(arrayOf( Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ID)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                startGetLocation()
            }
        } else if (requestCode == RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCustomActivity(getUrl(state))
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}