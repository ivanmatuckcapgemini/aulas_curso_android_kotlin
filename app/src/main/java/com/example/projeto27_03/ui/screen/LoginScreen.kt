package com.example.projeto27_03.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.projeto27_03.R

@Composable
fun LoginScreen(
    onLoginClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Título pedagógico da tela.
        Text(text = stringResource(R.string.login_title))

        // Texto explicando ao aluno o que precisa ser feito.
        Text(text = stringResource(R.string.login_instructions))

        // Nesta atividade, em vez de digitar o perfil, o usuário escolhe uma das opções.
        // Isso deixa o fluxo mais claro e mostra como salvar um valor fixo no SharedPreferences.
        Text(text = stringResource(R.string.login_profile_prompt))

        Button(
            onClick = { onLoginClick("admin") },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.admin_profile_button))
        }

        Button(
            onClick = { onLoginClick("cliente") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.client_profile_button))
        }

        // Não usamos um botão de confirmação separado porque cada opção já representa o login.
        // Ao tocar em um perfil, o estado é salvo e a tela principal é exibida.
    }
}



