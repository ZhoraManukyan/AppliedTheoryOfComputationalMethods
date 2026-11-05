package com.example.appliedtheoryofcomputationalmethods

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.appliedtheoryofcomputationalmethods.ui.FaddeevScreen
import com.example.appliedtheoryofcomputationalmethods.ui.LabMenuScreen
import com.example.appliedtheoryofcomputationalmethods.ui.LagrangeScreen
import com.example.appliedtheoryofcomputationalmethods.ui.NewtonRootScreen
import com.example.appliedtheoryofcomputationalmethods.ui.NewtonScreen
import com.example.appliedtheoryofcomputationalmethods.ui.TaylorScreen

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2563EB),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDBEAFE),
    onPrimaryContainer = Color(0xFF1E3A8A),
    secondary = Color(0xFF4F46E5),
    onSecondary = Color.White,
    background = Color(0xFFF5F7FA),
    onBackground = Color(0xFF111827),
    surface = Color.White,
    onSurface = Color(0xFF111827),
    surfaceVariant = Color(0xFFEEF2F8),
    onSurfaceVariant = Color(0xFF6B7280),
    outline = Color(0xFFE5E7EB),
    outlineVariant = Color(0xFFD1D5DB),
    tertiary = Color(0xFF059669),
    onTertiary = Color.White,
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF93C5FD),
    onPrimary = Color(0xFF0C1E3D),
    primaryContainer = Color(0xFF1E40AF),
    onPrimaryContainer = Color(0xFFDBEAFE),
    secondary = Color(0xFFC7D2FE),
    onSecondary = Color(0xFF1E1B4B),
    background = Color(0xFF0F1419),
    onBackground = Color(0xFFE5E7EB),
    surface = Color(0xFF1A1F26),
    onSurface = Color(0xFFE5E7EB),
    surfaceVariant = Color(0xFF2A3140),
    onSurfaceVariant = Color(0xFF9CA3AF),
    outline = Color(0xFF3D4654),
    outlineVariant = Color(0xFF374151),
    tertiary = Color(0xFF34D399),
    onTertiary = Color(0xFF003D24),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
)

enum class Screen {
    MENU,
    TAYLOR,
    LAGRANGE,
    NEWTON,
    NEWTON_ROOT,
    FADDEEV,
}

@Composable
@Preview
fun App() {
    val dark = isSystemInDarkTheme()
    MaterialTheme(colorScheme = if (dark) DarkColorScheme else LightColorScheme) {
        var currentScreen by remember { mutableStateOf(Screen.MENU) }

        when (currentScreen) {
            Screen.MENU -> LabMenuScreen(
                onTaylorClick = { currentScreen = Screen.TAYLOR },
                onLagrangeClick = { currentScreen = Screen.LAGRANGE },
                onNewtonClick = { currentScreen = Screen.NEWTON },
                onNewtonRootClick = { currentScreen = Screen.NEWTON_ROOT },
                onFaddeevClick = { currentScreen = Screen.FADDEEV },
            )

            Screen.TAYLOR -> TaylorScreen { currentScreen = Screen.MENU }
            Screen.LAGRANGE -> LagrangeScreen { currentScreen = Screen.MENU }
            Screen.NEWTON -> NewtonScreen { currentScreen = Screen.MENU }
            Screen.NEWTON_ROOT -> NewtonRootScreen { currentScreen = Screen.MENU }
            Screen.FADDEEV -> FaddeevScreen { currentScreen = Screen.MENU }
        }
    }
}
