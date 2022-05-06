package com.example.myapplication.myapplication.ui.face2

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.myapplication.myapplication.R
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import kotlinx.android.synthetic.main.activity_face_detection.*
import java.util.*


class FaceDetectionActivity : AppCompatActivity(), ImageBitmapListener {

    private val RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM = 0x01 shl 9
    var detector: FirebaseVisionFaceDetector? = null
    val oneSecond: Long = 1000
    val oneLessSecond: Long = 100
    var currentIndex: Int = 0
    var actionsToCheck: Int = 4
    var imageBitmap: Bitmap? = null
    private val publicTimer = Timer()
    val arrayOfIndex: ArrayList<Int> = ArrayList()
    val lockCases: ArrayList<Boolean> = ArrayList()
    val hashOfSet: HashMap<Int, Int> = HashMap()
    var smileLock: Boolean = true
    var leftEyeLock: Boolean = true
    var rightEyeLock: Boolean = true
    var turnFaceToLeftLock: Boolean = true
    var turnFaceToRightLock: Boolean = true
    var first: Boolean = false
    var leftHeadThrushHold = 15
    var rightHeadThrushHold = -15
    var closeLeftEyeThrushHold = 0.006
    var closeRightEyeThrushHold = 0.006
    var smileThrushHold = 0.2
    var stringValues : StringBuilder = StringBuilder()

