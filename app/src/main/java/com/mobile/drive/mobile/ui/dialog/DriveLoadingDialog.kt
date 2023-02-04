package com.mobile.drive.mobile.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mobile.drive.R
import com.mobile.drive.databinding.LoadingViewBinding
import com.mobile.drive.mobile.utils.autoCleared

class DriveLoadingDialog : DialogFragment() {
    private var binding: LoadingViewBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoadingViewBinding.inflate(inflater)
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.AppTheme_Dialog_Base
    }
}
