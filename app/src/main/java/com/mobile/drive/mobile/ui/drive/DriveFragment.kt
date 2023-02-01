package com.mobile.drive.mobile.ui.drive

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.mobile.drive.R
import com.mobile.drive.databinding.FragmentDriveBinding
import com.mobile.drive.mobile.ui.BaseFragment
import com.mobile.drive.mobile.ui.model.FileUiModel
import com.mobile.drive.mobile.utils.Strings
import com.mobile.drive.mobile.utils.autoCleared
import com.mobile.drive.mobile.vo.Status
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DriveFragment : MenuProvider, BaseFragment(
    title = Strings.get(R.string.drive_title)
) {
    private var binding: FragmentDriveBinding by autoCleared()
    private val viewModel: DriveViewModel by viewModel()
    private var adapter: FileAdapter by autoCleared()
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDriveBinding.inflate(inflater)
        setupView()
        setupObservers()
        googleSignInClient =
            GoogleSignIn.getClient(requireActivity(), viewModel.googleSignInOptions)
        activity?.addMenuProvider(this, viewLifecycleOwner)
        return binding.root
    }

    private fun setupView() = with(binding) {
        adapter = FileAdapter { file -> onItemClick(file) }
        fileList.adapter = adapter
    }

    private fun setupObservers() = with(viewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            uiContent.collect {
                when (it.state.status) {
                    Status.RUNNING -> {
                        showLoading()
                    }
                    Status.SUCCESS -> {
                        val items = it.state.data
                        binding.apply {
                            fileList.isVisible = items?.isEmpty() == false
                            notFiles.isVisible = items?.isEmpty() == true
                        }
                        adapter.submitList(items)
                        hideLoading()
                    }
                    Status.FAILED -> {
                        hideLoading()
                        showError(it.state.error?.message ?: "")
                    }
                    Status.EMPTY -> { // do nothing
                    }
                }
            }
        }
    }

    private fun onItemClick(file: FileUiModel) {
        // todo implement click
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.logout_menu, menu)
        menu.findItem(R.id.logout).actionView?.setOnClickListener {
            googleSignInClient.signOut().addOnCompleteListener {
                findNavController().navigate(DriveFragmentDirections.actionLogin())
            }.addOnFailureListener {
                showError(Strings.get(R.string.error_unexpected_message))
            }
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }
}
