package com.picpay.desafio.android

import com.picpay.desafio.android.data.network.model.UserResponse
import com.picpay.desafio.android.data.network.service.UserService

class ExampleService(
    private val service: UserService
) {

    fun example(): List<UserResponse> {
        val users = service.getUsers().execute()

        return users.body() ?: emptyList()
    }
}