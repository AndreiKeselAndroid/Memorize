package com.gmail.remember.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Divider
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
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
import com.gmail.remember.ui.theme.BlackBrown
import com.gmail.remember.ui.theme.GraphiteBlack
import com.gmail.remember.ui.theme.GrayishOrange
import com.gmail.remember.ui.theme.UmberGray

@OptIn(ExperimentalMaterial3Api::class)
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
            .background(color = GraphiteBlack),
        topBar = {
            Column {
                TopAppBar(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = TopAppBarDefaults
                        .centerAlignedTopAppBarColors(
                            containerColor = GraphiteBlack,
                            titleContentColor = GrayishOrange
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
                    navigationIcon = {
                        Spacer(modifier = Modifier.size(56.0.dp))
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
                Divider(
                    thickness = 0.5.dp,
                    color = UmberGray
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = GrayishOrange,
                onClick = {
                    viewModel.showDialog()
                }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    tint = BlackBrown,
                    contentDescription = "Add"
                )
            }
        }
    ) { paddingValues ->

        LazyVerticalGrid(
            modifier = Modifier
                .background(color = GraphiteBlack)
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(10.dp),
            columns = GridCells.Fixed(countColumn),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            items(themes, key = { model ->
                model.name
            }) { theme ->
                Column(
                    modifier = Modifier
                        .background(GraphiteBlack),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(
                        modifier = Modifier
                            .height(8.dp)
                            .fillMaxWidth()
                            .background(color = GraphiteBlack)
                    )
                    ItemBrainCard(
                        themeModel = theme,
                        progress = theme.progress,
                        onClick = {
                            navController.navigateSafeArgs(
                                Screens.WordsScreen.route,
                                theme.name
                            )
                        },
                        onLongClick = {
                            viewModel.deleteSection(name = theme.name)
                        }
                    )
                    Text(
                        modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp),
                        text = "${(theme.progress * 100).toInt().toString().uppercase()}% ${theme.name.uppercase()}",
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        color = GrayishOrange
                    )
                }
            }
        }

        if (showDialog) AlertDialog(
            modifier = Modifier.clip(shape = ShapeDefaults.Medium),
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
                                tint = GrayishOrange.copy(alpha = 0.32f)
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
                        viewModel.addSection(name, themes)
                        viewModel.dismissDialog()
                        focusManager.clearFocus()
                    }
                ) {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = stringResource(R.string.add_button),
                    )
                }
            },
            containerColor = GraphiteBlack,
            textContentColor = GrayishOrange,
            shape = ShapeDefaults.Medium,
        )
    }
}