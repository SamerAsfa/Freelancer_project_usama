package com.example.myapplication.myapplication.di

import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.data.BaseRequest
import com.example.myapplication.myapplication.data.remote.api.RetrofitApi
import com.example.myapplication.myapplication.data.remote.repository.RetrofitRepositoryImpl
import com.example.myapplication.myapplication.domain.datasource.RetrofitRepository
import com.example.myapplication.myapplication.models.UserModel
import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }


    @Singleton
    @Provides
    fun provideRetrofitApi(gson: Gson): Retrofit.Builder {

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

        return Retrofit.Builder()
            .baseUrl(BaseRequest.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
    }

    @Singleton
    @Provides
    fun provideRetrofitApi(retrofit: Retrofit.Builder): RetrofitApi {
        return retrofit
            .build()
            .create(RetrofitApi::class.java)
    }


    @Provides
    @Singleton
    fun provideRetrofitRepository(api: RetrofitApi): RetrofitRepository {
        return RetrofitRepositoryImpl(api)
    }

}