package com.mobile.drive.mobile

import android.app.Application
import com.mobile.drive.mobile.di.core.allModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class DriveApplication : Application() {
    companion object {
        lateinit var instance: DriveApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        instance = this
        startKoin {
            androidContext(this@DriveApplication)
            modules(allModules)
        }
    }
}
