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
data class HistoryModel(
    val date: String? = null,
    val pin: String? = null,
    val pout: String? = null,
    val amount_in_min: String? = null,
    val start_date: String? = null,
    val end_date: String? = null,
    val type_id: Int? = null,
    val status: String? = null,
    @SerializedName("break")
    @Expose
    val breaks: Int? = 0,
) : Parcelable {
    @Throws(JSONException::class)
    fun parseArray(data: String): ArrayList<HistoryModel?>? {
        val gson: Gson = GsonBuilder().create()
        val listType = object :
            TypeToken<List<HistoryModel?>?>() {}.type
        return gson.fromJson(data.toString(), listType)
    }
}
