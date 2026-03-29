package com.example.appliedtheoryofcomputationalmethods

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.appliedtheoryofcomputationalmethods.ui.LabMenuScreen
import com.example.appliedtheoryofcomputationalmethods.ui.LagrangeScreen
import com.example.appliedtheoryofcomputationalmethods.ui.NewtonScreen
import com.example.appliedtheoryofcomputationalmethods.ui.TaylorScreen

enum class Screen {
    MENU,
    TAYLOR,
    LAGRANGE,
    NEWTON
}

@Composable
@Preview
fun App() {
    MaterialTheme {
        var currentScreen by remember { mutableStateOf(Screen.MENU) }

        when (currentScreen) {

            Screen.MENU -> LabMenuScreen(
                onTaylorClick = { currentScreen = Screen.TAYLOR },
                onLagrangeClick = { currentScreen = Screen.LAGRANGE },
                onNewtonClick = { currentScreen = Screen.NEWTON }
            )

            Screen.TAYLOR -> TaylorScreen {
                currentScreen = Screen.MENU
            }

            Screen.LAGRANGE -> LagrangeScreen {
                currentScreen = Screen.MENU
            }

            Screen.NEWTON -> NewtonScreen {
                currentScreen = Screen.MENU
            }
        }
    }
}
