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
    val id: Int? = 0,
    val name: String? = null,
    val email: String? = null,
    val profile_photo_path: String? = null,
    val token: String? = null,
    @SerializedName("in")
    @Expose
    val inSide: String? = null,
    val out: String? = null,
    val location: LocationModel? = null,
    @SerializedName("action")
    @Expose
    val actionModel: ArrayList<ActionModel?>? = ArrayList(),
) : Parcelable {



    @Throws(JSONException::class)
    fun parse(data: String): UserModel? {
        val gson: Gson = GsonBuilder().create()
        return gson.fromJson(data, UserModel::class.java)
    }



}