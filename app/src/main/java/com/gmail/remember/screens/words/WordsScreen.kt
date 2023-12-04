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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.gmail.remember.R
import com.gmail.remember.common.components.ItemRememberCard
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
                            allWords = words,
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
        LazyColumn(
            modifier = Modifier
                .background(color = GraphiteBlack)
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(start = 4.dp, end = 4.dp, bottom = 10.dp, top = 10.dp)
        ) {
            items(words, key = { model -> model?.wordEng ?: "" }) { word ->
                ItemRememberCard(
                    model = word ?: WordModel(),
                    countSuccess = countSuccess,
                    enableMultiSelect = selectedWords.isNotEmpty(),
                    mediaPlayer = viewModel::mediaPlayer.get(),
                    onLongClick = { model ->
                        viewModel.enableMultiSelect(
                            word = model,
                            words = words,
                            childName = childName
                        )
                    },
                    onClick = { model ->
                        viewModel.selectWord(word = model)
                    }
                )
            }
        }
    }
}