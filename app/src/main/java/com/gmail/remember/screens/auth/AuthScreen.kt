package com.gmail.remember.screens.auth

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ComponentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.gmail.remember.R
import com.gmail.remember.navigation.Screens
import com.gmail.remember.ui.theme.GraphiteBlack
import com.gmail.remember.ui.theme.GrayishOrange
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

@SuppressLint("RestrictedApi")
@Composable
fun AuthScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val intent = Intent(
                    Settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS,
                    Uri.parse("package:${context.packageName}")
                )
                context.startActivity(intent)
            }
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            runCatching {
                val account = task.getResult(ApiException::class.java)
                if (account != null)
                    account.idToken?.let { idToken ->
                        viewModel.auth(idToken) {
                            if (it.isSuccessful) {
                                navController.navigate(Screens.MainScreen.route) {
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                }
                                viewModel.saveProfile(account = account)
                            }
                        }
                    }
            }
        })

    val activity = LocalContext.current as ComponentActivity

    LaunchedEffect(key1 = viewModel) {
        viewModel.signIn(activity) { client ->
            launcher.launch(client.signInIntent)
        }
    }

    Box(
        modifier = Modifier
            .background(GraphiteBlack)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    indication = null,
                    onClick = {
                        viewModel.signIn(activity) { client ->
                            launcher.launch(client.signInIntent)
                        }
                    }
                )
                .background(GraphiteBlack),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .padding(8.dp)
                    .size(24.dp),
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google",
                tint = Color.Unspecified
            )
            Text(
                fontSize = 16.sp,
                text = stringResource(R.string.chose_google_account),
                color = GrayishOrange
            )
        }
    }
}