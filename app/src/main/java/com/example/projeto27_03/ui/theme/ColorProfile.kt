package com.example.projeto27_03.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

enum class ColorProfile(
    val storageValue: String,
    val displayName: String,
    private val lightScheme: ColorScheme,
    private val darkScheme: ColorScheme
) {
    // Perfil inspirado em tons de oceano para um visual frio e estável.
    OCEAN(
        storageValue = "OCEAN",
        displayName = "Oceano",
        lightScheme = lightColorScheme(
            primary = Color(0xFF006A6A),
            secondary = Color(0xFF4B6262),
            tertiary = Color(0xFF4A5D8A),
            background = Color(0xFFF4FBFB),
            surface = Color(0xFFF4FBFB),
            surfaceVariant = Color(0xFFDCE5E5),
            outline = Color(0xFF6F7979),
            error = Color(0xFFBA1A1A)
        ),
        darkScheme = darkColorScheme(
            primary = Color(0xFF4CDADA),
            secondary = Color(0xFFB2CCCC),
            tertiary = Color(0xFFA8C7FF),
            background = Color(0xFF091516),
            surface = Color(0xFF091516),
            surfaceVariant = Color(0xFF3F4949),
            outline = Color(0xFF899393),
            error = Color(0xFFFFB4AB)
        )
    ),
    // Perfil mais quente, útil para contrastar com o tema anterior.
    SUNSET(
        storageValue = "SUNSET",
        displayName = "Pôr do sol",
        lightScheme = lightColorScheme(
            primary = Color(0xFFB3261E),
            secondary = Color(0xFF775651),
            tertiary = Color(0xFF8C4A2F),
            background = Color(0xFFFFF8F6),
            surface = Color(0xFFFFF8F6),
            surfaceVariant = Color(0xFFF3DDD8),
            outline = Color(0xFF85736F),
            error = Color(0xFFBA1A1A)
        ),
        darkScheme = darkColorScheme(
            primary = Color(0xFFFFB4A8),
            secondary = Color(0xFFE7BDB6),
            tertiary = Color(0xFFF4BA86),
            background = Color(0xFF1F100E),
            surface = Color(0xFF1F100E),
            surfaceVariant = Color(0xFF58413D),
            outline = Color(0xFFA08C88),
            error = Color(0xFFFFB4AB)
        )
    ),
    // Perfil esverdeado que ajuda a enxergar variações em tons naturais.
    FOREST(
        storageValue = "FOREST",
        displayName = "Floresta",
        lightScheme = lightColorScheme(
            primary = Color(0xFF2E7D32),
            secondary = Color(0xFF5A6E52),
            tertiary = Color(0xFF44624B),
            background = Color(0xFFF5FBF4),
            surface = Color(0xFFF5FBF4),
            surfaceVariant = Color(0xFFD8E4D4),
            outline = Color(0xFF6D7A67),
            error = Color(0xFFBA1A1A)
        ),
        darkScheme = darkColorScheme(
            primary = Color(0xFF8DDC8B),
            secondary = Color(0xFFBBCBB3),
            tertiary = Color(0xFF9BC4A0),
            background = Color(0xFF101510),
            surface = Color(0xFF101510),
            surfaceVariant = Color(0xFF3F4A3C),
            outline = Color(0xFF8A9485),
            error = Color(0xFFFFB4AB)
        )
    ),
    // Perfil roxo para demonstrar um terceiro conjunto visual bem distinto.
    VIOLET(
        storageValue = "VIOLET",
        displayName = "Violeta",
        lightScheme = lightColorScheme(
            primary = Color(0xFF6D3FD8),
            secondary = Color(0xFF645B8F),
            tertiary = Color(0xFF8C4DA8),
            background = Color(0xFFF9F6FF),
            surface = Color(0xFFF9F6FF),
            surfaceVariant = Color(0xFFE4DEF4),
            outline = Color(0xFF7D748C),
            error = Color(0xFFBA1A1A)
        ),
        darkScheme = darkColorScheme(
            primary = Color(0xFFD7BBFF),
            secondary = Color(0xFFCBBEE4),
            tertiary = Color(0xFFF0B6FF),
            background = Color(0xFF170F28),
            surface = Color(0xFF170F28),
            surfaceVariant = Color(0xFF4A3F5C),
            outline = Color(0xFF9689A7),
            error = Color(0xFFFFB4AB)
        )
    );

    fun colorScheme(darkTheme: Boolean): ColorScheme {
        // A composição escolhe entre a paleta clara ou escura do perfil ativo.
        return if (darkTheme) darkScheme else lightScheme
    }

    fun previewColors(darkTheme: Boolean): List<Color> {
        // A prévia destaca cores fundamentais do esquema para o aluno comparar rapidamente.
        val scheme = colorScheme(darkTheme)
        return listOf(
            scheme.primary,
            scheme.secondary,
            scheme.tertiary,
            scheme.error,
            scheme.background,
            scheme.surface,
            scheme.surfaceVariant,
            scheme.outline
        )
    }

    companion object {
        fun fromStorage(value: String): ColorProfile {
            // Se o valor salvo não for reconhecido, retornamos um padrão seguro.
            return entries.firstOrNull { it.storageValue == value } ?: OCEAN
        }
    }
}


