package com.example.myapplication.myapplication.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.error.VolleyError
import com.example.myapplication.myapplication.HiltApplication
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.BaseActivity
import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.base.ResponseApi
import com.example.myapplication.myapplication.data.BaseRequest
import com.example.myapplication.myapplication.data.BaseRequest.Companion.loginApi
import com.example.myapplication.myapplication.data.POSTMediasTask
import com.example.myapplication.myapplication.models.FaceBundle
import com.example.myapplication.myapplication.models.UserModel
import com.example.myapplication.myapplication.ui.face2.FaceDetectionActivity
import com.huawei.hms.mlsdk.livenessdetection.*
import kotlinx.android.synthetic.main.activity_custom_detection.*
import java.io.*
import java.util.*


class CustomDetectionActivity : BaseActivity(), ResponseApi {

    private var mlLivenessDetectView: MLLivenessDetectView? = null
    private var mPreviewContainer: FrameLayout? = null
    private var img_back: ImageView? = null
    val VIDEO_CAPTURE = 101
    lateinit var bitMapToSave : Bitmap

    var userModelToSave: UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_detection)
        mPreviewContainer = findViewById(R.id.surface_layout)
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
                        bitMapToSave = result.bitmap
                        startRecorder()
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



    fun startRecorder() {
        ActivityCompat.startActivityForResult(this, FaceDetectionActivity.startActivity(this,
            FaceBundle(numberOfActions = 1)
        ), VIDEO_CAPTURE,null)
    }


    fun bitmapToFile(bitmap: Bitmap): File? {
        var file: File? = null
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
            if (file.exists()) {
                file.mkdirs()
            }
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file?.absoluteFile
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
    ) {
        val filePath  = (intent?.extras?.get("result") as String)
        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                if (!filePath.isNullOrEmpty()) {
                    toggleProgressDialog(show = true,this,this.resources.getString(R.string.loading))
                    POSTMediasTask().uploadMedia(
                        this@CustomDetectionActivity,
                        loginApi,
                        bitmapToFile(bitMapToSave).toString(),
                        this@CustomDetectionActivity
                    )
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(
                    this, "Not correct detection",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this, "Not correct detection",
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
        toggleProgressDialog(show = false,this,this.resources.getString(R.string.loading))
        response?.let {
            userModelToSave = UserModel().parse(it)
            LongTermManager.getInstance().userModel = userModelToSave
            NavigationActivity().clearAndStart(this)
        }
    }


    override fun onErrorCall(error: VolleyError?) {
        toggleProgressDialog(show = false,this,this.resources.getString(R.string.loading))
        showDialogOneButtonsCustom("Error", error?.message.toString(), "Ok") { dialog ->
            dialog.dismiss()
        }
    }

}
