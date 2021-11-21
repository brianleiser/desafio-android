package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.mapper.mapToDomainModel
import com.picpay.desafio.android.data.network.model.UserResponse
import com.picpay.desafio.android.data.network.service.UserService
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository

class UserRepositoryImpl(private val service: UserService): UserRepository {

    override suspend fun getUsers(): List<User> =
        service.getUsers().map(UserResponse::mapToDomainModel)
}