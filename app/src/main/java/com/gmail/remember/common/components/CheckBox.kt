package com.gmail.remember.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.remember.ui.theme.GraphiteBlack
import com.gmail.remember.ui.theme.GrayishOrange

@Composable
fun CheckBox(
    modifier: Modifier = Modifier,
    checkBoxModifier: Modifier = Modifier,
    textModifier: Modifier = Modifier
        .padding(top = 8.dp, bottom = 8.dp),
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
    checkedColor: Color = GrayishOrange,
    uncheckedColor: Color = GrayishOrange.copy(alpha = 0.32f),
    checkmarkColor: Color = Color.White,
    text: String? = null,
    textColor: Color = if (checked) GrayishOrange else GrayishOrange.copy(alpha = 0.32f)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            modifier = checkBoxModifier,
            checked = checked,
            onCheckedChange = { value ->
                onCheckedChange(value)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = checkedColor,
                uncheckedColor = uncheckedColor,
                checkmarkColor = checkmarkColor
            )
        )

        if (text != null) Text(
            modifier = textModifier
                .clickable(
                    onClick = {
                        onCheckedChange(checked.not())
                    },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(color = GraphiteBlack.copy(alpha = 0.32f))
                ),
            text = text,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            color = textColor
        )
    }
}