package com.gmail.remember.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

internal val Green = Color(0xFF34A853)

internal val Blue = Color(0xFFA9D7E4)

internal val Braun = Color(0xFFEA8068)

internal val Yellow = Color(0xFFFCC934)
internal val GraphiteBlack
    @Composable get() = if (isSystemInDarkTheme()) Color(0xFF1A1208)
    else Color(0xFFFDFDFD)

internal val BlackBrown
    @Composable get() = if (isSystemInDarkTheme()) Color(0xFF47351F)
    else Color(0xFFD5CEC8)


internal val GrayishOrange
    @Composable get() = if (isSystemInDarkTheme()) Color(0xFFFFDCBE)
    else Color(0xFF58442C)