package com.example.projeto27_03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.example.projeto27_03.data.PreferencesManager
import com.example.projeto27_03.ui.screen.LoginScreen
import com.example.projeto27_03.ui.screen.OrderScreen
import com.example.projeto27_03.ui.theme.Projeto27_03Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // PreferencesManager centraliza o acesso ao DataStore do app.
        // Agora ele usa DataStore, que é assíncrono e expõe Flow para a UI acompanhar mudanças.
        val preferencesManager = PreferencesManager(this)

        setContent {
            // Coletamos os Flows do DataStore para que a UI reflita qualquer alteração persistida.
            val isLogged by preferencesManager.loginStateFlow.collectAsState(initial = false)
            val savedUserType by preferencesManager.userTypeStateFlow.collectAsState(initial = "")
            val scope = rememberCoroutineScope()

            Projeto27_03Theme {
                if (isLogged) {
                    // Se já existe uma sessão salva, mostramos a tela principal.
                    // O tipo de usuário é exibido para reforçar o vínculo com o DataStore.
                    OrderScreen(
                        userType = savedUserType,
                        onLogout = {
                            // Ao sair, limpamos os dados persistidos e voltamos para o login.
                            scope.launch {
                                preferencesManager.clearSession()
                            }
                        }
                    )
                } else {
                    // Se não estiver logado, mostramos a tela de login didática.
                    LoginScreen(
                        onLoginClick = { userType ->
                            // Aqui aplicamos o conceito do print:
                            // 1) salvamos o tipo de usuário
                            // 2) marcamos a sessão como logada
                            scope.launch {
                                preferencesManager.saveUserTypeState(userType)
                                preferencesManager.saveLoginState(true)
                            }
                        }
                    )
                }
            }
        }
    }
}

