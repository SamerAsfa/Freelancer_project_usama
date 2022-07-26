package com.example.myapplication.myapplication.models

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONException

data class LanguageModel(
    val id : Int? = 0,
    val name : String? = null,
    val flag : Int? = null,
    val flagName : String? = null,
){
    @Throws(JSONException::class)
    fun parseArray(data: String): ArrayList<LanguageModel?>? {
        val gson: Gson = GsonBuilder().create()
        val listType = object :
            TypeToken<List<LanguageModel?>?>() {}.type
        return gson.fromJson(data.toString(), listType)
    }
}