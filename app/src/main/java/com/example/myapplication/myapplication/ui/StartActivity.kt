package com.example.myapplication.myapplication.ui

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.R.attr.bitmap
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.myapplication.myapplication.R
import com.huawei.hms.mlsdk.livenessdetection.MLLivenessCapture
import com.huawei.hms.mlsdk.livenessdetection.MLLivenessCaptureResult
import kotlinx.android.synthetic.main.activity_custom_detection.*
import kotlinx.android.synthetic.main.activity_start.*
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

class StartActivity : AppCompatActivity() {

    val customCallback: MLLivenessCapture.Callback = object : MLLivenessCapture.Callback {
        override fun onSuccess(result: MLLivenessCaptureResult) {
            Toast.makeText(
                this@StartActivity,
                "onRequestPermissionsResult",
                Toast.LENGTH_LONG
            ).show()
//                mTextResult!!.text = result.toString()
//                mTextResult!!.setBackgroundResource(if (result.isLive) R.drawable.bg_blue else R.drawable.bg_red)
//                mImageResult?.setImageBitmap(result.bitmap)
        }

        override fun onFailure(errorCode: Int) {
//                mTextResult!!.text = "errorCode:$errorCode"
        }
    }
//        }
//        }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_with_face_activity)
//        mTextResult = findViewById(R.id.text_detect_result)
//        mImageResult = findViewById(R.id.img_detect_result)
        customLogin.setOnClickListener(View.OnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this@StartActivity,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startCustomActivity()
                return@OnClickListener
            }
            ActivityCompat.requestPermissions(
                this@StartActivity,
                PERMISSIONS,
                RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM,
            )


        })

        createTextView.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            this.startActivity(intent)
        }

    }


    private fun startCustomActivity() {
        val intent = Intent(this, CustomDetectionActivity::class.java)
        this.startActivityForResult(intent, 44)
    }

    // Permission application callback.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Toast.makeText(this, "onRequestPermissionsResult", Toast.LENGTH_LONG).show()
        if (requestCode == RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCustomActivity()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == 44 && resultCode == 44) {
            val isLive = intent?.getBooleanExtra("isLive", false)
//           val bitmap = BitmapFactory.decodeStream(this@StartActivity.openFileInput("myImage"))
            Toast.makeText(
                this@StartActivity,
                "Face is not recognized",
                Toast.LENGTH_LONG
            ).show()
        } else {
//            Toast.makeText(
//                this,
//                "onActivityResult requestCode $requestCode, resultCode $resultCode",
//                Toast.LENGTH_LONG
//            ).show()
        }
    }

}