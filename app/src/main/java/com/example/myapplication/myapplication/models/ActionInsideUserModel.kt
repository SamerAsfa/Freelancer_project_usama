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
data class ActionInsideUserModel(
    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("key")
    @Expose
    var key: String? = null,

    @SerializedName("active")
    @Expose
    var active: Boolean? = false,

    @SerializedName("disable")
    @Expose
    var disable: Boolean? = false,
) : Parcelable {


    @Throws(JSONException::class)
    fun parse(data: String): ActionInsideUserModel? {
        val gson: Gson = GsonBuilder().create()
        return gson.fromJson(data, ActionInsideUserModel::class.java)
    }

 
    @Throws(JSONException::class)
    fun parseArray(data: String): ArrayList<ActionInsideUserModel?>? {
        val gson: Gson = GsonBuilder().create()
        val listType = object :
            TypeToken<List<ActionInsideUserModel?>?>() {}.type
        return gson.fromJson(data.toString(), listType)
    }

    enum class ViewState {
        LARGE,
        DEFAULT,

    }
}