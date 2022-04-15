package com.example.myapplication.myapplication.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.error.VolleyError
import com.example.myapplication.myapplication.HiltApplication
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.base.ResponseApi
import com.example.myapplication.myapplication.data.POSTMediasTask
import com.example.myapplication.myapplication.models.ActionModel
import com.example.myapplication.myapplication.models.UserModel
import com.huawei.hms.mlsdk.livenessdetection.MLLivenessCaptureResult
import com.huawei.hms.mlsdk.livenessdetection.MLLivenessDetectView
import com.huawei.hms.mlsdk.livenessdetection.OnMLLivenessDetectCallback
import kotlinx.android.synthetic.main.activity_custom_detection.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class PunchCamDetectionActivity : AppCompatActivity(), ResponseApi {

    private var mlLivenessDetectView: MLLivenessDetectView? = null
    private var mPreviewContainer: FrameLayout? = null
    private var img_back: ImageView? = null
    val VIDEO_CAPTURE = 101
    var urlPath: String? = null
    var userModel: UserModel? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state)
        getIn()
        mPreviewContainer = findViewById(R.id.surface_layout)
        userModel = LongTermManager.getInstance().userModel
        val outMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels
        mlLivenessDetectView = MLLivenessDetectView.Builder()
            .setContext(this)
            .setOptions(MLLivenessDetectView.DETECT_MASK) // set Rect of face frame relative to surface in layout
            .setFaceFrameRect(Rect(0, 0, widthPixels, dip2px(this, 780f)))
            .setDetectCallback(object : OnMLLivenessDetectCallback {
                override fun onCompleted(result: MLLivenessCaptureResult) {
                    val value = Intent()
                    val isLive = result.isLive
                    if (isLive) {
                        var maps: MutableMap<String, String> = HashMap()
                        maps.put("Authorization", "Bearer ${userModel?.token}")
//                        maps.put("Authorization", "Bearer 1|7fsrJprTOi4jyHLWFTICI7Tgs2B6ZhxwBTrZKTLL")
                        maps.put("Accept", "application/json")
                        POSTMediasTask().uploadMediaWithHeader(
                            this@PunchCamDetectionActivity,
                            urlPath,
                            bitmapToFile(result.bitmap, "usama.JPEG")?.absolutePath,
                            this@PunchCamDetectionActivity, maps
                        )
                    } else {
                        value.putExtra("isLive", isLive)
                        setResult(44, value)
                        finish()
                    }
                }

                override fun onError(error: Int) {
                    finish()
                }

                override fun onInfo(infoCode: Int, bundle: Bundle) {

                }

                override fun onStateChange(state: Int, bundle: Bundle) {

                }
            }).build()

        mPreviewContainer!!.addView(mlLivenessDetectView)
        mlLivenessDetectView!!.onCreate(savedInstanceState)

        regEmailButton.setOnClickListener {
            finish()
            val intent = Intent(this, CreateAccountActivity::class.java)
            this.startActivity(intent)
        }
    }

    fun getIn() {
        val intent = intent
        urlPath = intent.getStringExtra("urlPath")
    }


    fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? { // File name like "image.png"
        var file: File? = null
        return try {
            file = File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + fileNameToSave
            )
            file.createNewFile()
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0, bos) // YOU can also save it in JPEG
            val bitmapdata = bos.toByteArray()
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
    ) {
        val videoUri = intent?.data
        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
//                videoUri?.let {
//                    UploadUtility(
//                        this,
//                        "http://frapi.hr-jo.com/api/token",
//                        this,
//                    ).uploadFile(it)
//                }
//                val value = Intent()
//                value.putExtra("isLive", true)
//                setResult(44, value)
//                finish()
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(
                    this, "Video recording cancelled.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this, "Failed to record video",
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


    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    override fun onDestroy() {
        super.onDestroy()
        mlLivenessDetectView!!.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mlLivenessDetectView!!.onPause()
    }

    override fun onResume() {
        super.onResume()
        HiltApplication.mContext = this
        mlLivenessDetectView!!.onResume()
    }


    override fun onSuccessCall(response: String?) {
        response?.let {
            val actionModels = ActionModel().parseArray(it)
            val userModel: UserModel = LongTermManager.getInstance().userModel
            val mainUserModel = UserModel(
                userModel.id,
                userModel.name,
                userModel.email,
                userModel.profile_photo_path,
                userModel.token,
                userModel.inSide,
                userModel.out,
                userModel.location,
                actionModels
            )
            LongTermManager.getInstance().userModel = mainUserModel
            val value = Intent()
            setResult(88, value)
            finish()
        }
    }

    override fun onErrorCall(error: VolleyError?) {
        finish()
    }

}