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
data class ActionModel(
    @SerializedName("in")
    @Expose
    var inSide: String? = null,
    @SerializedName("out")
    @Expose
    var out: String? = null,
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
    @SerializedName("action")
    @Expose
    var action: ArrayList<ActionModel>? = ArrayList(),
    var isLargeView: Boolean? = false,
) : Parcelable {


    @Throws(JSONException::class)
    fun parse(data: String): ActionModel? {
        val gson: Gson = GsonBuilder().create()
        return gson.fromJson(data, ActionModel::class.java)
    }

 
    @Throws(JSONException::class)
    fun parseArray(data: String): ArrayList<ActionModel?>? {
        val gson: Gson = GsonBuilder().create()
        val listType = object :
            TypeToken<List<ActionModel?>?>() {}.type
        return gson.fromJson(data.toString(), listType)
    }

    enum class ViewState {
        LARGE,
        DEFAULT,

    }
}