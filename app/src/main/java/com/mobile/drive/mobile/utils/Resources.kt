package com.mobile.drive.mobile.utils

import androidx.annotation.StringRes
import com.mobile.drive.mobile.DriveApplication

object Strings {
    fun get(@StringRes stringRes: Int, vararg formatArgs: Any = emptyArray()): String {
        return DriveApplication.instance.getString(stringRes, *formatArgs)
    }
}
