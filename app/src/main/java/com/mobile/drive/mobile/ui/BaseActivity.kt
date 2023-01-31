package com.mobile.drive.mobile.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.mobile.drive.R

abstract class BaseActivity : AppCompatActivity(), BaseFragment.BaseFragmentInterface {

    abstract val layoutId: Int

    private val titleTv: TextView by lazy { findViewById(R.id.toolbar_title) }
    val toolbar: Toolbar by lazy { findViewById(R.id.main_toolbar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
        }

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }

    override fun setToolbarVisible(visible: Boolean) {
        if (visible) {
            supportActionBar?.show()
        } else {
            supportActionBar?.hide()
        }
    }

    override fun setToolbarTitle(text: String?) {
        text?.let {
            titleTv.text = it
            titleTv.visibility = View.VISIBLE
        }
    }

    override fun setNavigationIcon(resId: Int?) {
        resId?.let {
            toolbar.navigationIcon = getColoredIcon(it, R.color.toolbar_primary_icon_color)
        }
    }

    override fun setShowBack(show: Boolean) {
        if (show) {
            toolbar.navigationIcon =
                getColoredIcon(R.drawable.ic_left_arrow, R.color.toolbar_primary_icon_color)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }
    }

    @Suppress("SameParameterValue")
    private fun getColoredIcon(@DrawableRes drawableId: Int, @ColorRes colorId: Int): Drawable? {
        val drawable = ResourcesCompat.getDrawable(resources, drawableId, null)
        if (drawable != null) {
            val color = ContextCompat.getColor(this, colorId)
            DrawableCompat.setTint(drawable, color)
        }
        return drawable
    }
}
