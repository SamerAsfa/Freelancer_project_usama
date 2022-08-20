package com.example.myapplication.myapplication.data

import com.example.myapplication.myapplication.data.BaseRequest.Companion.APPROVE_lEAVE
import com.example.myapplication.myapplication.data.BaseRequest.Companion.CHECK_USER_BY_COMPANY_ID
import com.example.myapplication.myapplication.data.BaseRequest.Companion.DELETE_ALL_NOTIFICATION
import com.example.myapplication.myapplication.data.BaseRequest.Companion.GET_MY_TEAM_NOTIFICATIONS
import com.example.myapplication.myapplication.data.BaseRequest.Companion.LOGIN_BY_EMAIL
import com.example.myapplication.myapplication.data.BaseRequest.Companion.REJECT_lEAVE
import com.example.myapplication.myapplication.data.BaseRequest.Companion.dashboardApi
import com.example.myapplication.myapplication.data.BaseRequest.Companion.leaveHistoryApi
import com.example.myapplication.myapplication.data.BaseRequest.Companion.myTeamLeaveHistoryApi
import com.example.myapplication.myapplication.data.BaseRequest.Companion.notificationsApi
import com.example.myapplication.myapplication.data.BaseRequest.Companion.punchHistoryApi
import com.example.myapplication.myapplication.data.remote.dto.LoginByEmailBody
import com.example.myapplication.myapplication.domain.model.GetAllNotificationsResponse
import com.example.myapplication.myapplication.models.*
import retrofit2.Call
import retrofit2.http.*


interface APIInterface {
    @GET(dashboardApi)
    fun getDashBoardApi(): Call<ActionModel?>?

    @GET("${punchHistoryApi}{monthYear}")///@Path("id") int groupId
    fun getHistoryApi(@Path("monthYear") monthYear: String?): Call<ArrayList<MyLeaveHistoryModel>?>?


    @GET("${leaveHistoryApi}{monthYear}")///@Path("id") int groupId
    fun getLeaveHistoryApi(@Path("monthYear") monthYear: String?): Call<ArrayList<MyLeaveHistoryModel>?>?

    @GET("${myTeamLeaveHistoryApi}{monthYear}")///@Path("id") int groupId
    fun getMyTeamLeaveHistoryApi(@Path("monthYear") monthYear: String?): Call<ArrayList<MyTeamLeaveHistoryModel>?>?


    @GET(notificationsApi)///@Path("id") int groupId
    fun getNotificationHistoryApi(): Call<GetAllNotificationsResponse?>?

    @GET("${CHECK_USER_BY_COMPANY_ID}{id}")//@Path("id") id: String?
    fun checkUserByUserCompanyId(@Path("id") id: String?): Call<OrganizationUserDetailsModel?>?

   // @FormUrlEncoded
    @POST(LOGIN_BY_EMAIL)
    fun loginByEmail(
       @Body loginByEmailBody:LoginByEmailBody): Call<UserModel?>?//@FormUrlEncoded

    @DELETE("${DELETE_ALL_NOTIFICATION}{id}")
    fun deleteAllNotification(id:String):Call<Any?>?


    @GET(GET_MY_TEAM_NOTIFICATIONS)
    fun getMyTeamNotifications(): Call<GetAllNotificationsResponse?>?

    @PUT("${APPROVE_lEAVE}{id}")
    fun approveLeave(@Path("id") id:Int?): Call<Any?>?

    @PUT("${REJECT_lEAVE}{id}")
    fun rejectLeave(@Path("id") id:Int?): Call<Any?>?

/*    @POST(LOGIN_BY_EMAIL)
    @FormUrlEncoded
    fun loginByEmail(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("device_id") device_id: Int,
        @Field("fcm") fcm: Int,
        @Field("lang") lang: String
    ): Call<UserModel?>?*/


//    @POST("/api/users")
//    fun createUser(@Body user: User?): Call<User?>?
//
//    @GET("/api/users?")
//    fun doGetUserList(@Query("page") page: String?): Call<UserList?>?
//
//    @FormUrlEncoded
//    @POST("/api/users?")
//    fun doCreateUserWithField(
//        @Field("name") name: String?,
//        @Field("job") job: String?
//    ): Call<UserList?>?
}