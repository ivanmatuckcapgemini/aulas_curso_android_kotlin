package com.example.projeto27_03.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.projeto27_03.R

@Composable
fun LoginScreen(
    isLoading: Boolean,
    errorMessage: String?,
    onLoginClick: (String) -> Unit
) {
    // A tela é rolável para manter usabilidade em aparelhos menores e quando o teclado abrir.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Título pedagógico da tela.
        Text(text = stringResource(R.string.login_title))

        // Explica ao aluno qual comportamento deve ser observado neste fluxo.
        Text(text = stringResource(R.string.login_instructions))

        // Em vez de uma entrada textual livre, usamos botões fixos para reforçar o conceito
        // de seleção de perfil e persistência de estado simples.
        Text(text = stringResource(R.string.login_profile_prompt))

        // Enquanto a autenticação simulada acontece, mostramos um indicador de progresso.
        if (isLoading) {
            CircularProgressIndicator()
            Text(text = "Validando login... aguarde um instante.")
        }

        // Se algo der errado no login, mostramos a mensagem para o aluno enxergar o fluxo completo.
        if (!errorMessage.isNullOrBlank()) {
            Text(text = errorMessage)
        }

        // Cada botão dispara o mesmo fluxo de autenticação, mas com perfis diferentes.
        Button(
            onClick = { onLoginClick("admin") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(text = stringResource(R.string.admin_profile_button))
        }

        Button(
            onClick = { onLoginClick("cliente") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(text = stringResource(R.string.client_profile_button))
        }

        // Não usamos botão extra de confirmação porque a escolha já é a própria ação de login.
        Spacer(modifier = Modifier.height(4.dp))
    }
}



