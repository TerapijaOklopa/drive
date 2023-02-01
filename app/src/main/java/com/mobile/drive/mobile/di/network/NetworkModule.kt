package com.mobile.drive.mobile.di.network

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.mobile.drive.R
import com.mobile.drive.data.session.SessionRepository
import com.mobile.drive.mobile.utils.Strings
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val networkModule = module {
    single { providesGoogleSignInOptions() }
    factory { providesDriveService(get(), androidApplication()) }
}

fun providesGoogleSignInOptions(): GoogleSignInOptions {
    return GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestProfile()
        .requestScopes(Scope(DriveScopes.DRIVE))
        .build()
}

fun providesDriveService(
    sessionRepository: SessionRepository,
    context: Context
): Drive {
    val credential = GoogleAccountCredential.usingOAuth2(
        context,
        listOf(DriveScopes.DRIVE)
    )
    credential.selectedAccount = sessionRepository.getActiveUser()
    return Drive.Builder(
        NetHttpTransport(),
        GsonFactory.getDefaultInstance(),
        credential
    ).setApplicationName(Strings.get((R.string.app_name)))
        .build()
}
