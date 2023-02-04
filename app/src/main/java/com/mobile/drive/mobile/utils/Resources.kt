package com.mobile.drive.mobile.utils

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.mobile.drive.mobile.DriveApplication

object Strings {
    fun get(@StringRes stringRes: Int, vararg formatArgs: Any = emptyArray()): String {
        return DriveApplication.instance.getString(stringRes, *formatArgs)
    }
}

object Drawables {
    fun get(@DrawableRes drawableRes: Int): Drawable? {
        return ContextCompat.getDrawable(DriveApplication.instance, drawableRes)
    }
}
