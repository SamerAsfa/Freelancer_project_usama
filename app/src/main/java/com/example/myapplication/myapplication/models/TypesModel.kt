package com.example.myapplication.myapplication.models

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONException

data class TypesModel(
    val id : Int? = 0,
    val name : String? = null,
){
    @Throws(JSONException::class)
    fun parseArray(data: String): ArrayList<TypesModel?>? {
        val gson: Gson = GsonBuilder().create()
        val listType = object :
            TypeToken<List<TypesModel?>?>() {}.type
        return gson.fromJson(data.toString(), listType)
    }
}