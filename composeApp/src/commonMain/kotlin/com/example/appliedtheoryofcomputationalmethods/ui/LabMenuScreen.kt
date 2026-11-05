package com.example.appliedtheoryofcomputationalmethods.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class MethodItem(
    val title: String,
    val description: String,
    /** Короткий символ вместо векторной иконки — без material-icons-extended. */
    val glyph: String,
    val onClick: () -> Unit,
)

@Composable
fun LabMenuScreen(
    onTaylorClick: () -> Unit,
    onLagrangeClick: () -> Unit,
    onNewtonClick: () -> Unit,
    onNewtonRootClick: () -> Unit,
    onFaddeevClick: () -> Unit,
) {
    val methods = listOf(
        MethodItem(
            title = "Ряд Тейлора",
            description = "Многочлен по значениям функции и производных в точке x₀",
            glyph = "T",
            onClick = onTaylorClick,
        ),
        MethodItem(
            title = "Интерполяция Лагранжа",
            description = "Многочлен по узлам (xᵢ, yᵢ), базисные дроби и раскрытая форма",
            glyph = "L",
            onClick = onLagrangeClick,
        ),
        MethodItem(
            title = "Ньютон (разделённые разности)",
            description = "Таблица разностей, каноническая форма и значения в дополнительных точках",
            glyph = "Δ",
            onClick = onNewtonClick,
        ),
        MethodItem(
            title = "Ньютон для уравнения f(x) = 0",
            description = "Итерации xₙ₊₁ = xₙ − f/f′ для многочлена",
            glyph = "x",
            onClick = onNewtonRootClick,
        ),
        MethodItem(
            title = "Фаддеев–Леверье",
            description = "Коэффициенты характеристического многочлена det(λI − A)",
            glyph = "λ",
            onClick = onFaddeevClick,
        ),
    )

    LabHomeScaffold(
        title = "Лабораторные методы",
        subtitle = "Прикладная теория вычислительных методов",
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Text(
                            text = "С чего начать",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "Выберите тему ниже. На каждом экране введите числа и нажмите основную кнопку — результат появится ниже. Десятичная запятая и точка оба поддерживаются.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.92f),
                        )
                    }
                }
            }

            itemsIndexed(methods) { index, item ->
                MethodCard(
                    number = index + 1,
                    title = item.title,
                    description = item.description,
                    glyph = item.glyph,
                    onClick = item.onClick,
                )
            }
        }
    }
}

@Composable
private fun MethodCard(
    number: Int,
    title: String,
    description: String,
    glyph: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp, pressedElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        RoundedCornerShape(12.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = glyph,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(Modifier.width(4.dp))

            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$number.",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 6.dp),
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
