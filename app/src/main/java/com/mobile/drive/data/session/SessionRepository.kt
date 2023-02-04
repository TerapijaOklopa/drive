package com.mobile.drive.data.session

import android.accounts.Account
import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn

class SessionRepositoryImpl(private val context: Context) : SessionRepository {

    override fun getActiveUser(): Account? {
        GoogleSignIn.getLastSignedInAccount(context)?.let {
            return it.account
        }
        return null
    }
}

interface SessionRepository {
    fun getActiveUser(): Account?
}
