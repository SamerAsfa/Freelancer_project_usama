package com.example.myapplication.myapplication.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.myapplication.HiltApplication
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.LongTermManager
import kotlinx.android.synthetic.main.activity_navigation.*

class NavigationActivity : AppCompatActivity() {
    fun clearAndStart(context: Context) {
        val intent = Intent(context, NavigationActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
        (context as Activity).overridePendingTransition(0, 0)
    }

    override fun onResume() {
        super.onResume()
        HiltApplication.mContext = this
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        logOutButton.setOnClickListener {
            LongTermManager.getInstance().clearAll()
            SplashActivity().clearAndStart(this)
        }
    }


}