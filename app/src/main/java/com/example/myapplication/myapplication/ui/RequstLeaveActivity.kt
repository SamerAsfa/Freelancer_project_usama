package com.example.myapplication.myapplication.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.error.VolleyError
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.BaseActivity
import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.base.ResponseApi
import com.example.myapplication.myapplication.data.BaseRequest
import com.example.myapplication.myapplication.data.DateUtils
import com.example.myapplication.myapplication.data.POSTMediasTask
import com.example.myapplication.myapplication.models.TypesModel
import com.example.myapplication.myapplication.models.UserModel
import com.example.myapplication.myapplication.ui.adapters.TypeAdapter
import kotlinx.android.synthetic.main.activity_requst_leave.*
import java.io.FileNotFoundException
import java.net.URISyntaxException
import java.util.*


class RequstLeaveActivity : BaseActivity() {
    var userModel: UserModel? = null
    var filePath: String? = null
    var leaveDate: DatePicker? = null
    var fromDate: TimePicker? = null
    var toDate: TimePicker? = null
    var typeModel: TypesModel? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requst_leave)
        userModel = LongTermManager.getInstance().userModel
        leaveDateTextView.setOnClickListener {
            try {
                val calendar = Calendar.getInstance()
                val dpd = DatePickerDialog(
                    this@RequstLeaveActivity,
                    { datePicker, i, i1, i2 ->
                        this@RequstLeaveActivity.leaveDate = datePicker
                        leaveDateTextView.text = String.format(
                            Locale.CANADA,
                            "%s,%s%s",
                            DateUtils().dayDate(datePicker.dayOfMonth),
                            DateUtils().monthDate(datePicker.month + 1),
                            datePicker.dayOfMonth
                        )
                    }, calendar[Calendar.YEAR], calendar[Calendar.MONTH],
                    calendar[Calendar.DATE]
                )
                dpd.show()
            } catch (ex: Exception) {
                ex.message
            }
        }
