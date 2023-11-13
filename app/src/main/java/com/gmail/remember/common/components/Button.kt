package com.gmail.remember.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gmail.remember.ui.theme.GrayBlack

@Composable
fun Button(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    defaultElevation: Dp = 0.dp,
    onClick: () -> Unit = {},
    shape: CornerBasedShape = ShapeDefaults.Medium,
    contentColor: Color = Color.White,
    containerColor: Color = GrayBlack,
    disabledContentColor: Color = contentColor.copy(alpha = 0.32f),
    disabledContainerColor: Color = containerColor.copy(alpha = 0.32f),
    content: @Composable () -> Unit = {}
) {
    androidx.compose.material3.Button(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        enabled = enabled,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = defaultElevation,
            pressedElevation = defaultElevation,
            disabledElevation = defaultElevation,
            hoveredElevation = defaultElevation,
            focusedElevation = defaultElevation
        ),
        onClick = {
            onClick()
        },
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            contentColor = contentColor,
            containerColor = containerColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
        )
    ) {
        content()
    }
}