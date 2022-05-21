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
import android.graphics.Canvas
import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.ctech.bitmp4.Encoder
import com.ctech.bitmp4.MP4Encoder
import com.example.myapplication.myapplication.R
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_face_detection.*
import org.jcodec.api.awt.AWTSequenceEncoder
import org.jcodec.common.io.NIOUtils
import org.jcodec.common.model.ColorSpace.RGB
import org.jcodec.common.model.Picture
import org.jcodec.common.model.Rational
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.System.out
import java.util.*
import java.util.concurrent.TimeUnit


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
    val hashOfSet: HashMap<Int, Int> = HashMap()
    var smileLock: Boolean = true
    var leftEyeLock: Boolean = true
    var rightEyeLock: Boolean = true
    var turnFaceToLeftLock: Boolean = true
    var turnFaceToRightLock: Boolean = true
    var statePublic: Boolean = false
    var dialog: ProgressDialog? = null
    var file: File? = null

    ///
    var stringValues: StringBuilder = StringBuilder()

    private lateinit var exportDisposable: Disposable
    private lateinit var encoder: Encoder
    var camera: Camera? = null
    val arrayOfTextActions = arrayOf(
        "Keep Smiling",
        "Keep Left Eye Closed",
        "Keep Right Eye Closed",
        "Keep Your Face on Left",
        "Keep Your Face on Right"
    )

    companion object {
        fun startActivity(context: Context): Intent {
            return Intent(context, FaceDetectionActivity::class.java)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_detection)

        creteRootPath()
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
            getFinalImageArray()
            runOnUiThread {
                drStateTextView.text = "Wait to configuration"
                showDialog()
                container.removeAllViews()
            }
            convertImagesToVideo()
        }
    }


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
        for (i in 1..4) {
            arrayOfImagesFinalImages.add(arrayOfImages[i])
        }
    }

    fun creteRootPath(): File? {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                file =  File(getApplicationContext().getExternalFilesDir("").toString() + File.separator + "UsamaSd.mp4")
            } else {
                file = File(Environment.getExternalStorageDirectory().absolutePath.toString() + File.separator + "UsamaSd.mp4")
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


    fun convertImagesToVideo() {
        try {
            val output = creteRootPath()
            val enc =
                AWTSequenceEncoder.createWithFps(NIOUtils.writableChannel(output), Rational.R(2, 1))
            for (bitmap in arrayOfImagesFinalImages) {
                enc.encodeNativeFrame(fromBitmaps(bitmap))
            }
            enc.finish()
        } finally {
            NIOUtils.closeQuietly(out);/////storage/emulated/0/UsamaSd.mp4
        }
        endIntent()
    }

    fun endIntent() {
        val returnIntent = Intent()
        returnIntent.putExtra("result", file?.absoluteFile)
        setResult(RESULT_OK, returnIntent)
        finish()
    }

    fun fromBitmaps(src: Bitmap): Picture {
        val dst: Picture = Picture.create(src.width, src.height, RGB)
        AndroidUtil.fromBitmap(src, dst)
        return dst
    }

    fun fromBitmap(src: Bitmap, dst: Picture) {
        val dstData: ByteArray? = dst.getPlaneData(0)
        val packed = IntArray(src.width * src.height)
        src.getPixels(packed, 0, src.width, 0, 0, src.width, src.height)
        var i = 0
        var srcOff = 0
        var dstOff = 0
        while (i < src.height) {
            var j = 0
            while (j < src.width) {
                val rgb = packed[srcOff]
                dstData?.set(dstOff, (rgb shr 16 and 0xff).toByte())
                dstData?.set(dstOff + 1, (rgb shr 8 and 0xff).toByte())
                dstData?.set(dstOff + 2, (rgb and 0xff).toByte())
                j++
                srcOff++
                dstOff += 3
            }
            i++
        }
    }

    fun addImagesToVideo() {
        try {
//            val downloarDir = getExternalFilesDir(null)
            val exportedFile = creteRootPath()
            encoder = MP4Encoder()
            encoder.setFrameDelay(50)
            encoder.setOutputFilePath(exportedFile?.path)
        } catch (ex: Exception) {
            ex.stackTrace
        }
    }

    private fun startExport() {
        encoder.setOutputSize(100, 200)
        encoder.startEncode()
        exportDisposable = Observable.interval(30, TimeUnit.MILLISECONDS)
            .map {
                arrayOfImagesFinalImages
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                encoder.addFrame(it[0])

            }
        Handler().postDelayed({
            stopExport()
        }, 400)
    }

    private fun createBitmapFromView(v: View): Bitmap {
        val bitmap = Bitmap.createBitmap(
            v.width,
            v.height,
            Bitmap.Config.ARGB_8888
        )

        val c = Canvas(bitmap)
        v.draw(c)
        return bitmap
    }

    private fun stopExport() {
        encoder.stopEncode()
        exportDisposable.dispose()
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