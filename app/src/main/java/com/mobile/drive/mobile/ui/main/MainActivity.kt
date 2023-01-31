package com.mobile.drive.mobile.ui.main

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.mobile.drive.R
import com.mobile.drive.mobile.ui.BaseActivity

class MainActivity : BaseActivity() {

    private lateinit var navController: NavController
    private val graphRootDestinations = setOf(R.id.navigation_login, R.id.navigation_drive)

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setupNavigationBar()
    }

    private fun setupNavigationBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_container_main) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun setShowBack(show: Boolean) {
        super.setShowBack(show)
        lifecycleScope.launchWhenResumed {
            if (!show && (navController.currentDestination?.id in graphRootDestinations)) {
                supportActionBar?.setDisplayShowHomeEnabled(false)
                toolbar.navigationIcon = null
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
