package com.example.myapplication.myapplication.ui

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import com.android.volley.error.VolleyError
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.BaseActivity
import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.base.ResponseApi
import com.example.myapplication.myapplication.data.BaseRequest
import com.example.myapplication.myapplication.data.BaseRequest.Companion.registerApi
import com.example.myapplication.myapplication.data.POSTMediasTask
import com.example.myapplication.myapplication.models.FaceBundle
import com.example.myapplication.myapplication.models.UserModel
import com.example.myapplication.myapplication.ui.face2.FaceDetectionActivity
import com.example.myapplication.myapplication.utils.PathUtil
import kotlinx.android.synthetic.main.activity_create_account.*


class CreateAccountActivity : BaseActivity(), ResponseApi {
    var userModel: UserModel? = null
    private val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val VIDEO_CAPTURE = 101
    val CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 1011
    private val RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM = 0x01 shl 9
    fun String.equalsIgnoreCase(other: String) =
        (this as java.lang.String).equalsIgnoreCase(other)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        createTextView.setOnClickListener {
            finish()
        }
        startVideoRecording()
        custom_btn.setOnClickListener {
            if (emailEditText.text.toString().equalsIgnoreCase(reEmailEditText.text.toString())) {
                if (passwordEditText.text.toString()
                        .equalsIgnoreCase(retypePasswordEditText.text.toString())
                ) {
                    toggleProgressDialog(
                        show = true,
                        this,
                        this.resources.getString(R.string.loading)
                    )
                    var maps: MutableMap<String, String> = HashMap()
                    maps.put("name", nameEditText.text.toString())
                    maps.put("email", emailEditText.text.toString())
                    maps.put("password", passwordEditText.text.toString())
                    maps.put("code", organizationEditText.text.toString())
                    maps.put("mobile_number", numberPhoneEditText.text.toString())
                    maps.put("fcm", LongTermManager.getInstance().notificationsToken)
                    if (userModel == null) {
                        POSTMediasTask().post(
                            this@CreateAccountActivity,
                            registerApi,
                            maps,
                            this@CreateAccountActivity
                        )
                    } else {
                        recordVideo(userModel!!)
                    }
                } else {
                    Toast.makeText(
                        this, "Password is mismatch",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    this, "Email is mismatch",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onSuccessCall(response: String?) {
        toggleProgressDialog(show = false, this, this.resources.getString(R.string.loading))
        response?.let {
            this.userModel = UserModel().parse(it)
            if (ActivityCompat.checkSelfPermission(
                    this@CreateAccountActivity,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                userModel?.let { it1 -> recordVideo(it1) }
            }
            ActivityCompat.requestPermissions(
                this@CreateAccountActivity,
                PERMISSIONS,
                RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM,
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            userModel?.let { recordVideo(it) }
        }
    }

//    private fun startCustomActivity(userModel: UserModel) {
//        val intent = Intent(this, CustomWithVideo::class.java)
//        intent.putExtra("user", userModel)
//        this.startActivityForResult(intent, 44)
//    }

    fun recordVideo(userModel: UserModel) {
        ActivityCompat.startActivityForResult(
            this, FaceDetectionActivity.startActivity(
                this,
                FaceBundle(numberOfActions = 4)
            ), VIDEO_CAPTURE, null
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
    ) {
        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                if(!intent?.extras?.getString("result").isNullOrEmpty()){
                    startVideoRecording()
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(
                    this, "Video recording cancelled.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this, "Failed to record video",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            var filePath = PathUtil.getPath(this, Uri.parse(intent?.data.toString()))
            uploadeVideo(filePath)
        }
        super.onActivityResult(
            requestCode,
            resultCode,
            intent
        )
    }

    fun uploadeVideo(filePath:String?) {
        toggleProgressDialog(show = true, this, this.resources.getString(R.string.loading))
        var maps: MutableMap<String, String> = HashMap()
        maps.put("Authorization", "Bearer ${userModel?.token}")
        maps.put("Accept", "application/json")
        POSTMediasTask().uploadMediaWithHeader(
            this@CreateAccountActivity,
            BaseRequest.uploadVodApi,
            filePath.toString(),
            object : ResponseApi {
                override fun onSuccessCall(response: String?) {
                    toggleProgressDialog(
                        show = false,
                        this@CreateAccountActivity,
                        this@CreateAccountActivity.resources.getString(R.string.loading)
                    )
                    response?.let {
                        LongTermManager.getInstance().userModel = userModel
                        NavigationActivity().clearAndStart(this@CreateAccountActivity)
                    }
                }

                override fun onErrorCall(error: VolleyError?) {
                    toggleProgressDialog(
                        show = false,
                        this@CreateAccountActivity,
                        this@CreateAccountActivity.resources.getString(R.string.loading)
                    )
                    showDialogOneButtonsCustom(
                        "Error",
                        error?.message.toString(),
                        "Ok"
                    ) { dialog ->
                        dialog.dismiss()
                    }
                }

            }, maps
        )
    }

    fun startVideoRecording() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        intent.putExtra(
            "android.intent.extras.CAMERA_FACING",
            android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1)
        } else {
            intent.putExtra("android.intent.extras.CAMERA_FACING", 1)
        }
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 1)
        intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true)
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)
        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE)
    }

    override fun onErrorCall(error: VolleyError?) {
        toggleProgressDialog(show = false, this, this.resources.getString(R.string.loading))
        showDialogOneButtonsCustom("Error", error?.message.toString(), "Ok") { dialog ->
            dialog.dismiss()
        }
        val clipboard = this.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", error?.message.toString())
        clipboard.setPrimaryClip(clip)
    }


}