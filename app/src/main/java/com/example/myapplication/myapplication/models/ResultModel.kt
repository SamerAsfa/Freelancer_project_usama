package com.example.myapplication.myapplication.models

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResultModel(val bitmap: Bitmap, val boolean: Boolean) : Parcelable