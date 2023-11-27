package com.gmail.remember.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.gmail.remember.navigation.NavGraph
import com.gmail.remember.ui.theme.RememberTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RememberActivity : ComponentActivity() {

    private val viewModel: RememberViewModel by viewModels()
    private var requestPermissionLauncher: ActivityResultLauncher<Array<String>>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        registerPermissionListener(splashScreen = splashScreen)
        checkNotificationPermission(splashScreen = splashScreen)
    }

    private fun initialization(splashScreen: SplashScreen) {
        lifecycleScope.launch {
            viewModel.profile.collectLatest { profile ->
                splashScreen.setKeepOnScreenCondition {
                    profile?.idToken.isNullOrEmpty() &&
                            viewModel.firebaseAuth.currentUser != null
                }
            }
        }

        setContent {
            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.apply {
                    setStatusBarColor(
                        color = Color.Black,
                        darkIcons = false
                    )
                    setNavigationBarColor(
                        color = Color.Black,
                        darkIcons = false
                    )
                }
            }

            RememberTheme {
                NavGraph(
                    navController = rememberNavController(),
                    isAuth = viewModel.firebaseAuth.currentUser == null
                )
            }
        }
    }

    private fun checkNotificationPermission(splashScreen: SplashScreen) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                requestPermissionLauncher?.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
            else initialization(splashScreen = splashScreen)
        } else {
            initialization(splashScreen = splashScreen)
        }
    }

    private fun registerPermissionListener(splashScreen: SplashScreen) {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { initialization(splashScreen = splashScreen) }
    }
}