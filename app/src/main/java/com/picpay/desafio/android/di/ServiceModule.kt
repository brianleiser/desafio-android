package com.picpay.desafio.android.di

import com.picpay.desafio.android.PicPayService
import org.koin.dsl.module
import retrofit2.Retrofit

val serviceModule = module {
    single { providePicPayService(get()) }
}

private fun providePicPayService(retrofit: Retrofit): PicPayService =
    retrofit.create(PicPayService::class.java)