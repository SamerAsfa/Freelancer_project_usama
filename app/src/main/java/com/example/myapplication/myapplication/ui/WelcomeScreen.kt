package com.example.myapplication.myapplication.ui

import android.os.Bundle
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.BaseActivity
import com.example.myapplication.myapplication.models.LanguageModel
import com.example.myapplication.myapplication.ui.adapters.CustomDropDownAdapter
import com.example.myapplication.myapplication.ui.adapters.LanguageAdapter
import com.example.myapplication.myapplication.utils.Keys
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeScreen : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
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

                }
            })
        selectLanguageSpinner.adapter = languageAdapter
        nextButton.setOnClickListener {
            StartActivity.clearAndStart(this)
        }
    }


}