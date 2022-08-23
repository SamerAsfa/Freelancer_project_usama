package com.example.myapplication.myapplication.common

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

//@RequiresApi(Build.VERSION_CODES.O)
//val dateFormatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
//val timeFormatter = SimpleDateFormat("hh:mm:ss")



fun String.toDate():String {
    return this.substring(0, this.indexOf('T'))// string must be like 2022-08-22T19:54:0000000Z
}

fun String.toTime():String{
    return this.split(" ")[1] // string must be like 2022-08-22 19:54:00
}