    val arrayOfTextActions = arrayOf(
        "Keep Smiling",
        "Keep Lef Eye Closed",
        "Keep Right Eye Closed",
        "Keep Your Face on Left",
        "Keep Your Face on Right"
    )

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, FaceDetectionActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_detection)
        createRandomIndex()
        callCameraPermission()
    }

    private fun createRandomIndex() {
        val numbers = listOf(0..actionsToCheck).random().shuffled()
        arrayOfIndex.addAll(numbers)
        for (i in 0..actionsToCheck) {
            lockCases.add(true)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setFragment()
            startTimerToSchedule()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun startRandomTextActions() {
        if (!isAllChecked()) {
            if (lockCases[getActionIndex()]) {
                val randomIndex = getRandomNumber()
                drStateTextView.text = arrayOfTextActions.get(randomIndex)
            }
            checkCases()
        } else {
            publicTimer.cancel()
            drStateTextView.text = "Done"
        }
    }


    private fun isAllChecked(): Boolean {
        return currentIndex == arrayOfIndex.size
    }


    private fun getActionIndex(): Int {
        return arrayOfIndex[currentIndex]
    }

    private fun getRandomNumber(): Int {
        val index = getActionIndex()
        return index
    }


    private fun setFragment() {
        val mCamera = getCameraInstance()
        val mPreview = mCamera?.let { CameraPreview(this, it, this) }
        container.addView(mPreview);
    }


    fun checkCases() {
        if (getActionIndex() == ActionsType.SMILING.ordinal) {
            smileLock()
            imageBitmap?.let { detectSmile(it) }
        } else if (getActionIndex() == ActionsType.LEFT_EYE_CLOSED.ordinal) {
            leftEyeLock()
            imageBitmap?.let { detectLeftEyeClosed(it) }
        } else if (getActionIndex() == ActionsType.RIGHT_EYE_CLOSED.ordinal) {
            rightEyeLock()
            imageBitmap?.let { detectRightEyeClosed(it) }
        } else if (getActionIndex() == ActionsType.HEAD_LEFT.ordinal) {
            turnFaceToLeftLock()
            imageBitmap?.let { detectLeftHead(it) }
        } else if (getActionIndex() == ActionsType.HEAD_RIGHT.ordinal) {
            turnFaceToRightLock()
            imageBitmap?.let { detectRightHead(it) }
        }
    }

    fun increase() {
        currentIndex += 1
    }

    fun smileLock() {
        smileLock = false
        lockCases[getActionIndex()] = smileLock
    }

    fun smileOpen() {
        if (!smileLock) {
            smileLock = true
            lockCases[getActionIndex()] = smileLock
            increase()
        }
    }


    fun leftEyeLock() {
        leftEyeLock = false
        lockCases[getActionIndex()] = leftEyeLock
    }

    fun leftEyeOpen() {
        if (!leftEyeLock) {
            leftEyeLock = true
            lockCases[getActionIndex()] = leftEyeLock
            increase()
        }
    }


    fun rightEyeLock() {
        rightEyeLock = false
        lockCases[getActionIndex()] = rightEyeLock
    }

    fun rightEyeOpen() {
        if (!rightEyeLock) {
            rightEyeLock = true
            lockCases[getActionIndex()] = rightEyeLock
            increase()
        }
    }

    fun turnFaceToLeftLock() {
        turnFaceToLeftLock = false
        lockCases[getActionIndex()] = turnFaceToLeftLock
    }

    fun turnFaceToLeftOpen() {
        if (!turnFaceToLeftLock) {
            turnFaceToLeftLock = true
            lockCases[getActionIndex()] = turnFaceToLeftLock
            increase()
        }
    }


    fun turnFaceToRightLock() {
        turnFaceToRightLock = false
        lockCases[getActionIndex()] = turnFaceToRightLock
    }

    fun turnFaceToRightOpen() {
        if (!turnFaceToRightLock) {
            turnFaceToRightLock = true
            lockCases[getActionIndex()] = turnFaceToRightLock
            increase()
        }
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
                .setPerformanceMode(FirebaseVisionFaceDetectorOptions.FAST)
                .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .build()
            detector = FirebaseVision.getInstance().getVisionFaceDetector(options)
        } catch (e: java.lang.Exception) {
            e.message
        }
        return c
    }

    private fun startTimerToSchedule() {
        publicTimer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    startRandomTextActions()
                }
            },
            0, oneSecond * 2
        )
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
        imageBitmap = bitmap
    }


    private fun detectSmile(bitmap: Bitmap) {
        try {
            Log.d("detect", "Start = ${System.currentTimeMillis()}")
            detector!!.detectInImage(FirebaseVisionImage.fromBitmap(bitmap))
                .addOnSuccessListener { faces ->
                    var smileProb = 0f
                    Log.d("detect", "befLoop = ${System.currentTimeMillis()}")
                    for (face in faces) {
                        Log.d("detect", "after = ${System.currentTimeMillis()}")
                        if (face.smilingProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                            smileProb = face.smilingProbability
                        }
                        stringValues.append("Smile : ${smileProb}\n")
                        addToText()
                        if (smileProb > smileThrushHold) {
                            smileOpen()
                        } else if (smileProb >= 0.01) {
                            smileLock()
                        }
                    }
                }
        } catch (ex: Exception) {
            ex.message
        }
    }


    private fun detectLeftEyeClosed(bitmap: Bitmap) {
        try {
            detector!!.detectInImage(FirebaseVisionImage.fromBitmap(bitmap))
                .addOnSuccessListener { faces ->
                    var rightEyeOpenProb = 0f
                    for (face in faces) {
                        if (face.rightEyeOpenProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                            rightEyeOpenProb = face.rightEyeOpenProbability
                        }
                        stringValues.append("LeftEyeClosed : ${rightEyeOpenProb}\n")
                        addToText()
                        if (rightEyeOpenProb <= closeLeftEyeThrushHold) {
                            leftEyeOpen()
                        } else if (rightEyeOpenProb >= 0.99) {
                            leftEyeLock()
                        }
                    }
                }
        } catch (ex: Exception) {
            ex.message
        }
    }

    private fun detectRightEyeClosed(bitmap: Bitmap) {
        try {
            detector!!.detectInImage(FirebaseVisionImage.fromBitmap(bitmap))
                .addOnSuccessListener { faces ->
                    var leftEyeOpenProb = 0f
                    for (face in faces) {
                        if (face.leftEyeOpenProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                            leftEyeOpenProb = face.leftEyeOpenProbability
                        }
                        stringValues.append("RightEyeClosed : ${leftEyeOpenProb}\n")
                        addToText()
                        if (leftEyeOpenProb <= closeRightEyeThrushHold) {
                            rightEyeOpen()
                        } else if (leftEyeOpenProb >= 0.99) {
                            rightEyeLock()
                        }
                    }
                }
        } catch (ex: Exception) {
            ex.message
        }
    }


    private fun detectLeftHead(bitmap: Bitmap) {
        try {
            detector!!.detectInImage(FirebaseVisionImage.fromBitmap(bitmap))
                .addOnSuccessListener { faces ->
                    var headEulerAngleY = 0f
                    for (face in faces) {
                        if (face.headEulerAngleY != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                            headEulerAngleY = face.headEulerAngleY
                        }
                        stringValues.append("LeftHead : ${headEulerAngleY}\n")
                        Log.d("myTag", headEulerAngleY.toString())
                        addToText()
                        if (headEulerAngleY >= leftHeadThrushHold) {
                            turnFaceToLeftOpen()
                        } else {
                            turnFaceToLeftLock()
                        }
                    }
                }
        } catch (ex: Exception) {
            ex.message
        }
    }

    private fun detectRightHead(bitmap: Bitmap) {
        try {
            detector!!.detectInImage(FirebaseVisionImage.fromBitmap(bitmap))
                .addOnSuccessListener { faces ->
                    var headEulerAngleY = 0f
                    for (face in faces) {
                        if (face.headEulerAngleY != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                            headEulerAngleY = face.headEulerAngleY
                        }
                        stringValues.append("RightHead : ${headEulerAngleY}\n")
                        addToText()
                        if (headEulerAngleY <= rightHeadThrushHold) {
                            turnFaceToRightOpen()
                        } else {
                            turnFaceToRightLock()
                        }
                    }
                }
        } catch (ex: Exception) {
            ex.message
        }
    }


    fun addToText(){
        textAll.text = stringValues
    }

    enum class ActionsType {
        SMILING, LEFT_EYE_CLOSED,
        RIGHT_EYE_CLOSED, HEAD_LEFT, HEAD_RIGHT
    }

}