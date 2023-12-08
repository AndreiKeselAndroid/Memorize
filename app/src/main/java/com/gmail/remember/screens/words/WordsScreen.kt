@file:Suppress("UNCHECKED_CAST")

package com.gmail.remember.screens.words

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
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
import com.gmail.remember.common.components.ItemRememberCard
import com.gmail.remember.data.api.models.Result
import com.gmail.remember.models.WordModel
import com.gmail.remember.navigation.Screens
import com.gmail.remember.navigation.navigateSafeArgs
import com.gmail.remember.ui.theme.BlackBrown
import com.gmail.remember.ui.theme.GraphiteBlack
import com.gmail.remember.ui.theme.GrayishOrange
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WordsScreen(
    navController: NavHostController,
    viewModel: WordsViewModel = hiltViewModel()
) {
    val words by viewModel.words.collectAsState()
    val selectedWords by viewModel.selectedWords.collectAsState()
    val childName by viewModel.childName.collectAsState()
    val countSuccess by viewModel.countSuccess.collectAsState()
    val state = rememberLazyListState()
    val stateUi by viewModel.stateUi.collectAsState()
    val emptyComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.empty_lottie)
    )
    val emptyProgress by animateLottieCompositionAsState(
        emptyComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
        speed = 1f,
        restartOnPlay = true
    )

    val errorComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.error_lottie)
    )
    val errorProgress by animateLottieCompositionAsState(
        errorComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
        speed = 1f,
        restartOnPlay = true
    )

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
                            bottomEnd = if (
                                remember {
                                    derivedStateOf {
                                        state.firstVisibleItemScrollOffset
                                    }
                                }.value != 0) 0.dp
                            else 24.dp,
                            bottomStart = if (
                                remember {
                                    derivedStateOf {
                                        state.firstVisibleItemScrollOffset
                                    }
                                }.value != 0) 0.dp
                            else 24.dp
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
                            text = if (selectedWords.isNotEmpty()) stringResource(
                                id = R.string.add_words
                            ) else childName.replaceFirstChar { char ->
                                if (char.isLowerCase()) char.titlecase(
                                    Locale.ROOT
                                ) else char.toString()
                            }
                        )
                    }
                },
                navigationIcon = {
                    if (selectedWords.isNotEmpty()) IconButton(onClick = {
                        viewModel.disableMultiSelect()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            tint = GrayishOrange,
                            contentDescription = "exit"
                        )
                    } else
                        IconButton(onClick = {
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
                    if (selectedWords.isNotEmpty()) IconButton(onClick = {
                        viewModel.selectedAllHandler(
                            allWords = words ?: emptyList(),
                            selectedWords = selectedWords,
                        )
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_check),
                            tint = GrayishOrange,
                            contentDescription = "Check"
                        )
                    }
                    else Spacer(modifier = Modifier.size(40.0.dp))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = GrayishOrange,
                onClick = {
                    if (selectedWords.isNotEmpty()) viewModel.deleteWords(selectedWords, childName)
                    else navController.navigateSafeArgs(
                        Screens.AddWordsScreen.route,
                        childName
                    )
                }) {
                if (selectedWords.isNotEmpty()) Icon(
                    imageVector = Icons.Default.Delete,
                    tint = GraphiteBlack,
                    contentDescription = "delete"
                )
                else Icon(
                    imageVector = Icons.Default.Add,
                    tint = GraphiteBlack,
                    contentDescription = "Add"
                )
            }
        }
    ) { paddingValues ->
        if (words != null && words?.isNotEmpty() == true) LazyColumn(
            state = state,
            modifier = Modifier
                .background(color = GraphiteBlack)
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 10.dp, top = 10.dp, start = 4.dp, end = 4.dp)
        ) {
            items(words!!, key = { model -> model?.wordEng ?: "" }) { word ->
                ItemRememberCard(
                    model = word ?: WordModel(),
                    countSuccess = countSuccess,
                    enableMultiSelect = selectedWords.isNotEmpty(),
                    mediaPlayer = viewModel::mediaPlayer.get(),
                    onLongClick = { model ->
                        viewModel.enableMultiSelect(
                            word = model,
                            words = words!!,
                            childName = childName
                        )
                    },
                    onClick = { model ->
                        viewModel.selectWord(word = model)
                    }
                )
            }
        }
        else if (words?.isEmpty() == true) Box(
            modifier = Modifier
                .background(color = GraphiteBlack)
                .padding(20.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                emptyComposition,
                emptyProgress,
                modifier = Modifier.padding(20.dp)
            )
        }
    }

    when(stateUi){
        is Result.Success -> {
            viewModel.setWords(
                words = ((stateUi as? Result.Success<*>)?.data as? List<WordModel?>) ?: emptyList()
            )
        }

        is Result.Error -> {
            Box(
                modifier = Modifier
                    .background(color = GraphiteBlack)
                    .padding(20.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    errorComposition,
                    errorProgress,
                    modifier = Modifier.padding(20.dp)
                )
            }
        }

        else -> {}
    }
}