package com.example.myapplication.myapplication.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.myapplication.myapplication.R
import com.huawei.hms.mlsdk.livenessdetection.MLLivenessCapture
import com.huawei.hms.mlsdk.livenessdetection.MLLivenessCaptureConfig
import com.huawei.hms.mlsdk.livenessdetection.MLLivenessCaptureResult
import com.huawei.hms.mlsdk.livenessdetection.MLLivenessDetectView
import kotlinx.android.synthetic.main.activity_start.*

@SuppressLint("StaticFieldLeak")
private var mTextResult: TextView? = null
@SuppressLint("StaticFieldLeak")
private var mImageResult: ImageView? = null
private val PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
//private val RC_CAMERA_AND_EXTERNAL_STORAGE_DEFAULT = 0x01 shl 8
private val RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM = 0x01 shl 9
class StartActivity : AppCompatActivity() {


    companion object {
        val customCallback: MLLivenessCapture.Callback = object : MLLivenessCapture.Callback {
            override fun onSuccess(result: MLLivenessCaptureResult) {
                mTextResult!!.text = result.toString()
                mTextResult!!.setBackgroundResource(if (result.isLive) R.drawable.bg_blue else R.drawable.bg_red)
                mImageResult?.setImageBitmap(result.bitmap)
            }
            override fun onFailure(errorCode: Int) {
                mTextResult!!.text = "errorCode:$errorCode"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        mTextResult = findViewById(R.id.text_detect_result)
        mImageResult = findViewById(R.id.img_detect_result)

//        default_btn.setOnClickListener (View.OnClickListener {
//            if (ActivityCompat.checkSelfPermission(this@StartActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                startCaptureActivity()
//                return@OnClickListener
//            }
//            ActivityCompat.requestPermissions(this@StartActivity, PERMISSIONS, RC_CAMERA_AND_EXTERNAL_STORAGE_DEFAULT)
//        })
        custom_btn.setOnClickListener (View.OnClickListener {
            if (ActivityCompat.checkSelfPermission(this@StartActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                startCustomActivity()
                return@OnClickListener
            }
            ActivityCompat.requestPermissions(this@StartActivity, PERMISSIONS, RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM)
        })

    }

//    // Callback for receiving the liveness detection result.
//    private val callback: MLLivenessCapture.Callback = object : MLLivenessCapture.Callback {
//        override fun onSuccess(result: MLLivenessCaptureResult) {
//            mTextResult!!.text = result.toString()
//            mTextResult!!.setBackgroundResource(if (result.isLive) R.drawable.bg_blue else R.drawable.bg_red)
//            mImageResult?.setImageBitmap(result.bitmap)
//        }
//        @SuppressLint("SetTextI18n")
//        override fun onFailure(errorCode: Int) {
//            mTextResult!!.text = "errorCode:$errorCode"
//        }
//    }
//
//    private fun startCaptureActivity() {
//        // Obtain liveness detection configuration and set detect mask and sunglasses.
//        val captureConfig = MLLivenessCaptureConfig.Builder().setOptions(MLLivenessDetectView.DETECT_MASK).build()
//        // Obtains the liveness detection plug-in instance.
//        val capture = MLLivenessCapture.getInstance()
//        // Set liveness detection configuration.
//        capture.setConfig(captureConfig)
//        // Enable liveness detection.
//        capture.startDetect(this, callback)
//    }

    private fun startCustomActivity() {
        val intent = Intent(this, CustomDetectionActivity::class.java)
        this.startActivity(intent)
    }

    // Permission application callback.
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Toast.makeText(this, "onRequestPermissionsResult", Toast.LENGTH_LONG).show()
//        if (requestCode == RC_CAMERA_AND_EXTERNAL_STORAGE_DEFAULT && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            startCaptureActivity()
//        }
        if (requestCode == RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCustomActivity()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        Toast.makeText(this, "onActivityResult requestCode $requestCode, resultCode $resultCode", Toast.LENGTH_LONG).show()
    }

}