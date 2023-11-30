package com.gmail.remember.common.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.unit.dp
import com.gmail.remember.ui.theme.GraphiteBlack
import com.gmail.remember.utils.brainAnimate
import com.gmail.remember.utils.drawBehind
import com.gmail.remember.utils.drawBrain

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("RestrictedApi")
@Composable
fun ItemBrainCard(
    name: String = "",
    progress: Float = 0f,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {

    val drawPathAnimation = remember {
        Animatable(1f)
    }

    LaunchedEffect(key1 = name) {
        brainAnimate(
            drawPathAnimation = drawPathAnimation
        )
    }

    Surface(
        modifier = Modifier
            .background(GraphiteBlack)
            .combinedClickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null,
                onClick = { onClick() },
                onLongClick = { onLongClick() }
            )
            .size(200.dp)
    ) {
        Canvas(
            modifier = Modifier
                .background(GraphiteBlack)
                .drawBehind {
                    drawBehind(drawPathAnimation = drawPathAnimation.value, progress = progress)
                }
        ) {
            progress.drawBrain(drawScope = this, drawPathAnimation = drawPathAnimation.value)
        }
    }
}