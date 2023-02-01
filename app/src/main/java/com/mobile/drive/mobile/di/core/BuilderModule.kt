package com.mobile.drive.mobile.di.core

import com.mobile.drive.mobile.di.domain.repositoryModule
import com.mobile.drive.mobile.di.features.viewModelModule
import com.mobile.drive.mobile.di.network.networkModule

val allModules = listOf(
    viewModelModule,
    repositoryModule,
    networkModule
)
