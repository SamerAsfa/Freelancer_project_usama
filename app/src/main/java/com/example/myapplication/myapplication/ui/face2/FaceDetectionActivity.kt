package com.example.myapplication.myapplication.ui.face2

//import com.homesoft.encoder.Muxer
//import com.homesoft.encoder.MuxingCompletionListener
//import com.homesoft.encoder.TAG

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.ctech.bitmp4.Encoder
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.models.FaceBundle
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_face_detection.*
import org.jcodec.api.awt.AWTSequenceEncoder
import org.jcodec.common.io.NIOUtils
import org.jcodec.common.model.ColorSpace.RGB
import org.jcodec.common.model.Picture
import org.jcodec.common.model.Rational
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.System.out
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.ArrayList


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
    val arrayOfImages: ArrayList<Bitmap> = ArrayList()
    val arrayOfImagesFinalImages: ArrayList<Bitmap> = ArrayList()
    val linkedImageArray: LinkedHashMap<String, Bitmap> = LinkedHashMap()
    val hashOfSet: HashMap<Int, Int> = HashMap()
    var smileLock: Boolean = true
    var leftEyeLock: Boolean = true
    var rightEyeLock: Boolean = true
    var turnFaceToLeftLock: Boolean = true
    var turnFaceToRightLock: Boolean = true
    var statePublic: Boolean = false
    var dialog: ProgressDialog? = null
    var file: File? = null
    var faceBundle: FaceBundle? = null

    ///
    var stringValues: StringBuilder = StringBuilder()

    private lateinit var exportDisposable: Disposable
    private lateinit var encoder: Encoder
    var camera: Camera? = null
    var arrayOfTextActions = ArrayList<String>()

    companion object {
        val FACE_BUNDLE = "faceBundle"
        val RESULT = "result"
        fun startActivity(context: Context, faceBundle: FaceBundle): Intent {
            val intent = Intent(context, FaceDetectionActivity::class.java)
            intent.putExtra(FACE_BUNDLE, faceBundle)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_detection)
        sorter()
        getFaceBundleExtra()
        createRandomIndex()
        callCameraPermission()
    }


    fun sorter() {
        arrayOfTextActions.add("Keep Smiling")
        arrayOfTextActions.add("Keep Left Eye Closed")
        arrayOfTextActions.add("Keep Right Eye Closed")
        arrayOfTextActions.add("Keep Your Face on Left")
        arrayOfTextActions.add("Keep Your Face on Right")
        arrayOfTextActions.shuffle();
    }


    fun getFaceBundleExtra() {
        try {
            faceBundle = intent.getParcelableExtra<FaceBundle>(FACE_BUNDLE)
            actionsToCheck = faceBundle?.numberOfActions!!
        } catch (ex: Exception) {
            ex.message
        }
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
            getFinalImageArray()
            runOnUiThread {
                drStateTextView.text = "Wait to configuration"
                showDialog()
                container.removeAllViews()
            }
            if (faceBundle?.uploadAsImage == true) {
                bitmapToFile(arrayOfImagesFinalImages[0])
                endIntent()
            } else {
                convertImagesToVideo()
            }

        }
    }
//452145

    override fun onDestroy() {
        super.onDestroy()
        timerToSchedule.cancel()
        timerToProcess.cancel()
    }

    fun showDialog() {
        dialog = ProgressDialog.show(this, "", "Loading...", true);
    }

    fun hideDialog() {
        dialog?.hide()
    }

    fun getFinalImageArray() {
        linkedImageArray.values.forEach { bitmap ->
            arrayOfImagesFinalImages.add(bitmap)
        }

        if (arrayOfImages.size > 12) {
            for (i in 9..11) {
                arrayOfImagesFinalImages.add(arrayOfImages[i])
            }
        }

    }

    fun creteRootPath(): File? {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                file = File(
                    getApplicationContext().getExternalFilesDir("")
                        .toString() + File.separator + "UsamaSd.mp4"
                )
            } else {
                file =
                    File(Environment.getExternalStorageDirectory().absolutePath.toString() + File.separator + "UsamaSd.mp4")
            }
            if (file?.exists() == true) {
                file?.mkdirs()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            file // it will return null
        }
        return file
    }

    fun bitmapToFile(bitmap: Bitmap): File? {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                file = File(
                    getApplicationContext().getExternalFilesDir("")
                        .toString() + File.separator + "UsamaImage.jpg"
                )
            } else {
                file =
                    File(Environment.getExternalStorageDirectory().absolutePath.toString() + File.separator + "UsamaImage.jpg")
            }
            if (file!!.exists()) {
                file!!.mkdirs()
            }
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return file?.absoluteFile
    }

    fun convertImagesToVideo() {
        try {
            val output = creteRootPath()
            val enc =
                AWTSequenceEncoder.createWithFps(NIOUtils.writableChannel(output), Rational.R(7, 2))
            for (bitmap in arrayOfImagesFinalImages) {
                enc.encodeNativeFrame(fromBitmaps(bitmap))
            }
            enc.finish()
        } finally {
            NIOUtils.closeQuietly(out);
        }
        endIntent()
    }

    fun endIntent() {
        val returnIntent = Intent()
        returnIntent.putExtra(RESULT, file?.absoluteFile.toString())
        returnIntent.putExtra(FACE_BUNDLE, faceBundle)
        setResult(RESULT_OK, returnIntent)
        finish()
    }

    fun fromBitmaps(src: Bitmap): Picture {
        val dst: Picture = Picture.create(src.width, src.height, RGB)
        AndroidUtil.fromBitmap(src, dst)
        return dst
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
        if (getActionByName(getActionIndex()) == ActionsType.SMILING.ordinal) {
            smileLock()
            if (smileProb > smileThrushHold) {
                smileOpen()
            }
        } else if (getActionByName(getActionIndex()) == ActionsType.LEFT_EYE_CLOSED.ordinal) {
            leftEyeLock()
            if (rightEyeOpenProb <= closeLeftEyeThrushHold) {
                leftEyeOpen()
            }
        } else if (getActionByName(getActionIndex()) == ActionsType.RIGHT_EYE_CLOSED.ordinal) {
            rightEyeLock()
            if (leftEyeOpenProb <= closeRightEyeThrushHold) {
                rightEyeOpen()
            }
        } else
            if (getActionByName(getActionIndex()) == ActionsType.HEAD_LEFT.ordinal) {
                turnFaceToLeftLock()
                if (leftHeadAngleY != 0f) {
                    if (leftHeadAngleY >= leftHeadThrushHold) {
                        turnFaceToLeftOpen()
                    }
                }
            } else if (getActionByName(getActionIndex()) == ActionsType.HEAD_RIGHT.ordinal) {
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

    fun getActionByName(index : Int) : Int{
        if(arrayOfTextActions.get(index)=="Keep Smiling"){
            return 0
        }else if(arrayOfTextActions.get(index)=="Keep Left Eye Closed"){
            return 1
        }else if(arrayOfTextActions.get(index)=="Keep Right Eye Closed"){
            return 2
        }else if(arrayOfTextActions.get(index)=="Keep Your Face on Left"){
            return 3
        }else {
            return 4
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
        try {
            if (getFrontCameraId() == -1) {
                camera = Camera.open()
            } else {
                camera = Camera.open(getFrontCameraId())
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
        return camera
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
        arrayOfImages.add(bitmap)
        linkedImageArray.put(drStateTextView.text.toString(), bitmap)
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