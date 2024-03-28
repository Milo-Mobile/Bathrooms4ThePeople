package com.milomobile.bathrooms4tp.di

import com.milomobile.bathrooms4tp.data.repository.BathroomRepository
import com.milomobile.bathrooms4tp.data.repository.BathroomRepositoryImpl
import com.milomobile.bathrooms4tp.presentation.bathroom_list.BathroomListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<BathroomRepository> { BathroomRepositoryImpl() }

    viewModel { BathroomListViewModel(get()) }
}