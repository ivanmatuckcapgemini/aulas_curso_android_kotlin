package com.example.projeto27_03.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projeto27_03.data.PreferencesManager
import com.example.projeto27_03.data.ThemeMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// ViewModel responsável por isolar a lógica de tema da camada de UI.
// Ele conversa com o PreferencesManager e expõe o tema como StateFlow.
class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesManager = PreferencesManager(application)

    // O tema é lido do DataStore via Flow e transformado em StateFlow para a UI.
    // Também entregamos a preferencia já mapeada para a escolha de tema.
    val themeUiState: StateFlow<ThemeUiState> = preferencesManager.themeModeFlow
        .map { mode ->
            ThemeUiState(
                mode = mode,
                useDarkTheme = when (mode) {
                    ThemeMode.LIGHT -> false
                    ThemeMode.DARK -> true
                    ThemeMode.SYSTEM -> null
                }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeUiState(ThemeMode.SYSTEM, null)
        )

    // Salva o tema escolhido pelo usuário.
    // A UI apenas dispara a ação, sem conhecer detalhes de persistência.
    fun onThemeSelected(theme: ThemeMode) {
        viewModelScope.launch {
            preferencesManager.saveTheme(theme)
        }
    }
}

// Estado simples que a UI consome para aplicar o tema atual.
data class ThemeUiState(
    val mode: ThemeMode,
    val useDarkTheme: Boolean?
)


