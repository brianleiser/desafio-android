package com.picpay.desafio.android.data.network.service

import com.picpay.desafio.android.data.network.model.UserResponse
import retrofit2.http.GET

interface UserService {

    @GET("users")
    suspend fun getUsers(): List<UserResponse>
}