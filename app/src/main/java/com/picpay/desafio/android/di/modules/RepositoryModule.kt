package com.picpay.desafio.android.di.modules

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.picpay.desafio.android.data.database.dao.UserDao
import com.picpay.desafio.android.data.database.model.UserEntity
import com.picpay.desafio.android.data.mapper.mapToEntityModel
import com.picpay.desafio.android.data.network.model.UserResponse
import com.picpay.desafio.android.data.network.service.UserService
import com.picpay.desafio.android.data.repository.UserRepositoryImpl
import com.picpay.desafio.android.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val repositoryModule = module {
    factory <UserRepository>{ UserRepositoryImpl(provideUserStore(get(), get()), Dispatchers.IO) }
}

private fun provideUserStore(
    service: UserService,
    dao: UserDao
) : Store<String, List<UserEntity>> = StoreBuilder.from(
    fetcher = Fetcher.of { _: String -> service.getUsers() },
    sourceOfTruth = SourceOfTruth.Companion.of(
        reader = { dao.readAll() },
        writer = { _: String, serviceResponse: List<UserResponse> ->
            val users = serviceResponse.mapToEntityModel()
            dao.insertAll(users)
        }
    )
).build()

