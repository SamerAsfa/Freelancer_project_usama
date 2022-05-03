package com.example.myapplication.myapplication.ui.facedetect

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.YuvImage
import android.hardware.Camera
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicYuvToRGB
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import java.io.ByteArrayOutputStream
import java.io.IOException


class Preview(context: Context?) : ViewGroup(context), SurfaceHolder.Callback {
    private val TAG = "Preview"
    var mSurfaceView: SurfaceView
    var mHolder: SurfaceHolder
    var mPreviewSize: Camera.Size? = null
    var mSupportedPreviewSizes: List<Camera.Size>? = null
    var mCamera: Camera? = null
    fun setCamera(camera: Camera?) {
        mCamera = camera
        if (mCamera != null) {
            mSupportedPreviewSizes = mCamera!!.getParameters().getSupportedPreviewSizes()
            requestLayout()
        }
    }

    fun switchCamera(camera: Camera) {
        setCamera(camera)
        try {
            camera.setPreviewDisplay(mHolder)
        } catch (exception: IOException) {
            Log.e(TAG, "IOException caused by setPreviewDisplay()", exception)
        }
        val parameters: Camera.Parameters = camera.getParameters()
        mPreviewSize?.height?.let { mPreviewSize?.width?.let { it1 -> parameters.setPreviewSize(it1, it) } }
        requestLayout()
        camera.setParameters(parameters)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // We purposely disregard child measurements because act as a
        // wrapper to a SurfaceView that centers the camera preview instead
        // of stretching it.
        val width = resolveSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = resolveSize(suggestedMinimumHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)
        if (mSupportedPreviewSizes != null) {
            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (changed && childCount > 0) {
            val child: View = getChildAt(0)
            val width = r - l
            val height = b - t
            var previewWidth = width
            var previewHeight = height
            if (mPreviewSize != null) {
                previewWidth = mPreviewSize!!.width
                previewHeight = mPreviewSize!!.height
            }

            // Center the child SurfaceView within the parent.
            if (width * previewHeight > height * previewWidth) {
                val scaledChildWidth = previewWidth * height / previewHeight
                child.layout(
                    (width - scaledChildWidth) / 2, 0,
                    (width + scaledChildWidth) / 2, height
                )
            } else {
                val scaledChildHeight = previewHeight * width / previewWidth
                child.layout(
                    0, (height - scaledChildHeight) / 2,
                    width, (height + scaledChildHeight) / 2
                )
            }
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw.
        try {
            mCamera?.setPreviewDisplay(holder)
        } catch (exception: IOException) {
            Log.e(TAG, "IOException caused by setPreviewDisplay()", exception)
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // Surface will be destroyed when we return, so stop the preview.
        mCamera?.stopPreview()
    }

    private fun getOptimalPreviewSize(sizes: List<Camera.Size>?, w: Int, h: Int): Camera.Size? {
        val ASPECT_TOLERANCE = 0.1
        val targetRatio = w.toDouble() / h
        if (sizes == null) return null
        var optimalSize: Camera.Size? = null
        var minDiff :Int = Double.MAX_VALUE.toInt()

        // Try to find an size match aspect ratio and size
        for (size in sizes) {
            val ratio: Int = size.width / size.height
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue
            if (Math.abs(size.height - h) < minDiff) {
                optimalSize = size
                minDiff = Math.abs(size.height - h)
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff  = Double.MAX_VALUE.toInt()
            for (size in sizes) {
                if (Math.abs(size.height - h) < minDiff) {
                    optimalSize = size
                    minDiff = Math.abs(size.height - h)
                }
            }
        }
        return optimalSize
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, w: Int, h: Int) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        if (mCamera != null) {
            val parameters: Camera.Parameters = mCamera!!.getParameters()
            mPreviewSize?.width?.let { parameters.setPreviewSize(it, mPreviewSize!!.height) }
            requestLayout()
            mCamera!!.setParameters(parameters)
            mCamera!!.startPreview()


//            val w: Int = parameters.getPreviewSize().width
//            val h: Int = parameters.getPreviewSize().height
//            val format: Int = parameters.getPreviewFormat()
//            val image = YuvImage(data, format, w, h, null)
//
//            val out = ByteArrayOutputStream()
//            val area = Rect(0, 0, w, h)
//            image.compressToJpeg(area, 50, out)
//            val bm = BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.size())
//            ivCam.setImageBitmap(bm)

        }
    }



    init {
        mSurfaceView = SurfaceView(context)
        addView(mSurfaceView)

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = mSurfaceView.holder
        mHolder.addCallback(this)
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }
}