package com.gmail.remember.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.remember.R
import com.gmail.remember.ui.theme.GraphiteBlack
import com.gmail.remember.ui.theme.GrayishOrange


@Composable
fun RadioButton(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
    checkedColor: Color = GrayishOrange,
    uncheckedColor: Color = GrayishOrange.copy(alpha = 0.32f),
    text: String? = null,
    textColor: Color = if (checked) GrayishOrange else GrayishOrange.copy(alpha = 0.32f)
) {
    Row(
        modifier = modifier
            .selectable(
                selected = checked,
                onClick = {
                    onCheckedChange(
                        checked.not()
                    )
                }
            )
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconToggleButton(
            checked = checked,
            onCheckedChange = { value ->
                onCheckedChange(value)
            })
        {
            Icon(
                painter = painterResource(
                    if (checked) R.drawable.checked_radio_button
                    else R.drawable.un_checked_radio_button
                ),
                tint = if (checked) checkedColor else uncheckedColor,
                contentDescription = "Radio button icon",
            )
        }

        if (text != null) Text(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(color = GraphiteBlack.copy(alpha = 0.32f)),
                    onClick = {
                        onCheckedChange(checked.not())
                    },
                ),
            text = text,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            color = textColor
        )
    }
}