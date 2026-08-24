package com.wasteflow.ecocycle.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val EcoColorScheme = lightColorScheme(
    primary = EcoPrimary,
    onPrimary = EcoWhite,
    primaryContainer = EcoPrimaryContainer,
    onPrimaryContainer = EcoOnPrimaryContainer,
    inversePrimary = EcoPrimaryFixedDim,
    secondary = EcoTertiary,
    onSecondary = EcoWhite,
    secondaryContainer = EcoSecondaryContainer,
    onSecondaryContainer = EcoOnSurface,
    tertiary = EcoTertiary,
    onTertiary = EcoWhite,
    tertiaryContainer = EcoTertiaryContainer,
    onTertiaryContainer = EcoOnSurface,
    background = EcoSurface,
    onBackground = EcoOnSurface,
    surface = EcoSurface,
    onSurface = EcoOnSurface,
    surfaceVariant = EcoSurfaceContainerHighest,
    onSurfaceVariant = EcoOnSurfaceVariant,
    surfaceContainer = EcoSurfaceContainer,
    surfaceContainerHigh = EcoSurfaceContainerHigh,
    surfaceContainerHighest = EcoSurfaceContainerHighest,
    surfaceContainerLow = EcoSurfaceContainerLow,
    surfaceContainerLowest = EcoSurfaceContainerLowest,
    error = EcoError,
    onError = EcoWhite,
    errorContainer = EcoErrorContainer,
    onErrorContainer = EcoOnErrorContainer,
    outline = EcoOutline,
    outlineVariant = EcoOutlineVariant
)

@Composable
fun WasteFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = EcoColorScheme,
        typography = Typography,
        content = content
    )
}
