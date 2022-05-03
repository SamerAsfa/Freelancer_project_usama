package com.example.myapplication.myapplication.ui.face2

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.Camera
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.myapplication.myapplication.R


class FaceDetectionActivity : AppCompatActivity() , ImageBitmapListener {

    private val RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM = 0x01 shl 9

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, FaceDetectionActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_detection)
        callCameraPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setFragment()
        }
    }


    protected fun setFragment() {
        val mCamera = getCameraInstance()
        val mPreview = mCamera?.let { CameraPreview(this, it , this) }
        setContentView(mPreview);
    }

    fun getCameraInstance(): Camera? {
        var c: Camera? = null
        try {
            c = Camera.open()
        } catch (e: java.lang.Exception) {

        }
        return c
    }


    private fun callCameraPermission() {
        try {
            ActivityCompat.requestPermissions(
                this@FaceDetectionActivity,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM,
            )
        } catch (ex: Exception) {
            ex.message
        }
    }

    override fun showStreamingImagesBitmap(bitmap: Bitmap) {
         print("")
    }

}