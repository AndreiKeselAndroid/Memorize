package com.gmail.remember.screens.words

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
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
            .background(color = Color.Black),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TopAppBarDefaults
                    .centerAlignedTopAppBarColors(
                        containerColor = Color.Black,
                        titleContentColor = Color.White
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
                            tint = Color.White,
                            contentDescription = "exit"
                        )
                    } else
                        IconButton(onClick = {
                            navController.popBackStack()
                        }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                tint = Color.White,
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
                            tint = Color.Green,
                            contentDescription = "Check"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Color.White,
                onClick = {
                    if (selectedWords.isNotEmpty()) viewModel.deleteWords(selectedWords, childName)
                    else navController.navigateSafeArgs(
                        Screens.AddWordsScreen.route,
                        childName
                    )
                }) {
                if (selectedWords.isNotEmpty()) Icon(
                    imageVector = Icons.Default.Delete,
                    tint = Color.Red,
                    contentDescription = "delete"
                )
                else Icon(
                    imageVector = Icons.Default.Add,
                    tint = Color.Black,
                    contentDescription = "Add"
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .background(color = Color.Black)
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(8.dp),

            ) {
            items(words) { word ->
                ItemRememberCard(
                    model = word ?: WordModel(),
                    countSuccess = countSuccess,
                    enableMultiSelect = selectedWords.isNotEmpty(),
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