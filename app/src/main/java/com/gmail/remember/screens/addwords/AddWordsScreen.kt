package com.gmail.remember.screens.addwords

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.gmail.remember.R
import com.gmail.remember.common.components.Button
import com.gmail.remember.common.components.OutlineTextField
import com.gmail.remember.ui.theme.BlackBrown
import com.gmail.remember.ui.theme.GraphiteBlack
import com.gmail.remember.ui.theme.GrayishOrange
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
internal fun AddWordsScreen(
    navController: NavHostController,
    viewModel: AddWordsViewModel = hiltViewModel()
) {

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val enWord by viewModel.enWord.collectAsState()
    val ruWord by viewModel.ruWord.collectAsState()
    val childName by viewModel.childName.collectAsState()
    val enableButton by viewModel.enableButton.collectAsState()
    val messageSuccess = stringResource(R.string.add_word_success)
    val messageNotSuccess = stringResource(R.string.not_add_word)

    LaunchedEffect(viewModel) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Scaffold(
        modifier = Modifier
            .background(color = GraphiteBlack)
            .fillMaxSize()
            .imePadding()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
                    }
                )
            },
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
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            fontWeight = FontWeight.Medium,
                            text = stringResource(id = R.string.add)
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
            Box(
                modifier = Modifier
                    .background(GraphiteBlack)
                    .fillMaxWidth()
            ) {
                Button(
                    modifier = Modifier.padding(bottom = 24.dp, start = 4.dp, end = 4.dp),
                    enabled = enableButton,
                    onClick = {
                        viewModel.clickAdd(
                            enWord = enWord,
                            ruWord = ruWord,
                            childName = childName
                        ) { task ->
                            scope.launch {
                                snackBarHostState
                                    .showSnackbar(
                                        message = if (task.isSuccessful) messageSuccess else messageNotSuccess,
                                        duration = SnackbarDuration.Short,
                                        withDismissAction = true
                                    )
                            }
                            if (task.isSuccessful) focusManager.moveFocus(FocusDirection.Up)
                        }
                    }
                ) {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = stringResource(R.string.add_button)
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState
            ) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = BlackBrown,
                    contentColor = GrayishOrange
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(color = GraphiteBlack)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            OutlineTextField(
                modifier = Modifier
                    .padding(top = 12.dp, start = 4.dp, end = 4.dp)
                    .focusRequester(focusRequester),
                value = enWord,
                textLabel = stringResource(R.string.english),
                focusManager = focusManager,
                keyboardActions = { value -> viewModel.setEnWord(value = value) },
                onValueChange = { value -> viewModel.setEnWord(value = value) },
                trailingIcon = {
                    if (enWord.isNotEmpty()) IconButton(
                        onClick = { viewModel.setEnWord("") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = GrayishOrange.copy(alpha = 0.32f)
                        )
                    }
                }
            )

            OutlineTextField(
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp),
                value = ruWord,
                imeAction = ImeAction.Go,
                textLabel = stringResource(R.string.russian),
                focusManager = focusManager,
                keyboardActions = { value -> viewModel.setRuWord(value = value) },
                onValueChange = { value -> viewModel.setRuWord(value = value) },
                trailingIcon = {
                    if (ruWord.isNotEmpty()) IconButton(
                        onClick = { viewModel.setRuWord("") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = GraphiteBlack.copy(alpha = 0.32f)
                        )
                    }
                }
            )
        }
    }
}