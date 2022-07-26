package com.example.myapplication.myapplication.ui.face2

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.myapplication.myapplication.R
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import kotlinx.android.synthetic.main.activity_face_detection.*


class FaceDetectionActivity : AppCompatActivity(), ImageBitmapListener {

    private val RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM = 0x01 shl 9
    var detector: FirebaseVisionFaceDetector? = null
    var bitmap: Bitmap? = null
    val oneSecond : Long = 1000

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

        if (requestCode == RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setFragment()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    protected fun setFragment() {
        val mCamera = getCameraInstance()
        val mPreview = mCamera?.let { CameraPreview(this, it, this) }
        container.addView(mPreview);
//        timer()
    }

    fun getCameraInstance(): Camera? {
        var c: Camera? = null
        try {
            if (getFrontCameraId() == -1) {
                c = Camera.open()
            } else {
                c = Camera.open(getFrontCameraId())
            }
            var options = FirebaseVisionFaceDetectorOptions.Builder()
                .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .build()
            detector = FirebaseVision.getInstance().getVisionFaceDetector(options)
        } catch (e: java.lang.Exception) {
            e.message
        }
        return c
    }


    private fun getFrontCameraId(): Int {
        var camId = -1
        val numberOfCameras = Camera.getNumberOfCameras()
        val ci = CameraInfo()
        for (i in 0 until numberOfCameras) {
            Camera.getCameraInfo(i, ci)
            if (ci.facing == CameraInfo.CAMERA_FACING_FRONT) {
                camId = i
            }
        }
        return camId
    }

    fun timer() {
        try {
            val timer = object : CountDownTimer(oneSecond * 2, oneSecond) {
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    bitmap?.let { detectImage(it) }
                }
            }
            timer.start()
        } catch (ex: Exception) {
            ex.message
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

    override fun showStreamingImagesBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
        detectImage(bitmap)
    }


    private fun detectImage(bitmap: Bitmap) {
        try {
            val image = FirebaseVisionImage.fromBitmap(bitmap)
            detector!!.detectInImage(image)
                .addOnSuccessListener(object : OnSuccessListener<List<FirebaseVisionFace?>?> {
                    override fun onSuccess(faces: List<FirebaseVisionFace?>?) {
                        drStateTextView.text  = getInfoFromFaces(faces as List<FirebaseVisionFace>)
                    }
                }).addOnFailureListener(object : OnFailureListener {
                    override fun onFailure(e: java.lang.Exception) {

                    }
                })

        } catch (ex: Exception) {
            ex.message
        }
    }

    private fun getInfoFromFaces(faces: List<FirebaseVisionFace>): String {
        val result = StringBuilder()
        var smileProb = 0f
        var leftEyeOpenProb = 0f
        var rightEyeOpenProb = 0f
        var headEulerAngleY = 0f
        for (face in faces) {

            // If landmark detection was enabled (mouth, ears, eyes, cheeks, and nose available):

            // If classification was enabled:
            if (face.smilingProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                smileProb = face.smilingProbability
            }
            if (face.leftEyeOpenProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                leftEyeOpenProb = face.leftEyeOpenProbability
            }
            if (face.rightEyeOpenProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                rightEyeOpenProb = face.rightEyeOpenProbability
            }
            if (face.headEulerAngleY != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                headEulerAngleY = face.headEulerAngleY
            }
            result.append("Smile: ")
            if (smileProb > 0.5) {
                result.append("Yes")
            } else {
                result.append("No")
            }
            result.append("\nLeft eye: ")
            if (leftEyeOpenProb > 0.5) {
                result.append("Open")
            } else {
                result.append("Close")
            }
            result.append("\nRight eye: ")
            if (rightEyeOpenProb > 0.5) {
                result.append("Open")
            } else {
                result.append("Close")
            }

            result.append("\nface to: ")
            if (headEulerAngleY >=  0.18f) {
                result.append("left")
            }
            if (headEulerAngleY <= -0.18f){
                result.append("right")
            }
            result.append("\n\n")
        }
        return result.toString()
    }

}