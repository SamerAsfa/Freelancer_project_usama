//package com.example.myapplication.myapplication.data
//
//import android.app.Activity
//import android.app.ProgressDialog
//import android.net.Uri
//import android.util.Log
//import android.webkit.MimeTypeMap
//import android.widget.Toast
//import com.example.myapplication.myapplication.base.ResponseApi
//import okhttp3.*
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.RequestBody.Companion.asRequestBody
//import java.io.File
//
//class UploadUtility(activity: Activity, serverURL: String, mainResponse: ResponseApi) {
//
//    var activity = activity;
//    var mainResponse = mainResponse;
//    var dialog: ProgressDialog? = null
//
//    //    var serverURL: String = "http://frapi.hr-jo.com/api/uploadVod"
//    var serverURL = serverURL
////    var serverUploadDirectoryPath: String = "https://handyopinion.com/tutorials/uploads/"
//    val client = OkHttpClient()
//
//    fun uploadFile(sourceFilePath: String, uploadedFileName: String? = null) {
//        uploadFile(File(sourceFilePath), uploadedFileName)
//    }
//
//    fun uploadFile(sourceFileUri: Uri, uploadedFileName: String? = null) {
//        val pathFromUri = URIPathHelper().getPath(activity, sourceFileUri)
//        uploadFile(File(pathFromUri), uploadedFileName)
//    }
//
//    fun uploadFile(sourceFile: File, uploadedFileName: String? = null) {
//        Thread {
//            val mimeType = getMimeType(sourceFile);
//            if (mimeType == null) {
//                Log.e("file error", "Not able to get mime type")
//                return@Thread
//            }
//            val fileName: String =
//                if (uploadedFileName == null) sourceFile.name else uploadedFileName
//            toggleProgressDialog(true)
//            try {
//                val requestBody: RequestBody =
//                    MultipartBody.Builder().setType(MultipartBody.FORM)
//                        .addFormDataPart(
//                            "attachment",
//                            fileName,
//                            sourceFile.asRequestBody(mimeType.toMediaTypeOrNull())
//                        )
//                        .build()
//
//                val request: Request = Request.Builder().url(serverURL).post(requestBody).build()
//
//                val response: Response = client.newCall(request).execute()
//                mainResponse.onSuccessCall(response)
////                if (response.isSuccessful) {
//////                    Log.d("File upload","success, path: $serverUploadDirectoryPath$fileName")
//////                    showToast("File uploaded successfully at $serverUploadDirectoryPath$fileName")
////                } else {
//////                    Log.e("File upload", "failed")
////                    showToast("File uploading failed")
////                }
//            } catch (ex: Exception) {
//                ex.printStackTrace()
//                Log.e("File upload", "failed")
////                showToast("File uploading failed")
//            }
//            toggleProgressDialog(false)
//        }.start()
//    }
//
//    // url = file path or whatever suitable URL you want.
//    fun getMimeType(file: File): String? {
//        var type: String? = null
//        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
//        if (extension != null) {
//            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
//        }
//        return type
//    }
//
//    fun showToast(message: String) {
//        activity.runOnUiThread {
//            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
//        }
//    }
//
//    fun toggleProgressDialog(show: Boolean) {
//        activity.runOnUiThread {
//            if (show) {
//                dialog = ProgressDialog.show(activity, "", "Uploading file...", true);
//            } else {
//                dialog?.dismiss();
//            }
//        }
//    }
//
//}
