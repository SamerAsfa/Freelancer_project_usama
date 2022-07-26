package com.example.myapplication.myapplication.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.database.Cursor
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.error.VolleyError
import com.example.myapplication.myapplication.HiltApplication
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.base.ResponseApi
import com.example.myapplication.myapplication.data.BaseRequest.Companion.uploadVodApi
import com.example.myapplication.myapplication.data.POSTMediasTask
import com.example.myapplication.myapplication.models.UserModel
import com.huawei.hms.mlsdk.livenessdetection.*
import kotlinx.android.synthetic.main.activity_custom_detection.*
import kotlinx.android.synthetic.main.activity_custom_vid_detection.*
import java.io.*
import java.net.URISyntaxException


class CustomWithVideo : AppCompatActivity(), ResponseApi {

    private var mlLivenessDetectView: MLLivenessDetectView? = null
    private var mPreviewContainer: FrameLayout? = null
    private var img_back: ImageView? = null
    val VIDEO_CAPTURE = 101
    var userModel: UserModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_vid_detection)
        mPreviewContainer = findViewById(R.id.surface_layoutVid)
        val intent = intent
        userModel = intent.getParcelableExtra("user")
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
                        recordVideo()
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
        regEmailButtonVid.visibility = View.VISIBLE
    }


    fun recordVideo() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
            intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
            intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
        } else {
            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        }
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 1);
        startActivityForResult(intent, VIDEO_CAPTURE)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
    ) {
        val videoUri = intent?.data
        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                val filePath = videoUri?.let { getFilePath(this@CustomWithVideo, it) }
                var maps: MutableMap<String, String> = HashMap()
                maps.put("Authorization", "Bearer ${userModel?.token}")
                maps.put("Accept", "application/json")
                POSTMediasTask().uploadMediaWithHeader(
                    this@CustomWithVideo,
                    uploadVodApi,
                    filePath,
                    this@CustomWithVideo, maps
                )
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

//    private fun saveVideoToInternalStorage(filePath: String): File? {
//        var newfile: File? = null
//        try {
//            val currentFile = File(filePath)
//            val fileName = currentFile.name
//            val cw = ContextWrapper(applicationContext)
//            val directory = cw.getDir("videoDir", MODE_PRIVATE)
//            newfile = File(directory, fileName)
//            if (currentFile.exists()) {
//                val `in`: InputStream = FileInputStream(currentFile)
//                val out: OutputStream = FileOutputStream(newfile)
//
//                // Copy the bits from instream to outstream
//                val buf = ByteArray(1024)
//                var len: Int
//                while (`in`.read(buf).also { len = it } > 0) {
//                    out.write(buf, 0, len)
//                }
//                `in`.close()
//                out.close()
//                Log.v("", "Video file saved successfully.")
//            } else {
//                Log.v("", "Video saving failed. Source file missing.")
//            }
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
//        return newfile
//    }
//
//    private fun loadVideoFromInternalStorage(filePath: String): String {
//        return Environment.getExternalStorageDirectory().toString() + filePath
//    }


    //    fun bitmapToFile(fileNameToSave: String): File? { // File name like "image.png"
//        //create a file to write bitmap data
//        var file: File? = null
//        return try {
//            file = File(
//                Environment.getExternalStorageDirectory()
//                    .toString() + File.separator + fileNameToSave
//            )
//            file.createNewFile()
//
////            //Convert bitmap to byte array
////            val bos = ByteArrayOutputStream()
////            bitmap.compress(Bitmap.CompressFormat.JPEG, 0, bos) // YOU can also save it in JPEG
////            val bitmapdata = bos.toByteArray()
//
//            //write the bytes in file
//            val fos = FileOutputStream(file)
////            fos.write(bitmapdata)
//            fos.flush()
//            fos.close()
//            file
//        } catch (e: Exception) {
//            e.printStackTrace()
//            file // it will return null
//        }
//    }
//
    @SuppressLint("NewApi")
    @Throws(URISyntaxException::class)
    fun getFilePath(context: Context, uri: Uri): String? {
        var uri = uri
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(
                context.applicationContext,
                uri
            )
        ) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                uri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("image" == type) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                selection = "_id=?"
                selectionArgs = arrayOf(
                    split[1]
                )
            }
        }
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            if (isGooglePhotosUri(uri)) {
                return uri.lastPathSegment
            }
            val projection = arrayOf(
                MediaStore.Images.Media.DATA
            )
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver
                    .query(uri, projection, selection, selectionArgs, null)
                val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }
//
//
//    fun getRealPathFromURI(context: Context, contentUri: Uri?): String? {
//        var cursor: Cursor? = null
//        return try {
//            val proj = arrayOf(MediaStore.Images.Media.DATA)
//            cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
//            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//            cursor.moveToFirst()
//            cursor.getString(column_index)
//        } finally {
//            cursor?.close()
//        }
//    }

    //    fun getRealPathFromURI(contentUri: Uri?): String? {
//        val proj = arrayOf(MediaStore.Video.Media.DATA)
//        val cursor: Cursor = managedQuery(contentUri, proj, null, null, null)
//        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
//        cursor.moveToFirst()
//        return cursor.getString(column_index)
//    }
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
            LongTermManager.getInstance().userModel = userModel
            NavigationActivity().clearAndStart(this)
        }
    }

    override fun onErrorCall(error: VolleyError?) {
        print("")
    }

}
