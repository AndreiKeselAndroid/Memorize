package com.gmail.remember.screens.remember

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.gmail.remember.R
import com.gmail.remember.common.components.ItemRememberCard
import com.gmail.remember.models.RememberWordModel
import com.gmail.remember.navigation.Screens
import com.gmail.remember.ui.theme.GrayBlack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RememberScreen(
    navController: NavHostController,
    viewModel: RememberViewModel = hiltViewModel()
) {
    val words by viewModel.words.collectAsState()
    val selectedWords by viewModel.selectedWords.collectAsState()
    val photoUrl by viewModel.photoUrl.collectAsState()

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
                            text = stringResource(
                                id = if (selectedWords.isNotEmpty()) R.string.add_words
                                else R.string.app_name
                            )
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
                    }
                },
                actions = {
                    if (selectedWords.isEmpty()) IconButton(
                        onClick = {
                            navController.navigate(Screens.ProfileScreen.route)
                        }
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape),
                            model = photoUrl,
                            contentDescription = "Settings"
                        )
                    }
                    else IconButton(onClick = {
                        viewModel.selectedAllHandler(
                            allWords = words,
                            selectedWords = selectedWords,
                        )
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_check),
                            tint = Color.White,
                            contentDescription = "Check"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = GrayBlack,
                onClick = {
                    if (selectedWords.isNotEmpty()) viewModel.deleteWords(selectedWords)
                    else navController.navigate(Screens.AddWordsScreen.route)
                }) {
                if (selectedWords.isNotEmpty()) Icon(
                    imageVector = Icons.Default.Delete,
                    tint = Color.White,
                    contentDescription = "delete"
                )
                else Icon(
                    imageVector = Icons.Default.Add,
                    tint = Color.White,
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
                    model = word ?: RememberWordModel(),
                    enableMultiSelect = selectedWords.isNotEmpty(),
                    onLongClick = { model ->
                        viewModel.enableMultiSelect(
                            word = model,
                            words = words
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