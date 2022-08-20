package com.example.myapplication.myapplication.models

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize
import org.json.JSONException

@Parcelize
data class MyLeaveHistoryModel(
    val id: Int? = null,
    val user_id: Int? = null,
    val date: String? = null,
    val pin: String? = null,
    val pout: String? = null,
    val amount_in_min: String? = null,
    val start_date: String? = null,
    val end_date: String? = null,
    val type_id: Int? = null,
    val status: String? = null,
    val attachment: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    @SerializedName("break")
    @Expose
    val breaks: Int? = 0,
) : Parcelable {
    @Throws(JSONException::class)
    fun parseArray(data: String): ArrayList<MyLeaveHistoryModel?>? {
        val gson: Gson = GsonBuilder().create()
        val listType = object :
            TypeToken<List<MyLeaveHistoryModel?>?>() {}.type
        return gson.fromJson(data.toString(), listType)
    }
}
