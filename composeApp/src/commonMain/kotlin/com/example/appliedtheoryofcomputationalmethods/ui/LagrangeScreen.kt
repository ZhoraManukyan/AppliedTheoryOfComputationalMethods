package com.example.appliedtheoryofcomputationalmethods.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appliedtheoryofcomputationalmethods.math.LagrangePolynomialBuilder
import com.example.appliedtheoryofcomputationalmethods.utils.format3
import com.example.appliedtheoryofcomputationalmethods.utils.parseAllDoubles
import com.example.appliedtheoryofcomputationalmethods.viewmodel.InterpolationViewModel

private const val LagrangeNodesMin = 2
private const val LagrangeNodesMax = 10

@Composable
fun LagrangeScreen(onBack: () -> Unit) {
    val vm = remember { InterpolationViewModel() }

    val xs = remember { mutableStateListOf("0", "0.5", "1", "1.5", "2") }
    val ys = remember { mutableStateListOf("1", "3.75", "20", "76", "213") }

    var simplifiedProducts by remember { mutableStateOf(listOf<String>()) }
    var expanded by remember { mutableStateOf("") }
    var sumSymbolic by remember { mutableStateOf("") }
    var sumNumericDenom by remember { mutableStateOf("") }
    val notebookTerms = remember { mutableStateListOf<LagrangePolynomialBuilder.NotebookTerm>() }
    var error by remember { mutableStateOf<String?>(null) }

    MethodScreenScaffold(
        title = "Интерполяция Лагранжа",
        subtitle = "Многочлен, проходящий через заданные узлы",
        onBack = onBack,
    ) {
        error?.let { ErrorBanner(it) { error = null } }

        Section(
            title = "Узлы интерполяции",
            subtitle = "Пар узлов: ${xs.size}. Координаты x должны быть попарно различны.",
        ) {
            CountStepperRow(
                label = "Число узлов",
                count = xs.size,
                range = LagrangeNodesMin..LagrangeNodesMax,
                onDecrement = {
                    if (xs.size > LagrangeNodesMin) {
                        xs.removeAt(xs.lastIndex)
                        ys.removeAt(ys.lastIndex)
                    }
                },
                onIncrement = {
                    if (xs.size < LagrangeNodesMax) {
                        xs.add("0")
                        ys.add("0")
                    }
                },
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "Координаты x",
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

        HintText(
            "Запись как в тетради: сначала дроби с произведениями в числителе и знаменателе, " +
                    "затем численный знаменатель, затем коэффициент при произведении (x−xⱼ), и в конце — раскрытый L(x).",
        )

        PrimaryButton("Построить многочлен") {
            error = null
            val x = xs.parseAllDoubles()
            val y = ys.parseAllDoubles()
            if (x == null || y == null) {
                error = "Проверьте все поля: нужны действительные числа."
                return@PrimaryButton
            }
            if (x.toSet().size != x.size) {
                error = "Координаты x должны быть разными."
                return@PrimaryButton
            }
            val points = x.zip(y)
            notebookTerms.clear()
            notebookTerms.addAll(LagrangePolynomialBuilder.buildNotebookTerms(points))
            sumSymbolic = LagrangePolynomialBuilder.buildNotebookSumEquation(points)
            sumNumericDenom =
                LagrangePolynomialBuilder.buildNotebookSumWithNumericDenominators(points)
            simplifiedProducts = LagrangePolynomialBuilder.buildNotebookSimplifiedProducts(points)
            val (_, exp) = vm.buildLagrangeFromInput(x, y)
            expanded = exp
        }

        if (notebookTerms.isNotEmpty()) {
            val m = notebookTerms.size - 1
            Section(
                title = "Обозначение",
                subtitle = "Для ${notebookTerms.size} узлов получается многочлен степени $m: " +
                        LagrangePolynomialBuilder.lagrangePolynomialLabel(notebookTerms.size) + ".",
            ) {
                Text(
                    text = "L_$m(x) = f₀·ℓ₀(x) + f₁·ℓ₁(x) + … + f_${m}·ℓ_$m(x), " +
                            "где ℓᵢ(x) = Π_{j≠i} (x−xⱼ)/(xᵢ−xⱼ).",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp,
                )
            }

            Section(
                title = "Полная сумма (знаменатель — произведения)",
                subtitle = "Как в тетради: каждое слагаемое fᵢ·(Π(x−xⱼ))/(Π(xᵢ−xⱼ)).",
            ) {
                ScrollableMathLine(sumSymbolic)
            }

            Section(
                title = "Та же сумма с численным знаменателем",
                subtitle = "После вычисления Π(xᵢ−xⱼ) для каждого i.",
            ) {
                ScrollableMathLine(sumNumericDenom)
            }

            Section(
                title = "Слагаемые по узлам",
                subtitle = "Дробь, численное значение знаменателя и вид cᵢ·Π(x−xⱼ).",
            ) {
                notebookTerms.forEachIndexed { index, term ->
                    LagrangeNotebookTermBlock(term)
                    if (index < notebookTerms.lastIndex) {
                        Spacer(Modifier.height(12.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }

        if (simplifiedProducts.isNotEmpty()) {
            Section(
                title = "Упрощённые слагаемые",
                subtitle = "cᵢ = fᵢ / Π(xᵢ−xⱼ); дальше можно раскрыть скобки и привести подобные.",
            ) {
                simplifiedProducts.forEachIndexed { index, term ->
                    PolynomialDisplay(term)
                    if (index < simplifiedProducts.lastIndex) Spacer(Modifier.height(6.dp))
                }
            }
        }

        if (expanded.isNotEmpty()) {
            Section(
                title = "Раскрытый многочлен L(x)",
                subtitle = "Стандартный вид по степеням x.",
            ) {
                PolynomialDisplay(expanded)
            }
        }
    }
}

@Composable
private fun ScrollableMathLine(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 18.sp,
            modifier = Modifier.padding(vertical = 4.dp),
        )
    }
}

@Composable
private fun LagrangeNotebookTermBlock(term: LagrangePolynomialBuilder.NotebookTerm) {
    val i = term.index
    Text(
        text = "Узел i = $i:  x_$i = ${format3(term.xi)},  f_$i = ${format3(term.yi)}",
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.SemiBold,
    )
    Spacer(Modifier.height(8.dp))
    Text(
        text = "f_$i · ℓ_$i(x):",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(top = 4.dp),
    ) {
        FractionView(term.numeratorStr, term.denominatorSymbolicStr, fontSize = 11.sp)
        Text(
            text = " · ${format3(term.yi)}",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
    Text(
        text = "Произведение (x_$i−xⱼ) = ${format3(term.denominatorValue)}",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 8.dp),
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(top = 4.dp),
    ) {
        Text("⇔ ", fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
        FractionView(term.numeratorStr, format3(term.denominatorValue), fontSize = 11.sp)
        Text(
            text = " · ${format3(term.yi)}",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
    Text(
        text = "⇔ ${format3(term.coefficient)} · (${term.numeratorStr})",
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(top = 8.dp),
    )
}
