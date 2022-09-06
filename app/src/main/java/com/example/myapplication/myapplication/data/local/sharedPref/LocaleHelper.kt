package com.example.myapplication.myapplication.data.local.sharedPref

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.preference.PreferenceManager
import java.util.*


class LocaleHelper {

    private val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"

    fun setAppLocale(context: Context, language: LocalEnum) {
        val language: String =
            when (language) {
                LocalEnum.AR -> "ar"
                LocalEnum.EN -> "en"
                else -> {
                    "en"
                }
            }

        setLocal(context, language)
    }

    fun getAppLocale(context: Context): LocalEnum {
     return   when (getLocal(context)) {
            "ar" -> LocalEnum.AR
            "en" -> LocalEnum.EN
            else -> {
                LocalEnum.EN
            }
        }
    }

    private fun setLocal(context: Context, language: String) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(SELECTED_LANGUAGE, language)
        editor.apply()
    }

    private fun getLocal(context: Context): String {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(SELECTED_LANGUAGE, "en").toString()
    }

    enum class LocalEnum(locale: String) {
        AR("ar"), EN("en")
    }
}