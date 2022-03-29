package com.example.myapplication.myapplication.ui

//import com.example.myapplication.myapplication.ui.StartActivity.Companion.customCallback
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.myapplication.R
import com.huawei.hms.mlsdk.livenessdetection.*
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.util.*


class CustomDetectionActivity : AppCompatActivity() {

    private var mlLivenessDetectView: MLLivenessDetectView? = null
    private var mPreviewContainer: FrameLayout? = null
    private var img_back: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_detection)
        mPreviewContainer = findViewById(R.id.surface_layout)
        img_back?.setOnClickListener(View.OnClickListener { finish() })
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
//                    createImageFromBitmap(result.bitmap)
                    value.putExtra("isLive", isLive)
//                    value.putExtra("bitmap", bitmap)
                    setResult(44, value)
                    finish()
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


    }

    fun createImageFromBitmap(bitmap: Bitmap): String? {
        var fileName: String? = "myImage" //no .png or .jpg needed
        try {
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val fo: FileOutputStream = openFileOutput(fileName, MODE_PRIVATE)
            fo.write(bytes.toByteArray())
            // remember close file output
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
        mlLivenessDetectView!!.onResume()
    }

}
