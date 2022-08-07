package com.example.myapplication.myapplication.models

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize
import org.json.JSONArray
import org.json.JSONException

@Parcelize
data class UserModel(
    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("email")
    @Expose
    var email: String? = null,

    @SerializedName("profile_photo_path")
    @Expose
    var profile_photo_path: String? = null,

    @SerializedName("token")
    @Expose
    var token: String? = null,

    @SerializedName("team")
    @Expose
    var team: String? = null,

    @SerializedName("role")
    @Expose
    var role: String? = null,


    @SerializedName("in")
    @Expose
    var inSide: String? = null,

    @SerializedName("out")
    @Expose
    var out: String? = null,

    @SerializedName("action")
    @Expose
    var action: List<ActionInsideUserModel>? = null,

    @SerializedName("lang")
    @Expose
    var lang: String? = null,

    @SerializedName("device_id")
    @Expose
    var device_id: String? = null,

    @SerializedName("mobile_number")
    @Expose
    var mobile_number: String? = null,

    @SerializedName("company_code")
    @Expose
    var company_code: String? = null,

    @SerializedName("location")
    @Expose
    var location: LocationModel? = null,

    @SerializedName("status")
    @Expose
    var status: Int? = null,

    @SerializedName("anti_spoof")
    @Expose
    var anti_spoof: Boolean = false,


    ) : Parcelable {



    @Throws(JSONException::class)
    fun parse(data: String): UserModel? {
        val gson: Gson = GsonBuilder().create()
        return gson.fromJson(data, UserModel::class.java)
    }



}