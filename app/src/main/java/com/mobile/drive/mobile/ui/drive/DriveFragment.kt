package com.mobile.drive.mobile.ui.drive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobile.drive.databinding.FragmentDriveBinding
import com.mobile.drive.mobile.ui.BaseFragment
import com.mobile.drive.mobile.utils.autoCleared

class DriveFragment : BaseFragment(
    showToolbar = false
) {
    private var binding: FragmentDriveBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDriveBinding.inflate(inflater)
        return binding.root
    }
}
