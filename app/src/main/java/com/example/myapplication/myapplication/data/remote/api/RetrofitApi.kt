package com.example.myapplication.myapplication.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {


    @GET("companyById/123456")
    suspend fun getUserNameById(
        @Query("id") id: String,
    ): String

}