package com.example.myapplication.myapplication.models

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize
import org.json.JSONException
import org.json.JSONObject

@Parcelize
data class NotificationModel(
    var id: String? = null,
    val title: String? = null,
    val message: String? = null,
    val body: String? = null,
    val not_type: String? = null,
    val click_action: String? = null,
    val date: String? = null,
    val created_at: String? = null,
) : Parcelable {
    @Throws(JSONException::class)
    fun parse(data: JSONObject): NotificationModel? {
        val gson: Gson = GsonBuilder().create()
        return gson.fromJson(data.toString(), NotificationModel::class.java)
    }

    @Throws(JSONException::class)
    fun parse(data: String): NotificationModel? {
        val gson: Gson = GsonBuilder().create()
        return gson.fromJson(data, NotificationModel::class.java)
    }

    @Throws(JSONException::class)
    fun parseArray(data: String): ArrayList<NotificationModel?>? {
        val gson: Gson = GsonBuilder().create()
        val listType = object :
            TypeToken<List<NotificationModel?>?>() {}.type
        return gson.fromJson(data.toString(), listType)
    }

}