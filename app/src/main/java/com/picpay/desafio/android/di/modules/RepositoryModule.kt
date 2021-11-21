package com.picpay.desafio.android.di.modules

import com.picpay.desafio.android.data.repository.UserRepositoryImpl
import com.picpay.desafio.android.domain.repository.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory <UserRepository>{ UserRepositoryImpl(get()) }
}