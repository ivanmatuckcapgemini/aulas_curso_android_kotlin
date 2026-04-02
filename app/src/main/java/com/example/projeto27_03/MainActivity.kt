package com.example.projeto27_03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projeto27_03.data.PreferencesManager
import com.example.projeto27_03.ui.screen.LoginScreen
import com.example.projeto27_03.ui.screen.OrderScreen
import com.example.projeto27_03.ui.screen.SettingsScreen
import com.example.projeto27_03.ui.theme.Projeto27_03Theme
import com.example.projeto27_03.viewmodel.LoginUiState
import com.example.projeto27_03.viewmodel.LoginViewModel
import com.example.projeto27_03.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // PreferencesManager continua cuidando do perfil do usuário (admin/cliente).
        // Já o token de sessão fica no LoginViewModel/DataStoreManager, evitando misturar
        // responsabilidades de autenticação com preferências pedagógicas da tela.
        val preferencesManager = PreferencesManager(this)

        setContent {
            // O ViewModel de login simula a autenticação e persiste o token em DataStore.
            // Ao observar o token como State, a tela muda automaticamente quando o login termina.
            val loginViewModel: LoginViewModel = viewModel()
            val sessionToken by loginViewModel.token.collectAsState()
            val loginUiState by loginViewModel.uiState.collectAsState()

            // Mantemos o tipo de usuário em uma preferência separada para reforçar o exercício
            // sobre saveUserTypeState e leitura reativa via Flow.
            val savedUserType by preferencesManager.userTypeStateFlow.collectAsState(initial = "")
            val settingsViewModel: SettingsViewModel = viewModel()
            val settingsUiState by settingsViewModel.settingsUiState.collectAsState()
            val scope = rememberCoroutineScope()
            var showSettings by remember { mutableStateOf(false) }

            // O token é a fonte de verdade para saber se existe sessão ativa.
            val isLogged = !sessionToken.isNullOrBlank()
            val isLoginLoading = loginUiState is LoginUiState.Loading
            val loginErrorMessage = (loginUiState as? LoginUiState.Error)?.message

            Projeto27_03Theme(
                darkTheme = settingsUiState.savedThemeMode.effectiveDarkTheme(isSystemInDarkTheme()),
                colorProfile = settingsUiState.savedColorProfile
            ) {
                if (isLogged) {
                    // Se já existe uma sessão salva, mostramos a tela principal.
                    // O tipo de usuário é exibido para reforçar o vínculo com o DataStore.
                    if (showSettings) {
                        SettingsScreen(
                            onBack = { showSettings = false }
                        )
                    } else {
                        OrderScreen(
                            userType = savedUserType,
                            onLogout = {
                                // Ao sair, limpamos os dados persistidos e voltamos para o login.
                                scope.launch {
                                    preferencesManager.clearSession()
                                    loginViewModel.logout()
                                }
                            },
                            onOpenSettings = { showSettings = true }
                        )
                    }
                } else {
                    // Se não estiver logado, mostramos a tela de login didática.
                    LoginScreen(
                        isLoading = isLoginLoading,
                        errorMessage = loginErrorMessage,
                        onLoginClick = { userType ->
                            // Aqui aplicamos o conceito do print e do esquema de token:
                            // 1) salvamos o tipo de usuário para manter o estudo do estado local
                            // 2) executamos o login simulado que grava o token no DataStore
                            scope.launch {
                                preferencesManager.saveUserTypeState(userType)
                                loginViewModel.login("user", "password")
                            }
                        }
                    )
                }
            }
        }
    }
}

