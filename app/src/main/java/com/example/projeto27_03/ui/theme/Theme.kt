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
    // O esquema final do Material3 vem do perfil de cor escolhido, combinado com a variação clara/escura.
    val colorScheme = colorProfile.colorScheme(darkTheme)

    // MaterialTheme aplica a identidade visual base do app para toda a árvore de composição.
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}