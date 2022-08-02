package com.example.myapplication.myapplication.data

import com.example.myapplication.myapplication.data.BaseRequest.Companion.CHECK_USER_BY_COMPANY_ID
import com.example.myapplication.myapplication.data.BaseRequest.Companion.dashboardApi
import com.example.myapplication.myapplication.data.BaseRequest.Companion.leaveHistoryApi
import com.example.myapplication.myapplication.data.BaseRequest.Companion.notificationsApi
import com.example.myapplication.myapplication.data.BaseRequest.Companion.punchHistoryApi
import com.example.myapplication.myapplication.models.ActionModel
import com.example.myapplication.myapplication.models.HistoryModel
import com.example.myapplication.myapplication.models.NotificationModel
import com.example.myapplication.myapplication.models.OrganizationUserDetailsModel
import retrofit2.Call
import retrofit2.http.*


interface APIInterface {
    @GET(dashboardApi)
    fun getDashBoardApi(): Call<ActionModel?>?

    @GET("${punchHistoryApi}{monthYear}")///@Path("id") int groupId
    fun getHistoryApi(@Path("monthYear") monthYear: String?): Call<ArrayList<HistoryModel>?>?


    @GET("${leaveHistoryApi}{monthYear}")///@Path("id") int groupId
    fun getLeaveHistoryApi(@Path("monthYear") monthYear: String?): Call<ArrayList<HistoryModel>?>?


    @GET(notificationsApi)///@Path("id") int groupId
    fun getNotificationHistoryApi(): Call<ArrayList<NotificationModel>?>?

    @GET(CHECK_USER_BY_COMPANY_ID)//@Path("id") id: String?
    fun checkUserByUserCompanyId(): Call<OrganizationUserDetailsModel?>?



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