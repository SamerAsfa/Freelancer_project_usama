package com.example.myapplication.myapplication.ui.face2

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.YuvImage
import android.hardware.Camera
import android.hardware.Camera.PreviewCallback
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.ByteArrayOutputStream
import java.io.IOException


/** A basic Camera preview class  */
class CameraPreview(context: Context?, camera: Camera, imageBitmapListener: ImageBitmapListener) :
    SurfaceView(context),
    SurfaceHolder.Callback {
    private val mHolder: SurfaceHolder
    private val mCamera: Camera
    private var mmageBitmapListener: ImageBitmapListener? = null


    override fun surfaceCreated(holder: SurfaceHolder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setDisplayOrientation(90)
            mCamera.setPreviewDisplay(holder)
            mCamera.startPreview()


            mCamera.setPreviewCallback(PreviewCallback { _data, _camera ->
                val params = _camera.parameters
                val w = params.previewSize.width
                val h = params.previewSize.height
                val format = params.previewFormat
                val image = YuvImage(_data, format, w, h, null)
                val out = ByteArrayOutputStream()
                val area = Rect(0, 0, w, h)
                image.compressToJpeg(area, 50, out)
                val bm = BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.size())
                mmageBitmapListener?.showStreamingImagesBitmap(bm)
            })
        } catch (e: IOException) {
            print("")
        }
    }


    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, w: Int, h: Int) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        if (mHolder.surface == null) {
            // preview surface does not exist
            return
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview()
        } catch (e: Exception) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder)
            mCamera.startPreview()
        } catch (e: Exception) {
            Log.d(TAG, "Error starting camera preview: " + e.message)
        }
    }

    companion object {
        private const val TAG = "CameraPreview"
    }

    init {
        mCamera = camera
        mmageBitmapListener = imageBitmapListener
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = holder
        mHolder.addCallback(this)

        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }


}