//        showSuccessLeaveDialog { dialog ->
//            dialog.dismiss()
//            NavigationActivity().clearAndStart(this@RequstLeaveActivity)
//        }
        fromSelectTextView.setOnClickListener {
            try {
                val calendar = Calendar.getInstance()
                val dpd = TimePickerDialog(
                    this@RequstLeaveActivity,
                    { datePicker, HOUR, MINUTE ->
                        this@RequstLeaveActivity.fromDate = datePicker
                        fromSelectTextView.text = String.format(
                            Locale.CANADA,
                            "%s:%s",
                            HOUR,
                            MINUTE,
                        )
                    }, calendar[Calendar.HOUR], calendar[Calendar.MINUTE],
                    true
                )
                dpd.show()
            } catch (ex: Exception) {
                ex.message
            }

        }

        toSelectTextView.setOnClickListener {
            try {
                val calendar = Calendar.getInstance()
                val dpd = TimePickerDialog(
                    this@RequstLeaveActivity,
                    { datePicker, HOUR, MINUTE ->
                        this@RequstLeaveActivity.toDate = datePicker
                        toSelectTextView.text = String.format(
                            Locale.CANADA,
                            "%s:%s",
                            HOUR,
                            MINUTE,
                        )
                    }, calendar[Calendar.HOUR], calendar[Calendar.MINUTE],
                    true
                )
                dpd.show()
            } catch (ex: Exception) {
                ex.message
            }

        }

        leaveTypeSelectTextView.setOnClickListener {
            POSTMediasTask().get(
                this,
                BaseRequest.leaveTypeApi,
                "",
                object : ResponseApi {
                    override fun onSuccessCall(response: String?) {
                        val arrayHistory: ArrayList<TypesModel?>? = response?.let {
                            TypesModel().parseArray(
                                it
                            )
                        }
                        shopperList(arrayHistory)
                    }

                    override fun onErrorCall(error: VolleyError?) {
                        showDialogOneButtonsCustom(
                            "Error",
                            error?.message.toString(),
                            "Ok"
                        ) { dialog ->
                            dialog.dismiss()
                        }
                    }
                }
            )
        }

        chooseFileSelectTextView.setOnClickListener {
            getImage()
        }

        sendButton.setOnClickListener {
            var v = leaveDate?.month
            if (leaveDate != null) {
                if (fromDate != null) {
                    if (toDate != null) {
                        if (typeModel != null) {
                            val fromTime = getDate(fromDate!!)
                            val toTime = getDate(toDate!!)
                            requstLeave(fromTime, toTime)
                        } else {
                            Toast.makeText(
                                this@RequstLeaveActivity,
                                "Select leave Type",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Toast.makeText(this@RequstLeaveActivity, "Add to Time", Toast.LENGTH_LONG)
                            .show()
                    }

                } else {
                    Toast.makeText(this@RequstLeaveActivity, "Add From Time", Toast.LENGTH_LONG)
                        .show()
                }

            } else {
                Toast.makeText(this@RequstLeaveActivity, "Add Leave Date", Toast.LENGTH_LONG)
                    .show()
            }

        }

    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun getDate(timePicker: TimePicker): String {
        return String.format(
            Locale.CANADA,
            "%s-%s-%s %s:%s",
            leaveDate?.dayOfMonth,
            DateUtils().monthDateApi(leaveDate?.month?.plus(1)),
            DateUtils().dayDateApi(leaveDate?.dayOfMonth),
            timePicker.hour,
            timePicker.minute,
        )
    }

    fun requstLeave(startDate: String, endDate: String) {
        toggleProgressDialog(show = true, this, this.resources.getString(R.string.loading))
        var maps: MutableMap<String, String> = HashMap()
        maps.put("Authorization", "Bearer ${userModel?.token}")
        maps.put("Accept", "application/json")
        POSTMediasTask().uploadMediaWithHeaderBody(
            this@RequstLeaveActivity,
            BaseRequest.leaveApi,
            filePath,
            responseApi = object : ResponseApi {
                override fun onSuccessCall(response: String?) {
                    toggleProgressDialog(
                        show = false,
                        this@RequstLeaveActivity,
                        this@RequstLeaveActivity.resources.getString(R.string.loading)
                    )
                    showSuccessLeaveDialog { dialog ->
                        dialog.dismiss()
                        NavigationActivity().clearAndStart(this@RequstLeaveActivity)
                    }
                }

                override fun onErrorCall(error: VolleyError?) {
                    toggleProgressDialog(
                        show = false,
                        this@RequstLeaveActivity,
                        this@RequstLeaveActivity.resources.getString(R.string.loading)
                    )
                    showDialogOneButtonsCustom("Error", error?.message.toString(), "Ok") { dialog ->
                        dialog.dismiss()
                    }
                }
            }, maps,
            startDate, endDate, typeModel?.id.toString()
        )
    }


    fun getImage() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, 99)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            try {
                val imageUri: Uri? = data?.data
                filePath = imageUri?.let { getFilePath(this@RequstLeaveActivity, it) }
                uploadDocumentSelectTextView.text = filePath
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(this@RequstLeaveActivity, "Something went wrong", Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            Toast.makeText(this@RequstLeaveActivity, "You haven't picked Image", Toast.LENGTH_LONG)
                .show()
        }
    }


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

    fun shopperList(shopperArrayList: ArrayList<TypesModel?>?) {
        if (shopperArrayList != null && shopperArrayList.size > 0) {
            val dialogAndroidAppCus = Dialog(this)
            dialogAndroidAppCus.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogAndroidAppCus.setContentView(R.layout.dialog_types)
            val recyclerView =
                dialogAndroidAppCus.findViewById<View>(R.id.recyclerView) as RecyclerView
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val realtyStringAdapter = TypeAdapter(shopperArrayList, object : TypeAdapter.Clicks {
                override fun click(typesModel: TypesModel, position: Int) {
                    dialogAndroidAppCus.dismiss()
                    typeModel = typesModel
                    leaveTypeSelectTextView.text = typesModel.name
                }
            })
            recyclerView.adapter = realtyStringAdapter
            dialogAndroidAppCus.setCancelable(true)
            dialogAndroidAppCus.show()
        }
    }


}