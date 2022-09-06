package com.example.myapplication.myapplication.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.BaseActivity
import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.data.local.sharedPref.LocaleHelper
import java.util.*
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : BaseActivity() {

    lateinit var localHelper:LocaleHelper

    fun clearAndStart(context: Context) {
        val intent = Intent(context, SplashActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
        (context as Activity).overridePendingTransition(0, 0)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        localHelper =LocaleHelper()
        handelAppLanguage()

        Handler().postDelayed({
            val userModel = LongTermManager.getInstance().userModel
            if (userModel!=null){
                val i = Intent(this@SplashActivity, NavigationActivity::class.java)
                startActivity(i)
                finish()
            }else{
                val i = Intent(this@SplashActivity, WelcomeScreen::class.java)
                startActivity(i)
                finish()
            }
        }, 2000)
    }

    private fun handelAppLanguage(){
        val locale = Locale( localHelper.getAppLocale(this@SplashActivity).name)
        Locale.setDefault(locale)

        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(
            config,
            this@SplashActivity.resources?.displayMetrics
        )
    }
}