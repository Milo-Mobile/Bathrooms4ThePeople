package com.milomobile.bathrooms4tp.di

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.milomobile.bathrooms4tp.data.repository.BathroomRepository
import com.milomobile.bathrooms4tp.data.repository.BathroomRepositoryImpl
import com.milomobile.bathrooms4tp.data.repository.LocationRepository
import com.milomobile.bathrooms4tp.data.repository.LocationRepositoryImpl
import com.milomobile.bathrooms4tp.presentation.bathroom_list.BathroomListViewModel
import com.milomobile.bathrooms4tp.presentation.create_bathroom.CreateBathroomViewModel
import com.milomobile.bathrooms4tp.presentation.maps.MapViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<FusedLocationProviderClient> {
        LocationServices.getFusedLocationProviderClient(androidContext())
    }
    single<BathroomRepository> { BathroomRepositoryImpl() }
    single<LocationRepository> { LocationRepositoryImpl(locationClient = get(), context = get()) }

    viewModel { BathroomListViewModel(bathroomRepository = get(), locationRepository = get()) }
    viewModel { CreateBathroomViewModel(bathroomRepository = get()) }
    viewModel { MapViewModel(locationRepository = get(), bathroomRepository = get()) }
}