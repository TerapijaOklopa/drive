package com.mobile.drive.mobile.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.drive.data.session.SessionRepository
import com.mobile.drive.mobile.vo.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private var _loggedIn = MutableStateFlow(LoginContent())
    val loggedIn = _loggedIn.asStateFlow()

    init {
        userLoggedIn()
    }

    fun userLoggedIn() {
        viewModelScope.launch {
            val loggedIn = sessionRepository.getActiveUser() != null
            _loggedIn.emit(LoginContent(state = Resource.success(loggedIn)))
        }
    }
}

data class LoginContent(
    val state: Resource<Boolean> = Resource.loading()
)
