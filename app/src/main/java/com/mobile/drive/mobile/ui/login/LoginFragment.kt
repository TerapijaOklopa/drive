package com.mobile.drive.mobile.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mobile.drive.databinding.FragmentLoginBinding
import com.mobile.drive.mobile.ui.BaseFragment
import com.mobile.drive.mobile.utils.autoCleared

class LoginFragment : BaseFragment(
    showToolbar = false
) {
    private var binding: FragmentLoginBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        setupView()
        return binding.root
    }

    private fun setupView() = with(binding) {
        login.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionDrive())
        }
    }
}
