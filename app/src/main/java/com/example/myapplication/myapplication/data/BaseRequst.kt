package com.example.myapplication.myapplication.data

class BaseRequest {


    companion object {
        val baseUrl: String = "http://frapi.hr-jo.com/api/"
        val loginApi: String = "${baseUrl}token"
        val uploadVodApi: String = "${baseUrl}uploadVod"
        val registerApi: String = "${baseUrl}register"
        const val dashboardApi: String = "dashboard"
        val punchHistoryApi: String = "${baseUrl}punchHistory"
        val leaveApi: String = "${baseUrl}leave"
        val leaveTypeApi: String = "${baseUrl}leaveType"
        val updateTokenFcmApi: String = "${baseUrl}updateTokenFcm"
        val notificationsApi: String = "${baseUrl}notifications"
        val PINApi: String = "${baseUrl}punch/PIN"
        val POUTApi: String = "${baseUrl}punch/POUT"
        val BREAKInApi: String = "${baseUrl}punch/BREAKIN"
        val BREAKOutApi: String = "${baseUrl}punch/BREAKOUT"
        val LEAVEApi: String = "${baseUrl}punch/LEAVE"
    }
}