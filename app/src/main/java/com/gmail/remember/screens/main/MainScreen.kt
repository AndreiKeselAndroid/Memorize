package com.gmail.remember.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.derivedStateOf
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.gmail.remember.R
import com.gmail.remember.common.components.Button
import com.gmail.remember.common.components.ItemBrainCard
import com.gmail.remember.common.components.OutlineTextField
import com.gmail.remember.data.api.models.Result
import com.gmail.remember.models.ThemeModel
import com.gmail.remember.navigation.Screens
import com.gmail.remember.navigation.navigateSafeArgs
import com.gmail.remember.ui.theme.BlackBrown
import com.gmail.remember.ui.theme.Blue
import com.gmail.remember.ui.theme.GraphiteBlack
import com.gmail.remember.ui.theme.GrayishOrange

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
    val stateUi by viewModel.stateUi.collectAsState()
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val displayMetrics = LocalContext.current.resources.displayMetrics
    val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
    val countColumn by remember { mutableStateOf((screenWidthDp / 200f + 0.5).toInt()) }
    val state = rememberLazyGridState()
    val compositionDumbbells by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.dumbbells)
    )
    val progressLottieDumbbells  by animateLottieCompositionAsState(
        compositionDumbbells,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
        speed = 2f,
        restartOnPlay = true
    )
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.loading_lottie)
    )
    val progressLottie by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
        speed = 2f,
        restartOnPlay = true
    )
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
                                }.value != 0) 0.dp else 24.dp,
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
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            fontWeight = FontWeight.Medium,
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
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FloatingActionButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(56.dp),
                    containerColor = Blue,
                    onClick = {
                        navController.navigate(Screens.TrainingWordsScreen.route)
                    }) {
                    LottieAnimation(
                        compositionDumbbells,
                        progressLottieDumbbells
                    )
                }
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
        }
    ) { paddingValues ->
        if (themes != null && themes?.isNotEmpty() == true) LazyVerticalGrid(
            state = state,
            modifier = Modifier
                .background(color = GraphiteBlack)
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 10.dp, top = 16.dp, start = 4.dp, end = 4.dp),
            columns = GridCells.Fixed(countColumn),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(themes!!, key = { model ->
                model.name
            }) { theme ->
                Column(
                    modifier = Modifier
                        .clip(ShapeDefaults.Large)
                        .background(BlackBrown.copy(0.22f)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
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
                            if (!theme.isChecked)
                                viewModel.deleteSection(name = theme.name)
                        }
                    )
                    Text(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        text = if (theme.progress > 0) "${
                            (theme.progress * 100).toInt().toString().uppercase()
                        }% ${theme.name.uppercase()}"
                        else theme.name.uppercase(),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        color = GrayishOrange
                    )
                }
            }
        } else if (themes?.isEmpty() == true) Box(
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
        else Box(
                modifier = Modifier
                    .background(color = GraphiteBlack)
                    .padding(20.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    composition,
                    progressLottie,
                    modifier = Modifier.padding(20.dp)
                )
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
                        viewModel.addSection(name, themes ?: emptyList())
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
    when (stateUi) {
        is Result.Success -> {
            viewModel.setThemes(
                themes = (stateUi as? Result.Success<List<ThemeModel>>)?.data ?: emptyList()
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

        else -> {
            Box(
                modifier = Modifier
                    .background(color = GraphiteBlack)
                    .padding(20.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    composition,
                    progressLottie,
                    modifier = Modifier.padding(20.dp)
                )
            }
        }
    }
}