package com.example.myapplication.myapplication.data

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.error.AuthFailureError
import com.android.volley.error.VolleyError
import com.android.volley.request.SimpleMultiPartRequest
import com.android.volley.request.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.myapplication.base.ResponseApi


class POSTMediasTask {
    var dialog: ProgressDialog? = null

    // //storage/emulated/0/usama.JPEG
    fun uploadMedia(context: Activity?, url: String?, filePath: String?, responseApi: ResponseApi) {
        context?.let { toggleProgressDialog(true, it) }
        val multiPartRequestWithParams = SimpleMultiPartRequest(
            Request.Method.POST, url,
            object : Response.Listener<String?> {
                override fun onResponse(response: String?) {
                    responseApi.onSuccessCall(response)
                    context?.let { toggleProgressDialog(false, it) }
                }

            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    val errors = VolleyError(error?.networkResponse?.data?.let { String(it) });
                    responseApi.onErrorCall(errors)
                    context?.let { toggleProgressDialog(false, it) }
                    Toast.makeText(
                        context, errors.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
        multiPartRequestWithParams.addFile("attachment", filePath)
//        multiPartRequestWithParams.addStringParam("param2", "SomeParamValue2")
        val queue = Volley.newRequestQueue(context)
        queue.add(multiPartRequestWithParams)
    }

    //    /storage/emulated/0/DCIM/Camera/VID_20220402_113309.mp4
    fun uploadMediaWithHeader(
        context: Activity?,
        url: String?,
        filePath: String?,
        responseApi: ResponseApi,
        header: MutableMap<String, String>
    ) {
        context?.let { toggleProgressDialog(true, it) }
        val multiPartRequestWithParams = SimpleMultiPartRequest(
            Request.Method.POST, url,
            object : Response.Listener<String?> {
                override fun onResponse(response: String?) {
                    responseApi.onSuccessCall(response)
                    context?.let { toggleProgressDialog(false, it) }
                }

            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    val errors = VolleyError(error?.networkResponse?.data?.let { String(it) });
                    responseApi.onErrorCall(errors)
                    context?.let { toggleProgressDialog(false, it) }
                    Toast.makeText(
                        context, errors.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
        )
        multiPartRequestWithParams.headers = header
//        multiPartRequestWithParams.addFile("attachment", "/${filePath}")
        multiPartRequestWithParams.addFile("attachment", filePath)
//        multiPartRequestWithParams.addStringParam("param2", "SomeParamValue2")
        val queue = Volley.newRequestQueue(context)
        queue.add(multiPartRequestWithParams)
    }

    fun post(
        context: Activity?,
        url: String?,
        params: MutableMap<String, String>,
        responseApi: ResponseApi
    ) {
        context?.let { toggleProgressDialog(true, it) }
        val queue = Volley.newRequestQueue(context)
        val sr: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener {
                responseApi.onSuccessCall(it)
                context?.let { toggleProgressDialog(false, it) }
            },
            Response.ErrorListener { error ->
                val errors = VolleyError(error?.networkResponse?.data?.let { String(it) });
                responseApi.onErrorCall(errors)
                context?.let { toggleProgressDialog(false, it) }
                Toast.makeText(
                    context, errors.message,
                    Toast.LENGTH_LONG
                ).show()
            }) {
            override fun getParams(): Map<String, String> {
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/x-www-form-urlencoded"
                return params
            }
        }
        queue.add(sr)
    }

    fun toggleProgressDialog(show: Boolean, activity: Activity) {
        activity.runOnUiThread {
            if (show) {
                dialog = ProgressDialog.show(activity, "", "Uploading file...", true);
            } else {
                dialog?.dismiss();
            }
        }
    }


}