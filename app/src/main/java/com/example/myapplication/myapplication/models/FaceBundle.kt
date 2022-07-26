package com.example.myapplication.myapplication.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FaceBundle(
    var numberOfActions: Int? = 0,
    var uploadAsImage: Boolean? = false,
    var url: String? = ""
) : Parcelable
