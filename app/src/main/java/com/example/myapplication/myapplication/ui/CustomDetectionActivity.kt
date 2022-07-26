package com.example.myapplication.myapplication.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
import com.example.myapplication.myapplication.data.BaseRequest
import com.example.myapplication.myapplication.data.BaseRequest.Companion.loginApi
import com.example.myapplication.myapplication.data.POSTMediasTask
import com.example.myapplication.myapplication.models.UserModel
import com.huawei.hms.mlsdk.livenessdetection.*
import kotlinx.android.synthetic.main.activity_custom_detection.*
import java.io.*
import java.util.*


class CustomDetectionActivity : AppCompatActivity(), ResponseApi {

    private var mlLivenessDetectView: MLLivenessDetectView? = null
    private var mPreviewContainer: FrameLayout? = null
    private var img_back: ImageView? = null
    val VIDEO_CAPTURE = 101



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
//                    value.putExtra("bitmap", result.bitmap)
                    val isLive = result.isLive
                    if (isLive) {
                        POSTMediasTask().uploadMedia(
                            this@CustomDetectionActivity,
                            loginApi,
                            bitmapToFile(result.bitmap),
//                            bitmapToFile(result.bitmap, "usama.JPEG")?.absolutePath,
                            this@CustomDetectionActivity
                        )
                    } else {
                        value.putExtra("isLive", isLive)
                        setResult(44, value)
                        finish()
                    }
//                    createImageFromBitmap(result.bitmap)
//                    value.putExtra("isLive", isLive)

//                    value.putExtra("bitmap", bitmap)
//                    setResult(44, value)
//                    finish()
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

    fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? { // File name like "image.png"
        //create a file to write bitmap data
        var file: File? = null
        return try {
            file = File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + fileNameToSave
            )
            file.createNewFile()

            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0, bos) // YOU can also save it in JPEG
            val bitmapdata = bos.toByteArray()

            //write the bytes in file
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

    private fun bitmapToFile(bitmap:Bitmap): String {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir("Images",Context.MODE_PRIVATE)
        file = File(file,"${UUID.randomUUID()}.jpg")
        try{
            val stream:OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
        }catch (e:IOException){
            e.printStackTrace()
        }

        // Return the saved bitmap uri
        return file.absolutePath
    }


    fun recordVideo() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(intent, VIDEO_CAPTURE)
    }

    fun createImageFromBitmap(bitmap: Bitmap): String? {
        var fileName: String? = "myImage" //no .png or .jpg needed
        try {
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val fo: FileOutputStream = openFileOutput(fileName, MODE_PRIVATE)
            fo.write(bytes.toByteArray())
            fo.close()
        } catch (e: Exception) {
            e.printStackTrace()
            fileName = null
        }
        return fileName
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
            val userModel = UserModel().parse(it)
            LongTermManager.getInstance().userModel = userModel
            NavigationActivity().clearAndStart(this)
        }
    }

    override fun onErrorCall(error: VolleyError?) {
        print("")
    }

}
