package com.picpay.desafio.android.di.modules

import com.picpay.desafio.android.presentation.UserListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { UserListViewModel(getUsers = get()) }
}