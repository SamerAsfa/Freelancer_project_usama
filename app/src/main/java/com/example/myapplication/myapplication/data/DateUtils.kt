package com.example.myapplication.myapplication.data

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateUtils {

    fun getTime(timeStamp: Long): String {
        val sdf = SimpleDateFormat("hh:mm", Locale.CANADA)
        val netDate = Date(timeStamp * 1000L)
        return sdf.format(netDate)
    }

    fun getDateFromTimeStamp(timeStamp: Long): String {
        val sdf = SimpleDateFormat("MMM yyyy", Locale.CANADA)
        val netDate = Date(timeStamp)
        return sdf.format(netDate)
    }

    fun getDateFromTimeStampNotification(timeStamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.CANADA)
        val netDate = Date(timeStamp)
        return sdf.format(netDate)
    }

    fun getDateForApiFromTimeStamp(timeStamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM", Locale.CANADA)
        val netDate = Date(timeStamp)
        return sdf.format(netDate)
    }


    fun addOneMonth(amount: Int): Date {
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

    fun TMonthformat(date: String?): String? {
        try {
            var format1 =  SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.000000'Z'", Locale.CANADA)
            val format = SimpleDateFormat("MM", Locale.CANADA)
            val c = Calendar.getInstance()
            c.time = format1.parse(date)
            return format.format(c.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }



    fun Tformat(date: String?): String? {
        try {
            var format1 =  SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.000000'Z'", Locale.CANADA)
            val format = SimpleDateFormat("MMM - hh:mm", Locale.CANADA)
            val c = Calendar.getInstance()
            c.time = format1.parse(date)
            return format.format(c.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    fun monthDate(date: Int?): String? {
        try {
            val format1 = SimpleDateFormat("MM", Locale.CANADA)
            val format = SimpleDateFormat("MMM", Locale.CANADA)
            val c = Calendar.getInstance()
            c.time = format1.parse(date.toString())
            return format.format(c.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    fun monthDateApi(date: Int?): String? {
        try {
            val format1 = SimpleDateFormat("MM", Locale.CANADA)
            val format = SimpleDateFormat("MM", Locale.CANADA)
            val c = Calendar.getInstance()
            c.time = format1.parse(date.toString())
            return format.format(c.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    fun yearDate(date: Int?): String? {
        try {
            val format1 = SimpleDateFormat("yyyy", Locale.CANADA)
            val format = SimpleDateFormat("yy", Locale.CANADA)
            val c = Calendar.getInstance()
            c.time = format1.parse(date.toString())
            return format.format(c.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    fun yearDateApi(date: Int?): String? {
        try {
            val format1 = SimpleDateFormat("yyyy", Locale.CANADA)
            val format = SimpleDateFormat("yyyy", Locale.CANADA)
            val c = Calendar.getInstance()
            c.time = format1.parse(date.toString())
            return format.format(c.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    fun dayDate(date: Int?): String? {
        try {
            val format1 = SimpleDateFormat("dd", Locale.CANADA)
            val format = SimpleDateFormat("EEEE", Locale.CANADA)
            val c = Calendar.getInstance()
            c.time = format1.parse(date.toString())
            return format.format(c.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    fun dayDateApi(date: Int?): String? {
        try {
            val format1 = SimpleDateFormat("dd", Locale.CANADA)
            val format = SimpleDateFormat("dd", Locale.CANADA)
            val c = Calendar.getInstance()
            c.time = format1.parse(date.toString())
            return format.format(c.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    fun getLeaveForaAdapterFromTimeStamp(date: String?): String? {
        try {
            val format1 = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.CANADA)
            val format = SimpleDateFormat("hh:mm a", Locale.CANADA)
            val c = Calendar.getInstance()
            c.time = format1.parse(date)
            return format.format(c.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }


}