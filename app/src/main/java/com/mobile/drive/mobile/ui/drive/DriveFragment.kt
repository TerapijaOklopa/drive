package com.mobile.drive.mobile.ui.drive

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.mobile.drive.R
import com.mobile.drive.databinding.FragmentDriveBinding
import com.mobile.drive.mobile.ui.BaseFragment
import com.mobile.drive.mobile.utils.Strings
import com.mobile.drive.mobile.utils.autoCleared
import org.koin.androidx.viewmodel.ext.android.viewModel

class DriveFragment : MenuProvider, BaseFragment(
    title = Strings.get(R.string.drive_title)
) {
    private var binding: FragmentDriveBinding by autoCleared()
    private val viewModel: DriveViewModel by viewModel()
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDriveBinding.inflate(inflater)
        googleSignInClient =
            GoogleSignIn.getClient(requireActivity(), viewModel.googleSignInOptions)
        activity?.addMenuProvider(this, viewLifecycleOwner)
        return binding.root
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
