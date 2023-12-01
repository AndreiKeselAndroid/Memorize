package com.gmail.remember.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

internal val Green = Color(0xFF34A853)

internal val Blue = Color(0xFFA9D7E4)

internal val Braun = Color(0xFFEA8068)

internal val Yellow = Color(0xFFFCC934)
internal val White
    @Composable get() = if (isSystemInDarkTheme()) Color(0xFFFFFBF8) else Color(0xFF342F2B)
internal val GraphiteBlack
    @Composable get() = if (isSystemInDarkTheme()) Color(0xFF1F1C17) else Color(0xFFF2EADF)
internal val BlackBrown
    @Composable get() = if (isSystemInDarkTheme()) Color(0xFF292421) else Color(0xFFFFFBF8)
internal val UmberGray
    @Composable get() = if (isSystemInDarkTheme()) Color(0xFF342F2B) else Color(0xFFFFFBF8)
internal val GrayishOrange
    @Composable get() = if (isSystemInDarkTheme()) Color(0xFFC0A78B) else Color(0xFF292421)
