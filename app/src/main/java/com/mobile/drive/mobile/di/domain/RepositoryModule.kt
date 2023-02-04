package com.mobile.drive.mobile.di.domain

import com.mobile.drive.data.drive.DriveRepository
import com.mobile.drive.data.drive.DriveRepositoryImpl
import com.mobile.drive.data.session.SessionRepository
import com.mobile.drive.data.session.SessionRepositoryImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val repositoryModule = module {
    single<SessionRepository> { SessionRepositoryImpl(androidApplication()) }
    factory<DriveRepository> { DriveRepositoryImpl(get()) }
}
