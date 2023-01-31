package com.mobile.drive.mobile.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.snackbar.Snackbar
import com.mobile.drive.mobile.ui.dialog.DriveLoadingDialog

abstract class BaseFragment(
    var showToolbar: Boolean = true,
    var showBack: Boolean = false,
    var title: String? = null
) : Fragment(), LifecycleObserver {
    interface BaseFragmentInterface {
        fun setToolbarVisible(visible: Boolean)
        fun setToolbarTitle(text: String?)
        fun setNavigationIcon(resId: Int?)
        fun setShowBack(show: Boolean)
    }

    private var loadingDialog: DriveLoadingDialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifecycle.addObserver(this)
    }

    override fun onDetach() {
        super.onDetach()
        lifecycle.removeObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onCreated() {
        if (activity is BaseFragmentInterface) {
            with(activity as BaseFragmentInterface) {
                setToolbarVisible(showToolbar)
                setToolbarTitle(title)
                setShowBack(showBack)
            }
        }
    }

    fun showError(errorMessage: String) {
        hideLoading()
        Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_LONG).show()
    }

    fun showLoading() {
        if (loadingDialog == null) loadingDialog = DriveLoadingDialog()
        loadingDialog?.show(parentFragmentManager, "Loading dialog")
    }

    fun hideLoading() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }
}
