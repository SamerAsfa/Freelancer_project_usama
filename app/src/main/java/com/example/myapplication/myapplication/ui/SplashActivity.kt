package com.example.myapplication.myapplication.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.LongTermManager


class SplashActivity : AppCompatActivity() {

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
        Handler().postDelayed({
            val userModel = LongTermManager.getInstance().userModel
            if (userModel!=null){
                val i = Intent(this@SplashActivity, NavigationActivity::class.java)
                startActivity(i)
                finish()
            }else{
                val i = Intent(this@SplashActivity, StartActivity::class.java)
                startActivity(i)
                finish()
            }
        }, 2000)
    }

}