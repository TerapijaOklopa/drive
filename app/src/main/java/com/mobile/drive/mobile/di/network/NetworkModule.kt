package com.mobile.drive.mobile.di.network

import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import org.koin.dsl.module

val networkModule = module {
    single { providesGoogleSignInOptions() }
}

fun providesGoogleSignInOptions(): GoogleSignInOptions {
    return GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestProfile()
        .build()
}
