package com.example.myapplication.myapplication.data.remote.repository

import com.example.myapplication.myapplication.data.remote.api.RetrofitApi
import com.example.myapplication.myapplication.domain.datasource.RetrofitRepository
import javax.inject.Inject

class RetrofitRepositoryImpl
@Inject constructor(
    private val api: RetrofitApi
) : RetrofitRepository {

}