package com.example.myapplication.myapplication.data

class BaseRequest {


    companion object {
        val baseUrl: String = "http://frapi.hr-jo.com/api"
        val loginApi: String = "${baseUrl}/token"
        val uploadVodApi: String = "${baseUrl}/uploadVod"
        val registerApi: String = "${baseUrl}/register"
        val dashboardApi: String = "${baseUrl}/dashboard"
        val punchHistoryApi: String = "${baseUrl}/punchHistory"

        val PINApi: String = "${baseUrl}/PIN"
        val POUTApi: String = "${baseUrl}/POUT"
        val BREAKApi: String = "${baseUrl}/BREAK"
        val LEAVEApi: String = "${baseUrl}/LEAVE"


    }
}