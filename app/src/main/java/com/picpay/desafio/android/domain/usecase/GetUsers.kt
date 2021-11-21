package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository

class GetUsers(private val repository: UserRepository) {

    suspend operator fun invoke() : Set<User> = repository.getUsers().toSet()
}