package com.example.appliedtheoryofcomputationalmethods.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appliedtheoryofcomputationalmethods.math.NewtonSolver
import com.example.appliedtheoryofcomputationalmethods.utils.format3
import com.example.appliedtheoryofcomputationalmethods.utils.parseAllDoubles
import com.example.appliedtheoryofcomputationalmethods.utils.parseDoubleLocalized

private const val NewtonPolyCoeffsMin = 2
private const val NewtonPolyCoeffsMax = 8

@Composable
fun NewtonRootScreen(onBack: () -> Unit) {
    val coeffs = remember { mutableStateListOf("1", "3", "4", "1", "-3") }
    var x0 by remember { mutableStateOf("1") }
    var steps by remember { mutableStateOf(listOf<NewtonSolver.Step>()) }
    var error by remember { mutableStateOf<String?>(null) }

    val degree = coeffs.size - 1

    MethodScreenScaffold(
        title = "Метод Ньютона для уравнения",
        subtitle = "Поиск корня многочлена (степень $degree)",
        onBack = onBack,
    ) {
        error?.let { ErrorBanner(it) { error = null } }

        Section(
            title = "Многочлен P(x)",
            subtitle = "Коэффициенты a₀…a$degree: a₀ — при старшей степени x$degree, a$degree — свободный член.",
        ) {
            CountStepperRow(
                label = "Число коэффициентов (степень + 1)",
                count = coeffs.size,
                range = NewtonPolyCoeffsMin..NewtonPolyCoeffsMax,
                onDecrement = {
                    if (coeffs.size > NewtonPolyCoeffsMin) coeffs.removeAt(coeffs.lastIndex)
                },
                onIncrement = {
                    if (coeffs.size < NewtonPolyCoeffsMax) coeffs.add("0")
                },
            )
            Spacer(Modifier.height(12.dp))
            Text(
                newtonPolyHint(coeffs.size),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            RowInputs(coeffs, { i, v -> coeffs[i] = v }, "a")
        }

        Section(
            title = "Стартовое приближение",
            subtitle = "Выберите x₀ ближе к ожидаемому корню — сходимость зависит от начальной точки.",
        ) {
            NumberField(x0, { x0 = it }, "Начальное x₀")
        }

        HintText("Если производная обнуляется на шаге или ряд не сходится, таблица может оборваться раньше ожидаемого.")

        PrimaryButton("Найти корень") {
            error = null
            val c = coeffs.parseAllDoubles()
            val start = x0.parseDoubleLocalized()
            if (c == null) {
                error = "Все ${coeffs.size} коэффициентов должны быть числами."
                return@PrimaryButton
            }
            if (start == null) {
                error = "Введите корректное начальное приближение x₀."
                return@PrimaryButton
            }
            steps = NewtonSolver.solve(c, start)
            if (steps.any { !it.x.isFinite() || !it.nextX.isFinite() || !it.fx.isFinite() || !it.dfx.isFinite() }) {
                error = "Вычисление прервалось (например, нулевая производная или расходимость). Попробуйте другое x₀."
            }
        }

        if (steps.isNotEmpty()) {
            Spacer(Modifier.height(4.dp))
            ResultCard("Приближённый корень", format3(steps.last().nextX))

            Section(
                title = "Итерации метода Ньютона",
                subtitle = "Последняя строка выделена как текущий лучший ответ.",
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                ) {
                    RCell("n", isHeader = true)
                    RCell("xₙ", isHeader = true)
                    RCell("f(xₙ)", isHeader = true)
                    RCell("f′(xₙ)", isHeader = true)
                    RCell("xₙ₊₁", isHeader = true)
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                steps.forEachIndexed { index, step ->
                    val isLast = index == steps.size - 1
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isLast) MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surface,
                            )
                            .padding(vertical = 7.dp, horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RCell("${step.iteration}", bold = isLast)
                        RCell(format3(step.x), bold = isLast)
                        RCell(format3(step.fx), bold = isLast)
                        RCell(format3(step.dfx), bold = isLast)
                        RCell(format3(step.nextX), bold = isLast)
                    }
                    if (!isLast) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            }
        }
    }
}

private fun newtonPolyHint(coeffCount: Int): String {
    val deg = coeffCount - 1
    return when (deg) {
        1 -> "Вид: a₀x + a₁"
        2 -> "Вид: a₀x² + a₁x + a₂"
        3 -> "Вид: a₀x³ + a₁x² + a₂x + a₃"
        4 -> "Вид: a₀x⁴ + a₁x³ + a₂x² + a₃x + a₄"
        5 -> "Вид: a₀x⁵ + a₁x⁴ + … + a₅"
        6 -> "Вид: a₀x⁶ + … + a₆"
        7 -> "Вид: a₀x⁷ + … + a₇"
        else -> "Старший коэффициент — a₀, свободный член — a$deg."
    }
}

@Composable
private fun RowScope.RCell(text: String, isHeader: Boolean = false, bold: Boolean = false) {
    Text(
        text = text,
        modifier = Modifier.weight(1f),
        fontSize = if (isHeader) 11.sp else 12.sp,
        fontWeight = when {
            isHeader -> FontWeight.SemiBold
            bold -> FontWeight.Bold
            else -> FontWeight.Normal
        },
        color = when {
            isHeader -> MaterialTheme.colorScheme.onSurfaceVariant
            bold -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.onSurface
        },
        textAlign = TextAlign.Center,
        maxLines = 1,
    )
}
