package com.picpay.desafio.android.data.mapper

import com.picpay.desafio.android.data.database.model.UserEntity
import com.picpay.desafio.android.data.network.model.UserResponse
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.util.extensions.orElse

private const val UNKNOWN_USER = "Contato desconhecido"

fun UserResponse.mapToEntityModel() : UserEntity = UserEntity(
    id,
    image.orEmpty(),
    name.orElse(UNKNOWN_USER),
    username.orElse(UNKNOWN_USER)
)

fun List<UserResponse>.mapToEntityModel() : List<UserEntity> =
    this.map(UserResponse::mapToEntityModel)

fun UserEntity.mapToDomainModel() : User = User(id, image, name, username)

fun List<UserEntity>.mapToDomainModel() : List<User> = this.map(UserEntity::mapToDomainModel)