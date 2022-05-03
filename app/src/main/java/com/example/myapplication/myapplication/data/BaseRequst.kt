package com.example.myapplication.myapplication.data

class BaseRequest {


    companion object {
        val baseUrl: String = "http://frapi.hr-jo.com/api"
        val loginApi: String = "${baseUrl}/token"
        val uploadVodApi: String = "${baseUrl}/uploadVod"
        val registerApi: String = "${baseUrl}/register"
        val dashboardApi: String = "${baseUrl}/dashboard"
        val punchHistoryApi: String = "${baseUrl}/punchHistory"
        val leaveApi: String = "${baseUrl}/leave"
        val leaveTypeApi: String = "${baseUrl}/leaveType"
        val updateTokenFcmApi: String = "${baseUrl}/updateTokenFcm"

        val PINApi: String = "${baseUrl}/punch/PIN"
        val POUTApi: String = "${baseUrl}/punch/POUT"
        val BREAKApi: String = "${baseUrl}/punch/BREAK"
        val LEAVEApi: String = "${baseUrl}/punch/LEAVE"


    }
}