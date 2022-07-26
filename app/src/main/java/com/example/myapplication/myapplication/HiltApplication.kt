package com.example.myapplication.myapplication

import android.app.Application
import android.content.Context
import com.example.myapplication.myapplication.base.LongTermManager
//import dagger.hilt.android.HiltAndroidApp

//@HiltAndroidApp
class HiltApplication : Application(){

    companion object {
        var mContext: Context? = null
        fun getContext(): Context? {
            return mContext
        }
    }


    override fun onCreate() {
        super.onCreate()
         mContext = this
        LongTermManager.getInstance().setContext(mContext)
    }
}