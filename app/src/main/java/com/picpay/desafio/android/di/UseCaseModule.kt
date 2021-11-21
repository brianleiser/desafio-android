package com.picpay.desafio.android.di

import com.picpay.desafio.android.domain.usecase.GetUsers
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetUsers(get()) }
}