package com.picpay.desafio.android.domain.repository

import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.Result
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUsers() : Flow<Result<List<User>>>
}