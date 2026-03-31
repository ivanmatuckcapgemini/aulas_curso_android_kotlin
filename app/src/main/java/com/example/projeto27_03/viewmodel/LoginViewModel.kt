package com.example.projeto27_03.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projeto27_03.data.AuthRepository
import com.example.projeto27_03.data.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// ViewModel responsável por coordenar a autenticação.
// Ele conversa com a fonte de login (AuthRepository) e com a persistência local (DataStoreManager).
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository()
    private val dataStoreManager = DataStoreManager(application)

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()

    // O token é observado como Flow para que a UI reaja automaticamente quando o login ou logout acontecer.
    val token = dataStoreManager.getToken().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            val result = authRepository.login(username, password)

            result.onSuccess { token ->
                dataStoreManager.saveToken(token)
                _uiState.value = LoginUiState.Success
            }.onFailure { exception ->
                _uiState.value = LoginUiState.Error(
                    exception.message ?: "Erro desconhecido"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            dataStoreManager.clearToken()
            _uiState.value = LoginUiState.Idle
        }
    }
}

