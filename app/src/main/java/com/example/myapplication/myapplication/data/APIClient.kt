package com.example.myapplication.myapplication.data


import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.models.UserModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


internal object APIClient {
    private var retrofit: Retrofit? = null
    val client: Retrofit?
        get() {
            val userModel: UserModel = LongTermManager.getInstance().userModel
            val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    return chain.proceed(
                        chain.request()
                            .newBuilder()
                            .addHeader("Authorization", "Bearer ${userModel.token}")
                            .addHeader("Accept", "application/json")
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .build()
                    )
                }
            }).build()
            retrofit = Retrofit.Builder()
                .baseUrl(BaseRequest.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit
        }

}