package com.picpay.desafio.android

import android.app.Application
import com.picpay.desafio.android.di.networkModule
import com.picpay.desafio.android.di.serviceModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class PicPayApplication: Application() {

    private val dependencyModules = listOf(networkModule, serviceModule)

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PicPayApplication)
            androidLogger(Level.DEBUG)
            modules(dependencyModules)
        }
    }
}