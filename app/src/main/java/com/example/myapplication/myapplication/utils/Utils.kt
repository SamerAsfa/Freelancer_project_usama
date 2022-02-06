package com.example.myapplication.myapplication.utils

import android.content.Context
import android.provider.Settings

fun Context.getDeviceId() = Settings.Secure.getString(
    contentResolver,
    Settings.Secure.ANDROID_ID
).orEmpty()