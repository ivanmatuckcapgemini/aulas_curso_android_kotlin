package com.example.projeto27_03.data

// Enum para representar o tema escolhido, mantendo o DataStore com String.
// O storageValue garante que a persistencia use apenas valores conhecidos.
enum class ThemeMode(val storageValue: String) {
    // O tema claro força uma aparência iluminada independente do sistema.
    LIGHT("LIGHT"),
    // O tema escuro força a versão noturna do Material3.
    DARK("DARK"),
    // O modo sistema respeita a preferência global do aparelho.
    SYSTEM("SYSTEM");

    // Converte a opção escolhida em um valor booleano prático para o MaterialTheme.
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

