package com.mobile.drive.mobile.ui.drive

import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class DriveViewModel(
    val googleSignInOptions: GoogleSignInOptions
) : ViewModel()
