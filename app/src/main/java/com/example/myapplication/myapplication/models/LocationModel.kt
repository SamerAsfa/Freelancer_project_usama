package com.example.myapplication.myapplication.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationModel(
    @SerializedName("radius")
    @Expose
    var radius: String? = null,

    @SerializedName("lat")
    @Expose
    var lat: String? = "",

    @SerializedName("lng")
    @Expose
    var lng: String? = "",
) : Parcelable