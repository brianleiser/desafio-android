package com.picpay.desafio.android.util.extensions

fun String?.orElse(defaultValue: String) = if (this.isNullOrBlank()) defaultValue else this