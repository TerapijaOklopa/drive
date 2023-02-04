package com.mobile.drive.mobile.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.mobile.drive.R
import com.mobile.drive.databinding.FragmentLoginBinding
import com.mobile.drive.mobile.ui.BaseFragment
import com.mobile.drive.mobile.utils.GoogleUtil
import com.mobile.drive.mobile.utils.Strings
import com.mobile.drive.mobile.utils.autoCleared
import com.mobile.drive.mobile.utils.Status
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment(
    showToolbar = false
) {
    private var binding: FragmentLoginBinding by autoCleared()
    private val viewModel: LoginViewModel by viewModel()
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        setupView()
        setupObservers()
        googleSignInClient =
            GoogleSignIn.getClient(requireActivity(), GoogleUtil.googleSignInOptions)
        return binding.root
    }

    private fun setupView() = with(binding) {
        login.setOnClickListener {
            performLogin()
        }
    }

    private fun setupObservers() = with(viewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            loggedIn.collect {
                when (it.state.status) {
                    Status.RUNNING -> showLoading()
                    Status.SUCCESS -> {
                        if (it.state.data == true) {
                            navigateToDrive()
                        }
                    }
                    Status.FAILED -> showError(
                        it.state.error?.message
                            ?: Strings.get(R.string.error_unexpected_message)
                    )
                    Status.EMPTY -> { // do nothing
                    }
                }
            }
        }
    }

    private fun performLogin() {
        val signInIntent = googleSignInClient.signInIntent
        previewRequest.launch(signInIntent)
    }

    private val previewRequest =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                handleSignData(it.data)
            } else {
                showError(Strings.get(R.string.error_unexpected_message))
            }
        }

    private fun handleSignData(data: Intent?) {
        // The Task returned from this call is always completed, no need to attach
        // a listener.
        GoogleSignIn.getSignedInAccountFromIntent(data)
            .addOnCompleteListener {
                navigateToDrive()
            }.addOnFailureListener {
                showError(Strings.get(R.string.error_unexpected_message))
            }
    }

    private fun navigateToDrive() {
        findNavController().navigate(LoginFragmentDirections.actionDrive())
    }
}
