package com.example.projeto27_03.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projeto27_03.R
import com.example.projeto27_03.data.ThemeMode
import com.example.projeto27_03.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel()
) {
    // A UI apenas consome o estado pronto do ViewModel.
    // Qualquer mudança no DataStore atualiza a tela automaticamente.
    val themeUiState by viewModel.themeUiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = stringResource(R.string.settings_title))
        Text(text = stringResource(R.string.settings_subtitle))

        // Cada opção dispara um evento para o ViewModel, sem lógica de persistência na UI.
        ThemeOptionRow(
            label = stringResource(R.string.settings_light),
            isSelected = themeUiState.mode == ThemeMode.LIGHT,
            onSelect = { viewModel.onThemeSelected(ThemeMode.LIGHT) }
        )

        ThemeOptionRow(
            label = stringResource(R.string.settings_dark),
            isSelected = themeUiState.mode == ThemeMode.DARK,
            onSelect = { viewModel.onThemeSelected(ThemeMode.DARK) }
        )

        ThemeOptionRow(
            label = stringResource(R.string.settings_system),
            isSelected = themeUiState.mode == ThemeMode.SYSTEM,
            onSelect = { viewModel.onThemeSelected(ThemeMode.SYSTEM) }
        )

        // Este botão serve para evidenciar a mudança de cor no tema escolhido.
        // Como ele usa MaterialTheme, a cor acompanha o tema atual automaticamente.
        Button(
            onClick = {},
            enabled = false,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.primary,
                disabledContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.settings_example_button))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.settings_back_button))
        }
    }
}

@Composable
private fun ThemeOptionRow(
    label: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        RadioButton(
            selected = isSelected,
            onClick = onSelect
        )
    }
}



