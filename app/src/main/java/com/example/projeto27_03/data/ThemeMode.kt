package com.example.projeto27_03.data

// Enum para representar o tema escolhido, mantendo o DataStore com String.
// O storageValue garante que a persistencia use apenas valores conhecidos.
enum class ThemeMode(val storageValue: String) {
    LIGHT("LIGHT"),
    DARK("DARK"),
    SYSTEM("SYSTEM");

    fun effectiveDarkTheme(systemDarkTheme: Boolean): Boolean {
        return when (this) {
            LIGHT -> false
            DARK -> true
            SYSTEM -> systemDarkTheme
        }
    }

    companion object {
        fun fromStorage(value: String): ThemeMode {
            return ThemeMode.entries.firstOrNull { it.storageValue == value } ?: SYSTEM
        }
    }
}

