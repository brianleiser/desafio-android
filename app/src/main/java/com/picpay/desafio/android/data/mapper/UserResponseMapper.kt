package com.picpay.desafio.android.data.mapper

import com.picpay.desafio.android.data.network.model.UserResponse
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.util.extensions.orElse

private const val UNKNOWN_USER = "Contato desconhecido"

fun UserResponse.mapToDomainModel() : User = User(
    id,
    image.orEmpty(),
    name.orElse(UNKNOWN_USER),
    username.orElse(UNKNOWN_USER)
)