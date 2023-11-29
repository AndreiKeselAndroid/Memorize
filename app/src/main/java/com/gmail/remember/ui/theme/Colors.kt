package com.gmail.remember.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


internal val BlueGray = Color(0xFFCFD8DC)

internal val BlueGrayTwo = Color(0xFFE0E5E7)

internal val GrayBlackTwo = Color(0xFF1F1E1E)
internal val White
    @Composable get() = if (isSystemInDarkTheme())Color(0xFFFEFEFE) else GrayBlackTwo
internal val GraphiteBlack
    @Composable get() = if (isSystemInDarkTheme()) Color(0xFF1F1C17) else BlueGray
internal val BlackBrown
    @Composable get() = if (isSystemInDarkTheme()) Color(0xFF292421) else BlueGrayTwo
internal val UmberGray
    @Composable get() = if (isSystemInDarkTheme()) Color(0xFF342F2B) else BlueGrayTwo

internal val GrayishOrange
    @Composable get() = if (isSystemInDarkTheme()) Color(0xFFC0A78B) else GrayBlackTwo
