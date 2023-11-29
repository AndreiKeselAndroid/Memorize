package com.gmail.remember.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.gmail.remember.ui.theme.GrayishOrange
import com.gmail.remember.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlineTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    textLabel: String = "",
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: ((String) -> Unit) = {},
    onValueChange: ((String) -> Unit) = {},
    trailingIcon: (@Composable () -> Unit) = {},
    focusedBorderColor: Color = White,
    unfocusedBorderColor: Color = GrayishOrange.copy(alpha = 0.32f),
    focusedLabelColor: Color = White,
    unfocusedLabelColor: Color = GrayishOrange.copy(alpha = 0.32f),
    textColor: Color = White,
    cursorColor: Color = White,
    focusManager: FocusManager
) {
    val currentValue by rememberUpdatedState(newValue = value)

    OutlinedTextField(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        value = currentValue,
        onValueChange = { text -> onValueChange(text) },
        label = {
            Text(text = textLabel)
        },
        singleLine = singleLine,
        shape = ShapeDefaults.Medium,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onGo = {
                focusManager.clearFocus()
                keyboardActions(value)
            },
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
                keyboardActions(value)
            }
        ) {
            keyboardActions(value)
            focusManager.clearFocus()
        },
        trailingIcon = {
            trailingIcon()
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = focusedBorderColor,
            unfocusedBorderColor = unfocusedBorderColor,
            focusedLabelColor = focusedLabelColor,
            unfocusedLabelColor = unfocusedLabelColor,
            textColor = textColor,
            cursorColor = cursorColor
        ),
    )
}