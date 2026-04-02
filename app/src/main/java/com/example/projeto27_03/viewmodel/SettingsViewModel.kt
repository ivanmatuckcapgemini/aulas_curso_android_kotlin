package com.example.projeto27_03.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projeto27_03.data.PreferencesManager
import com.example.projeto27_03.data.ThemeMode
import com.example.projeto27_03.ui.theme.ColorProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// ViewModel responsável por isolar a lógica de tema da camada de UI.
// Ele conversa com o PreferencesManager e expõe o estado salvo e o estado em edição.
class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesManager = PreferencesManager(application)

    private val savedThemeMode: StateFlow<ThemeMode> = preferencesManager.themeModeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ThemeMode.SYSTEM
    )

    private val savedColorProfile: StateFlow<ColorProfile> = preferencesManager.colorProfileFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ColorProfile.OCEAN
    )

    private val pendingThemeMode = MutableStateFlow(savedThemeMode.value)
    private val pendingColorProfile = MutableStateFlow(savedColorProfile.value)

    init {
        viewModelScope.launch {
            savedThemeMode.collect { pendingThemeMode.value = it }
        }
        viewModelScope.launch {
            savedColorProfile.collect { pendingColorProfile.value = it }
        }
    }

    val settingsUiState: StateFlow<SettingsUiState> = combine(
        savedThemeMode,
        savedColorProfile,
        pendingThemeMode,
        pendingColorProfile
    ) { savedTheme, savedProfile, selectedTheme, selectedProfile ->
        SettingsUiState(
            savedThemeMode = savedTheme,
            savedColorProfile = savedProfile,
            selectedThemeMode = selectedTheme,
            selectedColorProfile = selectedProfile,
            hasThemeChanges = savedTheme != selectedTheme,
            hasColorProfileChanges = savedProfile != selectedProfile
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsUiState(
            savedThemeMode = ThemeMode.SYSTEM,
            savedColorProfile = ColorProfile.OCEAN,
            selectedThemeMode = ThemeMode.SYSTEM,
            selectedColorProfile = ColorProfile.OCEAN,
            hasThemeChanges = false,
            hasColorProfileChanges = false
        )
    )

    fun onThemeSelected(theme: ThemeMode) {
        pendingThemeMode.value = theme
    }

    fun onColorProfileSelected(colorProfile: ColorProfile) {
        pendingColorProfile.value = colorProfile
    }

    fun confirmThemeChanges() {
        val selectedTheme = pendingThemeMode.value
        if (savedThemeMode.value == selectedTheme) return

        viewModelScope.launch {
            preferencesManager.saveTheme(selectedTheme)
        }
    }

    fun confirmColorProfileChanges() {
        val selectedColorProfile = pendingColorProfile.value
        if (savedColorProfile.value == selectedColorProfile) return

        viewModelScope.launch {
            preferencesManager.saveColorProfile(selectedColorProfile)
        }
    }
}

// Estado da tela de configurações, separando os valores salvos dos valores em edição.
data class SettingsUiState(
    val savedThemeMode: ThemeMode,
    val savedColorProfile: ColorProfile,
    val selectedThemeMode: ThemeMode,
    val selectedColorProfile: ColorProfile,
    val hasThemeChanges: Boolean,
    val hasColorProfileChanges: Boolean
)


