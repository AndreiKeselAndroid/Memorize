package com.gmail.remember.common.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.unit.dp
import com.gmail.remember.models.ThemeModel
import com.gmail.remember.ui.theme.GraphiteBlack
import com.gmail.remember.utils.brainAnimate
import com.gmail.remember.utils.drawBehind
import com.gmail.remember.utils.drawBrain

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("RestrictedApi")
@Composable
fun ItemBrainCard(
    themeModel: ThemeModel,
    progress: Float = 0f,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {

    val animation = remember {
        Animatable(1f)
    }

    LaunchedEffect(key1 = themeModel) {
        brainAnimate(animation = animation)
    }

    Surface(
        modifier = Modifier
            .padding(4.dp)
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
                    drawBehind(
                        progress = progress,
                        animation = animation,
                        isChecked = themeModel.isChecked
                    )
                }
        ) {
            progress.drawBrain(
                drawScope = this,
                animation = animation
            )
        }
    }
}