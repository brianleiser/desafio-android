package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository
import com.picpay.desafio.android.domain.Result
import kotlinx.coroutines.flow.Flow

class GetUsers(private val repository: UserRepository) {

    suspend operator fun invoke() : Flow<Result<List<User>>> = repository.getUsers()
}