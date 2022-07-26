package com.example.myapplication.myapplication.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationModel(
    val radius: Int? = 0,
    val lat: String? = "",
    val lng: String? = "",
) : Parcelable