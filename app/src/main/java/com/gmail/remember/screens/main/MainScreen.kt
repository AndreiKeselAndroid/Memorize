package com.gmail.remember.screens.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.gmail.remember.R
import com.gmail.remember.common.components.Button
import com.gmail.remember.common.components.ItemBrainCard
import com.gmail.remember.common.components.OutlineTextField
import com.gmail.remember.navigation.Screens
import com.gmail.remember.navigation.navigateSafeArgs
import com.gmail.remember.ui.theme.GrayBlack

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel()
) {

    val photoUrl by viewModel.photoUrl.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val name by viewModel.name.collectAsState()
    val themes by viewModel.themes.collectAsState()
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val displayMetrics = LocalContext.current.resources.displayMetrics
    val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
    val countColumn by remember { mutableStateOf((screenWidthDp / 200f + 0.5).toInt()) }

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
                                id = R.string.themes
                            )
                        )
                    }
                },
                actions = {
                    IconButton(
                        modifier = Modifier.padding(end = 10.dp),
                        onClick = {
                            navController.navigate(Screens.ProfileScreen.route)
                        }
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape),
                            model = photoUrl,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Color.White,
                onClick = {
                    viewModel.showDialog()
                }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    tint = Color.Black,
                    contentDescription = "Add"
                )
            }
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            modifier = Modifier
                .background(color = Color.Black)
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(10.dp),
            columns = GridCells.Fixed(countColumn),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            items(themes) { section ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ItemBrainCard(
                        progress = section.progress,
                        onClick = {
                            navController.navigateSafeArgs(
                                Screens.WordsScreen.route,
                                section.name
                            )
                        },
                        onLongClick = {
                            viewModel.deleteSection(name = section.name)
                        }
                    )
                    Text(text = section.name.uppercase(), color = Color.White)
                }
            }
        }

        if (showDialog) AlertDialog(
            onDismissRequest = {
                viewModel.dismissDialog()
            },
            text = {
                OutlineTextField(
                    modifier = Modifier
                        .onGloballyPositioned {
                            focusRequester.requestFocus()
                        }
                        .focusRequester(focusRequester)
                        .fillMaxWidth(),
                    value = name,
                    imeAction = ImeAction.Go,
                    textLabel = stringResource(R.string.theme_name),
                    focusManager = focusManager,
                    keyboardActions = { value -> viewModel.setName(name = value) },
                    onValueChange = { value -> viewModel.setName(name = value) },
                    trailingIcon = {
                        if (name.isNotEmpty()) IconButton(
                            onClick = { viewModel.setName("") }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = Color.White
                            )
                        }
                    }
                )
            },
            confirmButton = {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = name.isNotEmpty(),
                    onClick = {
                        viewModel.addSection(name)
                        viewModel.dismissDialog()
                        focusManager.clearFocus()
                    },
                    containerColor = Color.Black.copy(alpha = 0.32f)
                ) {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = stringResource(R.string.add_button)
                    )
                }
            },
            containerColor = GrayBlack,
            textContentColor = GrayBlack,
            shape = ShapeDefaults.Large,
        )
    }
}