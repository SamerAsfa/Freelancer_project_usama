package com.example.myapplication.myapplication.ui

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.R.attr.bitmap
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.error.VolleyError
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.BaseActivity
import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.base.ResponseApi
import com.example.myapplication.myapplication.data.BaseRequest
import com.example.myapplication.myapplication.data.POSTMediasTask
import com.example.myapplication.myapplication.models.FaceBundle
import com.example.myapplication.myapplication.models.UserModel
import com.example.myapplication.myapplication.ui.face2.FaceDetectionActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.login_with_face_activity.*
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


//@SuppressLint("StaticFieldLeak")
//private var mTextResult: TextView? = null
@SuppressLint("StaticFieldLeak")
//private var mImageResult: ImageView? = null
private val PERMISSIONS = arrayOf(Manifest.permission.CAMERA, WRITE_EXTERNAL_STORAGE)
private val RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM = 0x01 shl 9

class StartActivity : BaseActivity() {


    val VIDEO_CAPTURE = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_with_face_activity)
        getFcm()
        customLogin.setOnClickListener(View.OnClickListener {
            loginBy()
        })

        createTextView.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            this.startActivity(intent)
        }

    }


    fun getFcm() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val token = task.result
            LongTermManager.getInstance().notificationsToken = token
        })

    }

    fun loginBy() {
        if (ActivityCompat.checkSelfPermission(
                this@StartActivity,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startRecorder()
        }
        ActivityCompat.requestPermissions(
            this@StartActivity,
            PERMISSIONS,
            RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM,
        )
    }


    fun startRecorder() {
        ActivityCompat.startActivityForResult(
            this, FaceDetectionActivity.startActivity(
                this,
                FaceBundle(numberOfActions = 1, uploadAsImage = true),
            ), VIDEO_CAPTURE, null
        )
    }

//    private fun startCustomActivity() {
//        val intent = Intent(this, CustomDetectionActivity::class.java)
//        this.startActivityForResult(intent, 44)
//    }

    // Permission application callback.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            start/Recorder()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
    ) {
        val filePath = (intent?.extras?.get("result") as String)
        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                if (!filePath.isNullOrEmpty()) {
                    toggleProgressDialog(
                        show = true,
                        this,
                        this.resources.getString(R.string.loading)
                    )
                    POSTMediasTask().uploadMedia(
                        this@StartActivity,
                        BaseRequest.loginApi,
                        filePath,
                        object : ResponseApi {
                            override fun onSuccessCall(response: String?) {
                                toggleProgressDialog(
                                    show = false,
                                    this@StartActivity,
                                    this@StartActivity.resources.getString(R.string.loading)
                                )
                                response?.let {
                                    val userModelToSave = UserModel().parse(it)
                                    LongTermManager.getInstance().userModel = userModelToSave
                                    NavigationActivity().clearAndStart(this@StartActivity)
                                }

                            }

                            override fun onErrorCall(error: VolleyError?) {
                                toggleProgressDialog(
                                    show = false,
                                    this@StartActivity,
                                    this@StartActivity.resources.getString(R.string.loading)
                                )
                                showDialogOneButtonsCustom(
                                    "Error",
                                    error?.message.toString(),
                                    "Ok"
                                ) { dialog ->
                                    dialog.dismiss()
                                }
                            }

                        }
                    )
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(
                    this, "Not correct detection",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this, "Not correct detection",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        super.onActivityResult(
            requestCode,
            resultCode,
            intent
        )
    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
//        super.onActivityResult(requestCode, resultCode, intent)
//        if (requestCode == 44 && resultCode == 44) {
////            val isLive = intent?.getBooleanExtra("isLive", false)
////           val bitmap = BitmapFactory.decodeStream(this@StartActivity.openFileInput("myImage"))
//            loginBy()
//            Toast.makeText(
//                this@StartActivity,
//                "Face is not recognized",
//                Toast.LENGTH_LONG
//            ).show()
//        } else {
////            Toast.makeText(
////                this,
////                "onActivityResult requestCode $requestCode, resultCode $resultCode",
////                Toast.LENGTH_LONG
////            ).show()
//        }
//    }

}