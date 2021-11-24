package com.picpay.desafio.android.di.modules

import android.app.Application
import androidx.room.Room
import com.picpay.desafio.android.data.database.AppDatabase
import com.picpay.desafio.android.data.database.dao.UserDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single { provideDatabase(androidApplication()) }
    single { provideUserDao(get()) }
}

private fun provideDatabase(application: Application) : AppDatabase {
    return Room
        .databaseBuilder(application, AppDatabase::class.java, "UsersDB")
        .fallbackToDestructiveMigration()
        .build()
}

private fun provideUserDao(database: AppDatabase) : UserDao = database.userDao()