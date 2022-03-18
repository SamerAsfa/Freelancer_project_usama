package com.example.myapplication.myapplication.ui

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.myapplication.ui.StartActivity.Companion.customCallback
import com.huawei.hms.mlsdk.livenessdetection.*
import java.util.*
import kotlin.concurrent.schedule
import com.example.myapplication.myapplication.R


class CustomDetectionActivity : AppCompatActivity() {

    private var mlLivenessDetectView: MLLivenessDetectView? = null
    private var mPreviewContainer: FrameLayout? = null
    private var img_back: ImageView? = null
//    private val surfaceHolder: SurfaceHolder? = null
//    private val surfaceView: SurfaceView? = null
//    var mrec = MediaRecorder()
//    var video: File? = null
//    private val mCamera: Camera? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_detection)

        mPreviewContainer = findViewById(R.id.surface_layout)
        img_back?.setOnClickListener(View.OnClickListener { finish() })
        // Obtain MLLivenessDetectView
        val outMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels

        mlLivenessDetectView = MLLivenessDetectView.Builder()
            .setContext(this)
            .setOptions(MLLivenessDetectView.DETECT_MASK) // set Rect of face frame relative to surface in layout
            .setFaceFrameRect(Rect(0, 0, widthPixels, dip2px(this, 480f)))
            .setDetectCallback(object : OnMLLivenessDetectCallback {
                override fun onCompleted(result: MLLivenessCaptureResult) {
                    customCallback.onSuccess(result)
//                    mainCallback.onSuccess(result)
                    finish()
                }

                override fun onError(error: Int) {
                    customCallback.onFailure(error)
//                    mainCallback.onFailure(error)
                    finish()
                }

                override fun onInfo(infoCode: Int, bundle: Bundle) {
                    print("")
                }

                override fun onStateChange(state: Int, bundle: Bundle) {
                    print("")
                }
            }).build()

        mPreviewContainer!!.addView(mlLivenessDetectView)
        mlLivenessDetectView!!.onCreate(savedInstanceState)
        Timer("SettingUp", false).schedule(5000) {

//            mlLivenessDetectView?.let { mainView ->
//                var bitmap: Bitmap? = getBitmapFromView(
//                    mainView.rootView,
//                    mainView.rootView.height,
//                    mainView.rootView.width
//                )
//                val stream = ByteArrayOutputStream()
//                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)

//                Glide.with(this@CustomDetectionActivity)
//                    .load(stream.toByteArray())
//                    .asBitmap()
//                    .into(imageViewTest)
                print("")
//            }
        }

    }

//    companion object {
//        val mainCallback: MLLivenessCapture.Callback = object : MLLivenessCapture.Callback {
//            override fun onSuccess(result: MLLivenessCaptureResult) {
//                print("")
////                mTextResult!!.text = result.toString()
////                mTextResult!!.setBackgroundResource(if (result.isLive) R.drawable.bg_blue else R.drawable.bg_red)
////                mImageResult?.setImageBitmap(result.bitmap)
//            }
//            override fun onFailure(errorCode: Int) {
////                mTextResult!!.text = "errorCode:$errorCode"
//            }
//        }
//    }


//    fun getBitmapFromView(view: View, height: Int, width: Int): Bitmap? {
//        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bitmap)
//        val bgDrawable = view.background
//        if (bgDrawable != null) bgDrawable.draw(canvas) else canvas.drawColor(Color.WHITE)
//        view.draw(canvas)
//        return bitmap
//    }

//    private fun createBitmapFromLayout(tv: View): Bitmap? {
//        val spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//        tv.measure(spec, spec)
//        tv.layout(0, 0, tv.measuredWidth, tv.measuredHeight)
//        val b = Bitmap.createBitmap(
//            tv.measuredWidth, tv.measuredHeight,
//            Bitmap.Config.ARGB_8888
//        )
//        val c = Canvas(b)
//        c.translate((-tv.scrollX).toFloat(), (-tv.scrollY).toFloat())
//        tv.draw(c)
//        return b
//    }

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


        print('d')

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
