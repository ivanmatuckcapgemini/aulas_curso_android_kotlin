package com.example.projeto27_03.ui.screen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.verticalScroll
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
    val uiState by viewModel.settingsUiState.collectAsState()
    val previewDarkTheme = uiState.selectedThemeMode.effectiveDarkTheme(isSystemInDarkTheme())
    val previewColorScheme = uiState.selectedColorProfile.colorScheme(previewDarkTheme)
    val typography = MaterialTheme.typography

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
            Text(text = stringResource(R.string.settings_subtitle))

            Text(
                text = stringResource(R.string.settings_theme_section_title),
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = stringResource(R.string.settings_theme_section_subtitle))

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
            Text(text = stringResource(R.string.settings_color_profiles_subtitle))

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
                Text(text = stringResource(R.string.settings_confirm_color_profile_button))
            }

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


