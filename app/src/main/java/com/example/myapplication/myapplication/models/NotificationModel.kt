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
    var user_id: Int? = null,
    var not_type: String? = null,
    var item_id: String? = null,
    val title: String? = null,
    val body: String? = null,
    val is_read: Int? = 1,
    val created_at: String? = null,
    val updated_at: String? = null,

   // val message: String? = null,
/*    val click_action: String? = null,
    val date: String? = null,*/
    /*

            "created_at": null,
            "updated_at": "2022-08-09T19:00:24.000000Z"
     */


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