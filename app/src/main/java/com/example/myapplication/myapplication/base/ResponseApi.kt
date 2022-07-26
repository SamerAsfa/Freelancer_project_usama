package com.example.myapplication.myapplication.base

import com.android.volley.error.VolleyError
import okhttp3.Response


interface ResponseApi {

    fun onSuccessCall(response: String?)
    fun onErrorCall(error: VolleyError?)

}