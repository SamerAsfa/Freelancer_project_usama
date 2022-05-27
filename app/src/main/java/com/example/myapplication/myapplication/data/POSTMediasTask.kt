package com.example.myapplication.myapplication.data

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.error.AuthFailureError
import com.android.volley.error.VolleyError
import com.android.volley.request.JsonObjectRequest
import com.android.volley.request.SimpleMultiPartRequest
import com.android.volley.request.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.base.ResponseApi
import com.example.myapplication.myapplication.models.UserModel
import java.io.File


class POSTMediasTask {

    fun uploadMedia(context: Activity?, url: String?, filePath: String?, responseApi: ResponseApi) {

        val multiPartRequestWithParams = SimpleMultiPartRequest(
            Request.Method.POST, url,
            object : Response.Listener<String?> {
                override fun onResponse(response: String?) {
                    responseApi.onSuccessCall(response)

                }

            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    val errors = VolleyError(error?.networkResponse?.data?.let { String(it) });
                    responseApi.onErrorCall(errors)
                }

            })
        multiPartRequestWithParams.addFile("attachment", filePath)
        multiPartRequestWithParams.addStringParam(
            "fcm",
            LongTermManager.getInstance().notificationsToken
        )
        val queue = Volley.newRequestQueue(context)
        queue.add(multiPartRequestWithParams)
    }
/////data/user/0/com.test.face.usama/app_Images/92c6fabf-c582-4be4-bd84-12bda5bb18b2.jpg
    //    /storage/emulated/0/DCIM/Camera/VID_20220402_113309.mp4
    fun uploadMediaWithHeader(
    context: Activity?,
    url: String?,
    filePath: String,
    responseApi: ResponseApi,
    header: MutableMap<String, String>
    ) {
        val multiPartRequestWithParams = SimpleMultiPartRequest(
            Request.Method.POST, url,
            object : Response.Listener<String?> {
                override fun onResponse(response: String?) {
                    responseApi.onSuccessCall(response)
                }

            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    val errors = VolleyError(error?.networkResponse?.data?.let { String(it) });
                    responseApi.onErrorCall(errors)
                }

            }
        )
        multiPartRequestWithParams.headers = header
        multiPartRequestWithParams.addFile("attachment", filePath.toString())
        val queue = Volley.newRequestQueue(context)
        queue.add(multiPartRequestWithParams)
    }

    fun uploadMediaWithHeaderBody(
        context: Activity?,
        url: String?,
        filePath: String?,
        responseApi: ResponseApi,
        header: MutableMap<String, String>,
        startDate: String,
        end_date: String,
        type_id: String,
    ) {
        val multiPartRequestWithParams = SimpleMultiPartRequest(
            Request.Method.POST, url,
            object : Response.Listener<String?> {
                override fun onResponse(response: String?) {
                    responseApi.onSuccessCall(response)
                }

            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    val errors = VolleyError(error?.networkResponse?.data?.let { String(it) });
                    responseApi.onErrorCall(errors)
                }

            }
        )
        multiPartRequestWithParams.headers = header
        multiPartRequestWithParams.addFile("attachment", filePath)
        multiPartRequestWithParams.addStringParam("start_date", startDate)
        multiPartRequestWithParams.addStringParam("end_date", end_date)
        multiPartRequestWithParams.addStringParam("type_id", type_id)
        val queue = Volley.newRequestQueue(context)
        queue.add(multiPartRequestWithParams)
    }

    fun postWithHeader(
        context: Activity?,
        url: String?,
        params: MutableMap<String, String>,
        responseApi: ResponseApi
    ) {
        val userModel: UserModel = LongTermManager.getInstance().userModel

        val queue = Volley.newRequestQueue(context)
        val sr: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener {
                responseApi.onSuccessCall(it)

            },
            Response.ErrorListener { error ->
                val errors = VolleyError(error?.networkResponse?.data?.let { String(it) });
                responseApi.onErrorCall(errors)


            }) {
            override fun getParams(): Map<String, String> {
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/x-www-form-urlencoded"
                params["Authorization"] = "Bearer ${userModel.token}"
                params["Accept"] = "application/json"
                return params
            }
        }
        queue.add(sr)
    }

    fun post(
        context: Activity?,
        url: String?,
        params: MutableMap<String, String>,
        responseApi: ResponseApi
    ) {
        val queue = Volley.newRequestQueue(context)
        val sr: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener {
                responseApi.onSuccessCall(it)
            },
            Response.ErrorListener { error ->
                val errors = VolleyError(error?.networkResponse?.data?.let { String(it) });
                responseApi.onErrorCall(errors)
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


    fun get(
        context: Activity?,
        url: String?,
        concat: String? = "",
        responseApi: ResponseApi
    ) {
        val userModel: UserModel = LongTermManager.getInstance().userModel
        val queue = Volley.newRequestQueue(context)
        val sr: StringRequest = object : StringRequest(
            Method.GET, "${url}${concat}",
            Response.Listener {
                responseApi.onSuccessCall(it)
            },
            Response.ErrorListener { error ->
                val errors = VolleyError(error?.networkResponse?.data?.let { String(it) });
                responseApi.onErrorCall(errors)
                Toast.makeText(
                    context, errors.message,
                    Toast.LENGTH_LONG
                ).show()
            }) {
        }
        val maps: MutableMap<String, String> = HashMap()
        maps.put("Authorization", "Bearer ${userModel.token}")
//        maps.put("Authorization", "Bearer 142|ODzNOzZ1yhWkzEOob6URoJajOHVc7opOTpKQe8Wl")
        maps.put("Accept", "application/json")
        maps.put("Content-Type", "application/x-www-form-urlencoded")
        sr.headers = maps
        queue.add(sr)
    }


}