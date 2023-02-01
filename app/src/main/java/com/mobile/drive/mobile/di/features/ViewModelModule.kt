package com.mobile.drive.mobile.di.features

import com.mobile.drive.mobile.ui.drive.DriveViewModel
import com.mobile.drive.mobile.ui.folderdetails.FolderDetailsViewModel
import com.mobile.drive.mobile.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { LoginViewModel(get(), get()) }
    viewModel { DriveViewModel(get(), get()) }
    viewModel { parameters ->
        FolderDetailsViewModel(
            get(),
            folderId = parameters.get()
        )
    }
}
