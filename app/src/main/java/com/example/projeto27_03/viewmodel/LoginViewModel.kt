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

    // Cada dependência é mantida aqui para deixar o ViewModel responsável pela orquestração
    // do fluxo, e não pela implementação detalhada de autenticação ou persistência.
    private val authRepository = AuthRepository()
    private val dataStoreManager = DataStoreManager(application)

    // Estado transitório da tela: enquanto o login acontece, a UI mostra loading.
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
            // Primeiro avisamos a UI que a operação começou.
            _uiState.value = LoginUiState.Loading

            // Depois delegamos a validação para o repositório.
            val result = authRepository.login(username, password)

            result.onSuccess { token ->
                // Sucesso: persistimos o token e liberamos a UI para avançar.
                dataStoreManager.saveToken(token)
                _uiState.value = LoginUiState.Success
            }.onFailure { exception ->
                // Falha: armazenamos a mensagem para exibição no layout.
                _uiState.value = LoginUiState.Error(
                    exception.message ?: "Erro desconhecido"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            // Logout aqui significa limpar o token e voltar o estado visual para o início.
            dataStoreManager.clearToken()
            _uiState.value = LoginUiState.Idle
        }
    }
}

