package com.mobile.drive.mobile.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.mobile.drive.data.session.SessionRepository
import com.mobile.drive.mobile.vo.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    val googleSignInOptions: GoogleSignInOptions,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _loggedIn = MutableSharedFlow<LoginContent>()
    val loggedIn = _loggedIn.asSharedFlow()

    init {
        userLoggedIn()
    }

    private fun userLoggedIn() {
        viewModelScope.launch {
            val loggedIn = sessionRepository.getActiveUser() != null
            _loggedIn.emit(LoginContent(state = Resource.success(loggedIn)))
        }
    }
}

data class LoginContent(
    val state: Resource<Boolean> = Resource.loading()
)
