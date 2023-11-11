package com.gmail.remember.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
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
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.token.collectLatest { token ->
                splashScreen.setKeepOnScreenCondition {
                    token.isNullOrEmpty() &&
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
}