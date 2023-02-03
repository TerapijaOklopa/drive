package com.mobile.drive.mobile.ui.folderdetails

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mobile.drive.databinding.FragmentFolderDetailsBinding
import com.mobile.drive.mobile.ui.BaseActivity
import com.mobile.drive.mobile.ui.BaseFragment
import com.mobile.drive.mobile.ui.drive.FileAdapter
import com.mobile.drive.mobile.ui.model.FileUiModel
import com.mobile.drive.mobile.utils.autoCleared
import com.mobile.drive.mobile.vo.Status
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FolderDetailsFragment : MenuProvider, BaseFragment(
    showBack = true
) {
    private var binding: FragmentFolderDetailsBinding by autoCleared()
    private val args: FolderDetailsFragmentArgs by navArgs()
    private val viewModel: FolderDetailsViewModel by viewModel() {
        parametersOf(args.folderId)
    }
    private var adapter: FileAdapter by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // https://issuetracker.google.com/issues/237374580 singleTop navigation not working as expected so i have to add workaround :(
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewModel.handleBack()) {
                        findNavController().popBackStack()
                    }
                }
            }
        )
        binding = FragmentFolderDetailsBinding.inflate(inflater)
        title = args.name
        setupView()
        setupObservers()
        activity?.addMenuProvider(this, viewLifecycleOwner)
        return binding.root
    }

    private fun setupView() = with(binding) {
        adapter = FileAdapter { file -> onListItemClick(file) }
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
                            noFiles.isVisible = items?.isEmpty() == true
                        }
                        adapter.submitList(items)
                        hideLoading()
                    }
                    Status.FAILED -> {
                        showError(it.state.error?.message ?: "")
                    }
                    Status.EMPTY -> { // do nothing
                    }
                }
            }
        }
    }

    private fun onListItemClick(file: FileUiModel) {
        // https://issuetracker.google.com/issues/237374580 singleTop navigation not working as expected so i have to add workaround :(
//        FolderDetailsFragmentDirections.actionFolderDetails(
//            file.id,
//            file.name
//        )
        if (file.isFolder) {
            (activity as BaseActivity).setToolbarTitle(file.name)
            viewModel.onItemClick(file)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            if (viewModel.handleBack()) {
                findNavController().popBackStack()
                return false
            }
            return true
        }
        return true
    }
}
