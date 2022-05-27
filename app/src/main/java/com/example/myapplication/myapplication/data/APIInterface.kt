package com.example.myapplication.myapplication.data

import com.example.myapplication.myapplication.data.BaseRequest.Companion.dashboardApi
import com.example.myapplication.myapplication.models.ActionModel
import retrofit2.Call
import retrofit2.http.*


interface APIInterface {
    @GET(dashboardApi)
    fun getDashBoardApi(): Call<ActionModel?>?

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