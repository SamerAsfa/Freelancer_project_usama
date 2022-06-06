package com.example.myapplication.myapplication.data

class BaseRequest {


    companion object {
        val baseUrl: String = "http://frapi.hr-jo.com/api/"
        val loginApi: String = "${baseUrl}token"
        val uploadVodApi: String = "${baseUrl}uploadVod"
        val registerApi: String = "${baseUrl}register"
        const val dashboardApi: String = "dashboard"
        const val punchHistoryApi: String = "punchHistory/"
        const val leaveHistoryApi: String = "leave/"
        const val notificationsApi: String = "notifications/"
        val leaveApi: String = "${baseUrl}leave"
        val leaveTypeApi: String = "${baseUrl}leaveType"
        val updateTokenFcmApi: String = "${baseUrl}updateTokenFcm"
        val PINApi: String = "${baseUrl}punch/PIN"
        val POUTApi: String = "${baseUrl}punch/POUT"
        val BREAKInApi: String = "${baseUrl}punch/BREAKIN"
        val BREAKOutApi: String = "${baseUrl}punch/BREAKOUT"
        val LEAVEINApi: String = "${baseUrl}punch/LEAVEIN"
        val LEAVEOutApi: String = "${baseUrl}punch/LEAVEOUT"
    }
}