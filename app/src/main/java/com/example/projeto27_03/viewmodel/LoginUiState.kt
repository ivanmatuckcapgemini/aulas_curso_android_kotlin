package com.example.projeto27_03.viewmodel

// Representa os diferentes momentos do fluxo de login.
// Usar um `sealed class` deixa o estado explícito e ajuda a evitar combinações inválidas.
sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

