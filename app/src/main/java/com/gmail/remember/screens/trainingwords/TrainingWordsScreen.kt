package com.gmail.remember.screens.trainingwords

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.gmail.remember.R
import com.gmail.remember.common.components.Button
import com.gmail.remember.common.components.OutlineTextField
import com.gmail.remember.models.WordModel
import com.gmail.remember.ui.theme.BlackBrown
import com.gmail.remember.ui.theme.GraphiteBlack
import com.gmail.remember.ui.theme.GrayishOrange

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
internal fun TrainingWordsScreen(
    navController: NavHostController,
    viewModel: TrainingWordsViewModel = hiltViewModel()
) {
    val wordModelDeepLink by viewModel.wordModelDeepLink.collectAsState()
    val wordModel by viewModel.wordModel.collectAsState()
    val answer by viewModel.answer.collectAsState()
    val error by viewModel.error.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val message = if (!error &&
        wordModelDeepLink?.wordEng.isNullOrEmpty() &&
        wordModel?.wordEng.isNullOrEmpty()
    ) stringResource(R.string.chose_new_theme) else stringResource(
        id = R.string.error_message,
        wordModelDeepLink?.wordRu ?: wordModel?.wordRu ?: ""
    )
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.teacher)
    )
    val lottie by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
        speed = 1f,
        restartOnPlay = false
    )

    LaunchedEffect(error) {
        if (!error) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = GraphiteBlack),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .background(color = GraphiteBlack)
                    .clip(
                        RoundedCornerShape(
                            bottomEnd = 24.dp,
                            bottomStart = 24.dp
                        )
                    )
                    .fillMaxWidth(),
                colors = TopAppBarDefaults
                    .centerAlignedTopAppBarColors(
                        containerColor = BlackBrown,
                        titleContentColor = GrayishOrange
                    ),
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            fontWeight = FontWeight.Medium,
                            text = stringResource(R.string.title_training)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        focusManager.clearFocus()
                        navController.popBackStack()
                    }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            tint = GrayishOrange,
                            contentDescription = "BackIcon"
                        )
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.size(40.0.dp))
                }
            )
        },
        bottomBar = {
            if (!error) OutlineTextField(
                modifier = Modifier
                    .padding(top = 16.dp, start = 4.dp, end = 4.dp, bottom = 20.dp)
                    .focusRequester(focusRequester),

                value = answer,
                textLabel = stringResource(R.string.enter_answer),
                focusManager = focusManager,
                keyboardActions = { value ->
                    viewModel.onCheckAnswer(
                        word = wordModelDeepLink ?: wordModel ?: WordModel(),
                        answer = value,
                        errorMessage = message
                    )
                },
                onValueChange = { value -> viewModel.setAnswer(value = value) },
                trailingIcon = {
                    if (answer.isNotEmpty()) IconButton(
                        onClick = { viewModel.setAnswer("") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = GrayishOrange.copy(alpha = 0.32f)
                        )
                    }
                }
            )
            else Button(
                modifier = Modifier
                    .padding(bottom = 24.dp, start = 4.dp, end = 4.dp),
                onClick = {
                    viewModel.onNext()
                }
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = stringResource(R.string.title_ok)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = GraphiteBlack)
                .padding(paddingValues),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .background(color = GraphiteBlack)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                LottieAnimation(
                    composition = composition,
                    progress = lottie,
                    modifier = Modifier
                        .size(200.dp)
                )
                if (!wordModelDeepLink?.wordEng.isNullOrEmpty() ||
                    !wordModel?.wordEng.isNullOrEmpty()
                ) Card(
                    modifier = Modifier
                        .padding(end = 8.dp, top = 30.dp),
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomEnd = 12.dp,
                        bottomStart = 0.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = GrayishOrange,
                        contentColor = if (error) Color.Red else GraphiteBlack
                    )
                ) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp),
                        text = if (!error) wordModelDeepLink?.wordEng ?: wordModel?.wordEng ?: ""
                        else errorMessage
                    )
                }
                else Card(
                    modifier = Modifier
                        .padding(end = 8.dp, top = 30.dp),
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomEnd = 12.dp,
                        bottomStart = 0.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = GrayishOrange,
                        contentColor = GraphiteBlack
                    )
                ) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp),
                        text = message
                    )
                }
            }
        }
    }
}