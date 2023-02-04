package com.mobile.drive.mobile.di.features

import com.mobile.drive.mobile.ui.drive.DriveViewModel
import com.mobile.drive.mobile.ui.folderdetails.FolderDetailsViewModel
import com.mobile.drive.mobile.ui.login.LoginViewModel
import com.mobile.drive.mobile.utils.coroutines.CoroutineDispatcherProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { LoginViewModel(get()) }
    viewModel { DriveViewModel(get(), CoroutineDispatcherProvider()) }
    viewModel { parameters ->
        FolderDetailsViewModel(
            get(),
            CoroutineDispatcherProvider(),
            folder = parameters.get()
        )
    }
}
