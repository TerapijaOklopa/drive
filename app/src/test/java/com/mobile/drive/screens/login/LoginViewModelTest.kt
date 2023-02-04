package com.mobile.drive.screens.login

import android.accounts.Account
import com.google.common.truth.Truth.assertThat
import com.mobile.BaseTest
import com.mobile.drive.data.session.SessionRepository
import com.mobile.drive.mobile.ui.login.LoginViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest : BaseTest() {

    private val sessionRepository: SessionRepository = mockk(relaxed = true)
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(
            sessionRepository = sessionRepository
        )
    }

    @Test
    fun `LoginViewModel userLoggedIn false`() {
        coEvery {
            sessionRepository.getActiveUser()
        } returns null
        loginViewModel.userLoggedIn()
        assertThat(loginViewModel.loggedIn.value.state.data).isFalse()
    }

    @Test
    fun `LoginViewModel userLoggedIn true`() {
        coEvery {
            sessionRepository.getActiveUser()
        } returns Account("smallPdf", "")
        loginViewModel.userLoggedIn()
        assertThat(loginViewModel.loggedIn.value.state.data).isTrue()
    }
}
