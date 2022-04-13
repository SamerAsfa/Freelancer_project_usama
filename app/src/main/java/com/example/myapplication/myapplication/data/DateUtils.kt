package com.example.myapplication.myapplication.data

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateUtils {

    fun getTime(timeStamp: Long): String {
        val sdf = SimpleDateFormat("hh:mm", Locale.CANADA)
        val netDate = Date(timeStamp)
        return sdf.format(netDate)
    }

    fun getDateFromTimeStamp(timeStamp: Long): String {
        val sdf = SimpleDateFormat("MMM yyyy", Locale.CANADA)
        val netDate = Date(timeStamp)
        return sdf.format(netDate)
    }

    fun getDateForApiFromTimeStamp(timeStamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM", Locale.CANADA)
        val netDate = Date(timeStamp)
        return sdf.format(netDate)
    }


    fun addOneMonth(amount : Int): Date {
        val cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, amount);
        return cal.time;
    }

    fun getDateForaAdapterFromTimeStamp(date: String?): String? {
        try {
            val format1 = SimpleDateFormat("yyyy-MM-dd", Locale.CANADA)
            val format = SimpleDateFormat("dd\nMMM", Locale.CANADA)
            val c = Calendar.getInstance()
            c.time = format1.parse(date)
            return format.format(c.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }




}