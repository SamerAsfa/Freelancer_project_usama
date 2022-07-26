package com.example.myapplication.myapplication.utils

import android.content.Context
import android.content.res.Configuration
import java.util.*

object Utils {

    fun changeLang(context: Context, lang: String?) {
        val myLocale = Locale(lang)
        Locale.setDefault(myLocale)
        val config = Configuration()
        config.locale = myLocale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }


}