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
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.android.synthetic.main.activity_face_detection.*
import okhttp3.internal.wait
import java.util.*


class FaceDetectionActivity : AppCompatActivity(), ImageBitmapListener {

    private val RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM = 0x01 shl 9
    var detector: FaceDetector? = null
    val oneSecond: Long = 1000
    val oneLessSecond: Long = 100
    var currentIndex: Int = 0
    var actionsToCheck: Int = 4
    var imageBitmap: Bitmap? = null

    private val timerToProcess = Timer()
    private val timerToSchedule = Timer()
    private val timerTo = Timer()
    val arrayOfIndex: ArrayList<Int> = ArrayList()
    val lockCases: ArrayList<Boolean> = ArrayList()
    val hashOfSet: HashMap<Int, Int> = HashMap()
    var smileLock: Boolean = true
    var leftEyeLock: Boolean = true
    var rightEyeLock: Boolean = true
    var turnFaceToLeftLock: Boolean = true
    var turnFaceToRightLock: Boolean = true
    var statePublic: Boolean = false

    ///
    var stringValues: StringBuilder = StringBuilder()


    val arrayOfTextActions = arrayOf(
        "Keep Smiling",
        "Keep Left Eye Closed",
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
            startTimerToProcess()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun startRandomTextActions() {
        if (!isAllChecked()) {
            val randomIndex = getRandomNumber()
            drStateTextView.text = arrayOfTextActions.get(randomIndex)
            checkCases()
        } else {
            timerToSchedule.cancel()
            timerToProcess.cancel()
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

    var leftHeadThrushHold = 30
    var rightHeadThrushHold = -30
    var closeLeftEyeThrushHold = 0.5
    var closeRightEyeThrushHold = 0.5
    var smileThrushHold = 0.028

    //
    var smileProb = 0f
    var rightEyeOpenProb = 0.80f
    var leftEyeOpenProb = 0.80f
    var leftHeadAngleY = 0f
    var rightHeadAngleY = 0f
    fun checkCases() {
        if (getActionIndex() == ActionsType.SMILING.ordinal) {
            smileLock()
            if (smileProb > smileThrushHold) {
                smileOpen()
            }
        } else if (getActionIndex() == ActionsType.LEFT_EYE_CLOSED.ordinal) {
            leftEyeLock()
            if (rightEyeOpenProb <= closeLeftEyeThrushHold) {
                leftEyeOpen()
            }
        } else if (getActionIndex() == ActionsType.RIGHT_EYE_CLOSED.ordinal) {
            rightEyeLock()
            if (leftEyeOpenProb <= closeRightEyeThrushHold) {
                rightEyeOpen()
            }
        } else
            if (getActionIndex() == ActionsType.HEAD_LEFT.ordinal) {
                turnFaceToLeftLock()
                if (leftHeadAngleY != 0f) {
                    if (leftHeadAngleY >= leftHeadThrushHold) {
                        turnFaceToLeftOpen()
                    }
                }
            } else if (getActionIndex() == ActionsType.HEAD_RIGHT.ordinal) {
                turnFaceToRightLock()
                if (rightHeadAngleY != 0f) {
                    if (rightHeadAngleY <= rightHeadThrushHold) {
                        turnFaceToRightOpen()
                    }
                }
            }
        Log.d("SMILING", "SMILING ${smileProb}")
        Log.d("LEFT_EYE_CLOSED", "LEFT_EYE_CLOSED ${rightEyeOpenProb}")
        Log.d("RIGHT_EYE_CLOSED", "RIGHT_EYE_CLOSED${leftEyeOpenProb}")
        Log.d("HEAD_LEFT", "HEAD_LEFT${leftHeadAngleY}")
        Log.d("HEAD_RIGHT", "HEAD_RIGHT${rightHeadAngleY}")
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
            var options = FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build()
            detector = FaceDetection.getClient(options)
        } catch (e: java.lang.Exception) {
            e.message
        }
        return c
    }

    private fun startTimerToSchedule() {
        timerToSchedule.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    startRandomTextActions()
                }
            },
            0, 10
        )
    }

    private fun startTimerToProcess() {
        timerToProcess.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    if (!statePublic) {
                        imageBitmap?.let { detectAll(it) }
                    }
                }
            },
            0, 10
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

    var state = false
    private fun detectAll(bitmap: Bitmap) {
        try {
            statePublic = true
            detector?.process(InputImage.fromBitmap(bitmap, 0))
                ?.addOnSuccessListener { faces ->
                    statePublic = false
                    if (faces.size > 0) {
                        smileProb = faces[0].smilingProbability!!
                        rightEyeOpenProb = faces[0].rightEyeOpenProbability!!
                        leftEyeOpenProb = faces[0].leftEyeOpenProbability!!
                        leftHeadAngleY = faces[0].headEulerAngleY
                        rightHeadAngleY = faces[0].headEulerAngleY
//                    for (face in faces) {
//                        smileProb = face.smilingProbability
//                        rightEyeOpenProb = face.rightEyeOpenProbability
//                        leftEyeOpenProb = face.leftEyeOpenProbability
//                        leftHeadAngleY = face.headEulerAngleY
//                        rightHeadAngleY = face.headEulerAngleY
//                    }
                    }
                }

        } catch (ex: Exception) {
            ex.message
        }
    }


    fun addToText() {
        textAll.text = stringValues
    }

    enum class ActionsType {
        SMILING, LEFT_EYE_CLOSED,
        RIGHT_EYE_CLOSED,
        HEAD_LEFT, HEAD_RIGHT
    }

}