package com.picpay.desafio.android.di.modules

import com.picpay.desafio.android.data.network.service.UserService
import org.koin.dsl.module
import retrofit2.Retrofit

val serviceModule = module {
    single { provideUserService(get()) }
}

private fun provideUserService(retrofit: Retrofit): UserService =
    retrofit.create(UserService::class.java)