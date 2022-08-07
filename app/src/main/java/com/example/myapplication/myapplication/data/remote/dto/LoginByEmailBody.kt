package com.example.myapplication.myapplication.data.remote.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class LoginByEmailBody (
    @SerializedName("email")
   // @Expose
    var email:String ,

    @SerializedName("password")
   // @Expose
    var password:String,

    @SerializedName("device_id")
   // @Expose
    var device_id:String,

    @SerializedName("fcm")
   // @Expose
    var fcm:String,

    @SerializedName("lang")
    //@Expose
    var lang:String
)