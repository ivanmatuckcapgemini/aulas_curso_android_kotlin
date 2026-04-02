package com.example.projeto27_03.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projeto27_03.R
import com.example.projeto27_03.data.ThemeMode
import com.example.projeto27_03.ui.theme.ColorProfile
import com.example.projeto27_03.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel()
) {
    // A tela trabalha com estado reativo: sempre que o usuário muda uma opção,
    // a prévia e os botões de confirmação respondem imediatamente.
    val uiState by viewModel.settingsUiState.collectAsState()
    // O tema da própria tela de configurações acompanha a seleção em edição.
    val previewDarkTheme = uiState.selectedThemeMode.effectiveDarkTheme(isSystemInDarkTheme())
    val previewColorScheme = uiState.selectedColorProfile.colorScheme(previewDarkTheme)
    val typography = MaterialTheme.typography

    // Envolvemos o conteúdo em MaterialTheme para que a prévia mostre o perfil
    // de cores escolhido sem precisar navegar para outra tela.
    MaterialTheme(
        colorScheme = previewColorScheme,
        typography = typography
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.headlineSmall
            )
            // Explica o objetivo da tela para o aluno entender que aqui há duas decisões separadas.
            Text(text = stringResource(R.string.settings_subtitle))

            // Seção responsável apenas pelo modo claro/escuro/sistema.
            Text(
                text = stringResource(R.string.settings_theme_section_title),
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = stringResource(R.string.settings_theme_section_subtitle))

            // Cada card representa uma escolha exclusiva de tema.
            ThemeModeOptionRow(
                label = stringResource(R.string.settings_light),
                isSelected = uiState.selectedThemeMode == ThemeMode.LIGHT,
                onSelect = { viewModel.onThemeSelected(ThemeMode.LIGHT) }
            )
            ThemeModeOptionRow(
                label = stringResource(R.string.settings_dark),
                isSelected = uiState.selectedThemeMode == ThemeMode.DARK,
                onSelect = { viewModel.onThemeSelected(ThemeMode.DARK) }
            )
            ThemeModeOptionRow(
                label = stringResource(R.string.settings_system),
                isSelected = uiState.selectedThemeMode == ThemeMode.SYSTEM,
                onSelect = { viewModel.onThemeSelected(ThemeMode.SYSTEM) }
            )

            // A confirmação do tema só fica habilitada quando há diferença entre o salvo
            // e o que está sendo editado na tela.
            Button(
                onClick = { viewModel.confirmThemeChanges() },
                enabled = uiState.hasThemeChanges,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text(text = stringResource(R.string.settings_confirm_theme_button))
            }

            Text(
                text = stringResource(R.string.settings_color_profiles_title),
                style = MaterialTheme.typography.titleMedium
            )
            // Aqui o aluno escolhe apenas o perfil visual do Material3.
            Text(text = stringResource(R.string.settings_color_profiles_subtitle))

            // O conjunto abaixo mantém a seleção única por checkbox, mas com aparência de card.
            ColorProfileOptionRow(
                label = ColorProfile.OCEAN.displayName,
                isSelected = uiState.selectedColorProfile == ColorProfile.OCEAN,
                onSelect = { viewModel.onColorProfileSelected(ColorProfile.OCEAN) }
            )
            ColorProfileOptionRow(
                label = ColorProfile.SUNSET.displayName,
                isSelected = uiState.selectedColorProfile == ColorProfile.SUNSET,
                onSelect = { viewModel.onColorProfileSelected(ColorProfile.SUNSET) }
            )
            ColorProfileOptionRow(
                label = ColorProfile.FOREST.displayName,
                isSelected = uiState.selectedColorProfile == ColorProfile.FOREST,
                onSelect = { viewModel.onColorProfileSelected(ColorProfile.FOREST) }
            )
            ColorProfileOptionRow(
                label = ColorProfile.VIOLET.displayName,
                isSelected = uiState.selectedColorProfile == ColorProfile.VIOLET,
                onSelect = { viewModel.onColorProfileSelected(ColorProfile.VIOLET) }
            )

            // A grade em 4 colunas mostra a paleta do perfil selecionado em tempo real.
            Text(
                text = stringResource(R.string.settings_preview_title),
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = stringResource(R.string.settings_preview_subtitle))

            ThemePreviewGrid(colors = uiState.selectedColorProfile.previewColors(previewDarkTheme))

            Button(
                onClick = { viewModel.confirmColorProfileChanges() },
                enabled = uiState.hasColorProfileChanges,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                // Este botão confirma apenas o perfil de cores, separadamente do tema.
                Text(text = stringResource(R.string.settings_confirm_color_profile_button))
            }

            // Espaço inferior extra para evitar que o botão final fique colado ao fim da tela.
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Text(text = stringResource(R.string.settings_back_button))
            }
        }
    }
}

@Composable
private fun ThemeModeOptionRow(
    label: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    // Card clicável torna a seleção mais evidente e confortável em telas pequenas.
    Card(
        onClick = onSelect,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                // O checkbox reforça visualmente a seleção única da opção escolhida.
                checked = isSelected,
                onCheckedChange = { checked ->
                    if (checked) onSelect()
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun ColorProfileOptionRow(
    label: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    // A lógica é a mesma do tema, mas com cor secundária para diferenciar visualmente a seção.
    Card(
        onClick = onSelect,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                // Mantemos o padrão de interação por toque em toda a linha do card.
                checked = isSelected,
                onCheckedChange = { checked ->
                    if (checked) onSelect()
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun ThemePreviewGrid(
    colors: List<Color>
) {
    // A prévia é quebrada em linhas de quatro círculos para atender ao layout pedido.
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        colors.chunked(4).forEach { rowColors ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowColors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                    )
                }
                repeat(4 - rowColors.size) {
                    Spacer(modifier = Modifier.size(44.dp))
                }
            }
        }
    }
}


