package com.example.appliedtheoryofcomputationalmethods.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LabMenuScreen(
    onTaylorClick: () -> Unit,
    onLagrangeClick: () -> Unit,
    onNewtonClick: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Лабораторная 1", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onTaylorClick, modifier = Modifier.fillMaxWidth()) {
            Text("Тейлор")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onLagrangeClick, modifier = Modifier.fillMaxWidth()) {
            Text("Лагранж")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onNewtonClick, modifier = Modifier.fillMaxWidth()) {
            Text("Ньютон")
        }
    }
}
