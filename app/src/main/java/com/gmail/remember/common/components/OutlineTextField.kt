package com.gmail.remember.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.gmail.remember.R
import com.gmail.remember.models.LevelModel
import com.gmail.remember.models.TimeModel
import com.gmail.remember.ui.theme.BlackBrown
import com.gmail.remember.ui.theme.GraphiteBlack
import com.gmail.remember.ui.theme.GrayishOrange

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
    focusedBorderColor: Color = GrayishOrange,
    unfocusedBorderColor: Color = GrayishOrange.copy(alpha = 0.32f),
    focusedLabelColor: Color = GrayishOrange,
    unfocusedLabelColor: Color = GrayishOrange.copy(alpha = 0.32f),
    disabledBorderColor: Color = GrayishOrange.copy(alpha = 0.32f),
    disabledTextColor: Color = GrayishOrange,
    textColor: Color = GrayishOrange,
    cursorColor: Color = GrayishOrange,
    focusManager: FocusManager? = null,
    list: List<Any> = emptyList(),
    expandTime: MutableState<Boolean> = mutableStateOf(false),
    onDismissDropdownMenu: () -> Unit = {},
    onClickItemMenu: (Any) -> Unit = {},
    enabled: Boolean = true
) {

    val currentValue by rememberUpdatedState(newValue = value)
    val state = rememberScrollState()
    val displayMetrics = LocalContext.current.resources.displayMetrics
    val screenHeightDp = displayMetrics.heightPixels / displayMetrics.density
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by expandTime

    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                }
                .padding(horizontal = 4.dp)
                .fillMaxWidth(),
            value = currentValue,
            onValueChange = { text -> onValueChange(text) },
            label = {
                Text(text = textLabel)
            },
            enabled = enabled,
            singleLine = singleLine,
            shape = ShapeDefaults.Medium,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onGo = {
                    focusManager?.clearFocus()
                    keyboardActions(value)
                },
                onNext = {
                    focusManager?.moveFocus(FocusDirection.Down)
                    keyboardActions(value)
                }
            ) {
                keyboardActions(value)
                focusManager?.clearFocus()
            },
            trailingIcon = {
                trailingIcon()
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = focusedBorderColor,
                unfocusedBorderColor = unfocusedBorderColor,
                focusedLabelColor = focusedLabelColor,
                unfocusedLabelColor = unfocusedLabelColor,
                disabledBorderColor = disabledBorderColor,
                disabledTextColor = disabledTextColor,
                textColor = textColor,
                cursorColor = cursorColor
            ),
        )

        if (list.isNotEmpty())
            DropdownMenu(
                modifier = Modifier
                    .background(GraphiteBlack)
                    .verticalScroll(state)
                    .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                    .heightIn(max = with(LocalDensity.current) { screenHeightDp.toDp() }),
                offset = DpOffset(x = 0.dp, y = 8.dp),
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                    onDismissDropdownMenu()
                },
            ) {
                list.forEachIndexed { index, model ->
                    Column {
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                                onClickItemMenu(model)
                            },
                            text = {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = when (model) {
                                        is TimeModel -> model.time
                                        is LevelModel -> model.name
                                        else -> ""
                                    },
                                    color = GrayishOrange,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Normal
                                )
                            },
                            trailingIcon = {
                                if (when (model) {
                                        is TimeModel -> model.isCheck
                                        is LevelModel -> model.check
                                        else -> false
                                    }
                                ) Image(
                                    painter = painterResource(id = R.drawable.ic_check),
                                    colorFilter = ColorFilter.tint(GrayishOrange),
                                    contentDescription = "Ok"
                                )
                            }
                        )
                        if (list.size.minus(1) != index)
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                color = BlackBrown.copy(0.32f)
                            )
                    }
                }
            }
    }
}