package com.gmail.remember.common.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.remember.ui.theme.BlackBrown
import com.gmail.remember.ui.theme.GraphiteBlack
import com.gmail.remember.ui.theme.GrayishOrange

@Composable
fun Switch(
    checked: Boolean = true,
    onCheckedChange: (Boolean) -> Unit = {},
    checkedThumbColor: Color = GrayishOrange,
    checkedTrackColor: Color = BlackBrown,
    uncheckedThumbColor: Color = GrayishOrange.copy(alpha = 0.32f),
    uncheckedTrackColor: Color = BlackBrown,
    checkedBorderColor: Color = BlackBrown,
    uncheckedBorderColor: Color = BlackBrown,
    @StringRes text: Int? = null,
    textColor: Color = if (checked) GrayishOrange else GrayishOrange.copy(alpha = 0.32f)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (text != null) Text(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp, end = 16.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(color = GraphiteBlack.copy(alpha = 0.32f)),
                    onClick = {
                        onCheckedChange(checked.not())
                    }
                ),
            text = stringResource(text),
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            fontSize = 18.sp,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium
        )

        androidx.compose.material3.Switch(
            checked = checked,
            onCheckedChange = { value ->
                onCheckedChange(value)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = checkedThumbColor,
                checkedTrackColor = checkedTrackColor,
                uncheckedThumbColor = uncheckedThumbColor,
                uncheckedTrackColor = uncheckedTrackColor,
                checkedBorderColor = checkedBorderColor,
                uncheckedBorderColor = uncheckedBorderColor
            )
        )
    }
}