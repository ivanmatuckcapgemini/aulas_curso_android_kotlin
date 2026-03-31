package com.example.projeto27_03.viewmodel

// Representa os diferentes momentos do fluxo de login.
sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

