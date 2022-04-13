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
data class ActionModel(
    val name: String? = null,
    var active: Boolean? = false,
    @SerializedName("disable")
    @Expose
    val diable: Boolean? = false,
) : Parcelable {

    @Throws(JSONException::class)
    fun parseArray(data: String): ArrayList<ActionModel?>? {
        val gson: Gson = GsonBuilder().create()
        val listType = object :
            TypeToken<List<ActionModel?>?>() {}.type
        return gson.fromJson(data.toString(), listType)
    }
}