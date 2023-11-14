package com.gmail.remember.common.components

import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gmail.remember.R
import com.gmail.remember.models.RememberWordModel
import com.gmail.remember.ui.theme.GrayBlack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemRememberCard(
    model: RememberWordModel = RememberWordModel(),
    enableMultiSelect: Boolean = false,
    onLongClick: (RememberWordModel) -> Unit = {},
    onClick: (RememberWordModel) -> Unit = {}
) {
    var isEnglish by remember { mutableStateOf(false) }
    var value by remember { mutableStateOf(0f) }
    val applicationContext = LocalContext.current.applicationContext
    val scope = rememberCoroutineScope()
    val angle by animateFloatAsState(
        targetValue = value,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label = ""
    ) { values ->
        isEnglish = values == 180f
        if (isEnglish && model.url.isNotEmpty()) scope.launch(Dispatchers.IO) {
            MediaPlayer().apply {
                reset()
                setDataSource(applicationContext, Uri.parse(model.url))
                prepare()
                start()
            }
        }
    }

    Card(modifier = Modifier
        .graphicsLayer { rotationX = angle }
        .padding(8.dp)
        .fillMaxWidth()
        .combinedClickable(
            onClick = {
                if (enableMultiSelect) onClick(model)
                else value = if (value == 0f) 180f else 0f
            },
            onLongClick = {
                onLongClick(model)
            }
        )
        .height(60.dp),
        shape = ShapeDefaults.Large
    ) {
        Row(
            modifier = Modifier
                .background(GrayBlack)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .graphicsLayer { rotationX = angle },
                textAlign = TextAlign.Start,
                text = if (isEnglish) model.wordEng else model.wordRu,
                color = Color.White,
                overflow = TextOverflow.Ellipsis,
            )

            if (model.isCheck) Image(
                modifier = Modifier
                    .graphicsLayer { rotationX = angle }
                    .padding(end = 16.dp),
                painter = painterResource(id = R.drawable.ic_check),
                colorFilter = ColorFilter.tint(Color.White),
                contentDescription = "Check"
            )
        }
    }
}