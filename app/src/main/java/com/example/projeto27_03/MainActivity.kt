package com.example.projeto27_03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.projeto27_03.data.PreferencesManager
import com.example.projeto27_03.ui.screen.LoginScreen
import com.example.projeto27_03.ui.screen.OrderScreen
import com.example.projeto27_03.ui.theme.Projeto27_03Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // PreferencesManager centraliza o acesso ao SharedPreferences do app.
        // Criamos uma instância aqui porque o contexto da Activity está disponível.
        val preferencesManager = PreferencesManager(this)

        setContent {
            // Esses estados ficam na Activity/Compose e determinam qual tela será exibida.
            // No início, buscamos no armazenamento persistente se o usuário já estava logado.
            var isLogged by remember { mutableStateOf(preferencesManager.getLoginState()) }
            var savedUserType by remember { mutableStateOf(preferencesManager.getUserTypeState()) }

            Projeto27_03Theme {
                if (isLogged) {
                    // Se já existe uma sessão salva, mostramos a tela principal.
                    // O tipo de usuário é exibido para reforçar o vínculo com o SharedPreferences.
                    OrderScreen(
                        userType = savedUserType,
                        onLogout = {
                            // Ao sair, limpamos os dados persistidos e voltamos para o login.
                            preferencesManager.clearSession()
                            savedUserType = ""
                            isLogged = false
                        }
                    )
                } else {
                    // Se não estiver logado, mostramos a tela de login didática.
                    LoginScreen(
                        onLoginClick = { userType ->
                            // Aqui aplicamos o conceito do print:
                            // 1) salvamos o tipo de usuário
                            // 2) marcamos a sessão como logada
                            preferencesManager.saveUserTypeState(userType)
                            preferencesManager.saveLoginState(true)
                            savedUserType = userType
                            isLogged = true
                        }
                    )
                }
            }
        }
    }
}

