package com.picpay.desafio.android.di.modules

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {
    single { provideRetrofitClient(get(), get()) }
    factory { provideOkHttpClient(get()) }
    factory { provideHttpLoggingInterceptor() }
    factory<Converter.Factory> { provideConverterFactory() }
}

private const val BASE_URL = "https://609a908e0f5a13001721b74e.mockapi.io/picpay/api/"

private fun provideRetrofitClient(
    okHttpClient: OkHttpClient,
    converterFactory: Converter.Factory
): Retrofit {
    return Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(converterFactory)
        .client(okHttpClient)
        .build()
}

private fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
    OkHttpClient
        .Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()

private fun provideHttpLoggingInterceptor() =
    HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

private fun provideConverterFactory() = MoshiConverterFactory.create()