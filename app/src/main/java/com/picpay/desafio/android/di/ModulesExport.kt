package com.picpay.desafio.android.di

import com.picpay.desafio.android.di.modules.*

val dependencyModules = listOf(
    uiModule,
    networkModule,
    serviceModule,
    repositoryModule,
    useCaseModule,
    databaseModule
)