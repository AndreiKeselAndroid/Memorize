package com.gmail.remember.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun RememberTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content
    )
}