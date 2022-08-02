package com.example.myapplication.myapplication.models

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize
import org.json.JSONException

//@Parcelize
data class OrganizationUserDetailsModel(
    @SerializedName("name")
    var name: String? = null,// @Expose
) //: Parcelable {
//}