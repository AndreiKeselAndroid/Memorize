package com.gmail.remember.screens.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ComponentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.gmail.remember.R
import com.gmail.remember.common.components.ShowProgressBarDialog
import com.gmail.remember.navigation.Screens
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

@Composable
fun AuthScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            runCatching {
                val account = task.getResult(ApiException::class.java)
                if (account != null)
                    account.idToken?.let { idToken ->
                        viewModel.auth(idToken) {
                            if (it.isSuccessful) {
                                navController.navigate(Screens.RememberScreen.route){
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                }
                                viewModel.saveToken(idToken)
                            }
                        }
                    }
            }
        })
    val activity = LocalContext.current as ComponentActivity
    val isShowProgress by viewModel.isShowProgress.collectAsState()

    Box(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    viewModel.signIn(activity) { client ->
                        launcher.launch(client.signInIntent)
                    }
                },
            text = stringResource(R.string.sign),
            color = Color.White
        )
    }

    if (isShowProgress) ShowProgressBarDialog()
}