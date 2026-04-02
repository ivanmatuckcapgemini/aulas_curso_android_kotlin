package com.example.projeto27_03.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun Projeto27_03Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorProfile: ColorProfile = ColorProfile.OCEAN,
    content: @Composable () -> Unit
) {
    val colorScheme = colorProfile.colorScheme(darkTheme)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}