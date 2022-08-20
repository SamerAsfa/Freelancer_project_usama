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
        const val myTeamLeaveHistoryApi: String = "myTeam/leave/"
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
        const val CHECK_USER_BY_COMPANY_ID: String = "http://frapi.hr-jo.com/api/companyById/"//123456
        const val DELETE_ALL_NOTIFICATION: String = "http://frapi.hr-jo.com/api/notification/"
        const val GET_MY_TEAM_NOTIFICATIONS: String = "http://frapi.hr-jo.com/api/myTeam/notifications"
        const val LOGIN_BY_EMAIL: String = "tokenByEmail"
        const val APPROVE_lEAVE: String = "http://frapi.hr-jo.com/api/leave/approve/"
        const val REJECT_lEAVE: String =  "http://frapi.hr-jo.com/api/leave/reject/"


    }
}