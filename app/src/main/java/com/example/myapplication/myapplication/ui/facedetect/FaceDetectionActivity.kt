package com.example.myapplication.myapplication.ui.facedetect

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.media.ImageReader
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Surface
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.myapplication.myapplication.R
import kotlinx.android.synthetic.main.activity_face_detection.*
import kotlinx.android.synthetic.main.fragment_camera2_basic.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.io.File


class FaceDetectionActivity : AppCompatActivity(), ImageReader.OnImageAvailableListener {

    private val RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM = 0x01 shl 9
    var previewHeight = 0
    var previewWidth = 0
    var sensorOrientation = 0


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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    protected fun setFragment() {
        val manager =
            getSystemService(Context.CAMERA_SERVICE) as CameraManager
        var cameraId: String? = null
        try {
            cameraId = manager.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
//        val fragment: Fragment
//        val camera2Fragment = CameraConnectionFragment.newInstance(
//            object :
//                CameraConnectionFragment.ConnectionCallback {
//                override fun onPreviewSizeChosen(size: Size?, rotation: Int) {
//                    previewHeight = size!!.height
//                    previewWidth = size.width
//                    sensorOrientation = rotation - getScreenOrientation()
//                }
//            },
//            this,
//            R.layout.camera_fragment,
//            Size(640, 480)
//        )
//        camera2Fragment.setCamera(cameraId)
//        fragment = camera2Fragment
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.container, fragment)
//            commit()
//        }
        supportFragmentManager.beginTransaction().apply {
            replace(
                R.id.container, Camera2BasicFragment.newInstance(
                    this@FaceDetectionActivity
                )
            )
            commit()
        }
    }


    fun setImageFile(imgFile : File){
        Glide.with(this)
            .load(imgFile).into(imageFile)
    }

    protected fun getScreenOrientation(): Int {
        return when (windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_270 -> 270
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_90 -> 90
            else -> 0
        }
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

    override fun onImageAvailable(reader: ImageReader?) {
        print("")
    }

}