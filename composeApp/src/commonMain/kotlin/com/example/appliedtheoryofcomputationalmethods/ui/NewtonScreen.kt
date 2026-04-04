package com.example.appliedtheoryofcomputationalmethods.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appliedtheoryofcomputationalmethods.utils.format3
import com.example.appliedtheoryofcomputationalmethods.utils.parseAllDoubles
import com.example.appliedtheoryofcomputationalmethods.viewmodel.InterpolationViewModel

private const val NewtonNodesMin = 2
private const val NewtonNodesMax = 10
private const val NewtonEvalMin = 1
private const val NewtonEvalMax = 10

@Composable
fun NewtonScreen(onBack: () -> Unit) {
    val vm = remember { InterpolationViewModel() }

    val xs = remember {
        mutableStateListOf("0.115", "0.120", "0.125", "0.130", "0.135")
    }
    val ys = remember {
        mutableStateListOf("8.65", "8.29", "7.95", "7.64", "7.36")
    }
    val evalXs = remember {
        mutableStateListOf("0.1217", "0.1236", "0.1141", "0.105")
    }

    var values by remember { mutableStateOf(listOf<Double>()) }
    var table by remember { mutableStateOf(listOf<List<Double>>()) }
    var form by remember { mutableStateOf(listOf<String>()) }
    var expanded by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    MethodScreenScaffold(
        title = "Ньютон (разделённые разности)",
        subtitle = "Таблица разностей и интерполяционный многочлен",
        onBack = onBack,
    ) {
        error?.let { ErrorBanner(it) { error = null } }

        Section(
            title = "Узлы и значения функции",
            subtitle = "${xs.size} пар (xᵢ, f(xᵢ)); по x удобно возрастание.",
        ) {
            CountStepperRow(
                label = "Число узлов",
                count = xs.size,
                range = NewtonNodesMin..NewtonNodesMax,
                onDecrement = {
                    if (xs.size > NewtonNodesMin) {
                        xs.removeAt(xs.lastIndex)
                        ys.removeAt(ys.lastIndex)
                    }
                },
                onIncrement = {
                    if (xs.size < NewtonNodesMax) {
                        xs.add("0")
                        ys.add("0")
                    }
                },
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "Узлы xᵢ",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 6.dp),
            )
            RowInputs(xs, { i, v -> xs[i] = v }, "x")
            Spacer(Modifier.height(14.dp))
            Text(
                "Значения f(xᵢ)",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 6.dp),
            )
            RowInputs(ys, { i, v -> ys[i] = v }, "f")
        }

        Section(
            title = "Дополнительные точки",
            subtitle = "В этих x вычисляется значение построенного многочлена.",
        ) {
            CountStepperRow(
                label = "Число точек x*",
                count = evalXs.size,
                range = NewtonEvalMin..NewtonEvalMax,
                onDecrement = {
                    if (evalXs.size > NewtonEvalMin) evalXs.removeAt(evalXs.lastIndex)
                },
                onIncrement = {
                    if (evalXs.size < NewtonEvalMax) evalXs.add("0")
                },
            )
            Spacer(Modifier.height(12.dp))
            evalXs.forEachIndexed { i, v ->
                NumberField(
                    value = v,
                    onChange = { evalXs[i] = it },
                    label = "x*${i + 1}",
                )
            }
        }

        PrimaryButton("Построить многочлен и таблицу") {
            error = null
            val xList = xs.toList().parseAllDoubles()
            val yList = ys.toList().parseAllDoubles()
            val eList = evalXs.toList().parseAllDoubles()
            if (xList == null || yList == null || eList == null) {
                error = "Все поля должны содержать корректные числа."
                return@PrimaryButton
            }
            if (xList.size != yList.size) {
                error = "Число x и f(x) должно совпадать."
                return@PrimaryButton
            }
            if (xList.toSet().size != xList.size) {
                error = "Узлы x должны быть попарно различны."
                return@PrimaryButton
            }
            val (t, f, e) = vm.buildNewtonFromInput(xList, yList)
            table = t
            form = f
            expanded = e
            values = vm.evaluateNewtonAtPoints(xList, yList, eList)
        }

        if (table.isNotEmpty()) {
            val n = table.size
            Section(
                title = "Таблица разделённых разностей",
                subtitle = "Первый столбец — значения; далее разности до порядка ${n - 1}.",
            ) {
                Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    Column {
                        Row(modifier = Modifier.padding(bottom = 6.dp)) {
                            NCell("i", isHeader = true, width = 36)
                            NCell("f[·]₀", isHeader = true)
                            for (o in 1 until n) {
                                NCell(deltaHeader(o), isHeader = true)
                            }
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        table.forEachIndexed { i, row ->
                            Row(modifier = Modifier.padding(vertical = 2.dp)) {
                                NCell("$i", width = 36)
                                row.take(n - i).forEach { v -> NCell(format3(v)) }
                            }
                        }
                    }
                }
            }

            Section(
                title = "Форма Ньютона",
                subtitle = "Произведения (x − xⱼ) с коэффициентами из первого столбца таблицы.",
            ) {
                form.forEach { term ->
                    PolynomialDisplay(term)
                    Spacer(Modifier.height(4.dp))
                }
            }

            Section(
                title = "Раскрытый многочлен",
            ) {
                PolynomialDisplay(expanded)
            }

            Section(
                title = "Значения многочлена",
                subtitle = "N(x*) в дополнительных точках.",
            ) {
                values.forEachIndexed { i, v ->
                    ResultCard("N(x*${i + 1})", format3(v))
                    if (i < values.size - 1) Spacer(Modifier.height(6.dp))
                }
            }
        }
    }
}

private fun deltaHeader(order: Int): String = when (order) {
    1 -> "Δ¹"
    2 -> "Δ²"
    3 -> "Δ³"
    4 -> "Δ⁴"
    5 -> "Δ⁵"
    6 -> "Δ⁶"
    7 -> "Δ⁷"
    8 -> "Δ⁸"
    9 -> "Δ⁹"
    else -> "Δ$order"
}

@Composable
private fun NCell(text: String, isHeader: Boolean = false, width: Int = 80) {
    Text(
        text = text,
        modifier = Modifier.width(width.dp).padding(horizontal = 4.dp, vertical = 4.dp),
        fontSize = if (isHeader) 11.sp else 13.sp,
        fontWeight = if (isHeader) FontWeight.SemiBold else FontWeight.Normal,
        color = if (isHeader) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
    )
}
