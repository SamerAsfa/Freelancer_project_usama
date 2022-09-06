package com.example.myapplication.myapplication.ui

import android.content.res.Configuration
import android.os.Bundle
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.BaseActivity
import com.example.myapplication.myapplication.data.local.sharedPref.LocaleHelper
import com.example.myapplication.myapplication.models.LanguageModel
import com.example.myapplication.myapplication.ui.adapters.CustomDropDownAdapter
import com.example.myapplication.myapplication.ui.adapters.LanguageAdapter
import com.example.myapplication.myapplication.utils.Keys
import kotlinx.android.synthetic.main.activity_welcome.*
import java.util.*
import kotlin.collections.ArrayList

class WelcomeScreen : BaseActivity() {

    lateinit var localHelper: LocaleHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        localHelper= LocaleHelper()
        initViews()
    }

    fun initViews() {
        val arrayLanguages: ArrayList<LanguageModel> = ArrayList()
        arrayLanguages.add(
            LanguageModel(
                0, this.resources.getString(R.string.english), R.drawable.logo,
                Keys.EN
            )
        )
        arrayLanguages.add(
            LanguageModel(
                1,
                this.resources.getString(R.string.arabic),
                R.drawable.logo,
                Keys.AR
            )
        )
        val languageAdapter =
            CustomDropDownAdapter(this, arrayLanguages, object : LanguageAdapter.Clicks {
                override fun click(typesModel: LanguageModel, position: Int) {

                    val localEnum :LocaleHelper.LocalEnum= when(typesModel.id){
                        1-> LocaleHelper.LocalEnum.AR
                        2-> LocaleHelper.LocalEnum.EN
                        else -> {LocaleHelper.LocalEnum.EN}
                    }
                    localHelper.setAppLocale(this@WelcomeScreen,localEnum)
                    handelAppLanguage()

                    finish()
                    startActivity(intent)
                }
            })
        selectLanguageSpinner.adapter = languageAdapter
        nextButton.setOnClickListener {
            StartActivity.clearAndStart(this)
        }
    }

    private fun handelAppLanguage(){
        val locale = Locale( localHelper.getAppLocale(this@WelcomeScreen).name)
        Locale.setDefault(locale)

        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(
            config,
            this@WelcomeScreen.resources?.displayMetrics
        )
    }
}