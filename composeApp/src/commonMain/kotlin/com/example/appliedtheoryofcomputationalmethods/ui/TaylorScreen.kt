package com.example.appliedtheoryofcomputationalmethods.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appliedtheoryofcomputationalmethods.utils.parseAllDoubles
import com.example.appliedtheoryofcomputationalmethods.utils.parseDoubleLocalized
import com.example.appliedtheoryofcomputationalmethods.viewmodel.InterpolationViewModel

private const val TaylorDerivMin = 1
private const val TaylorDerivMax = 12

private fun taylorDerivativeLabel(order: Int): String = when (order) {
    0 -> "f(x₀)"
    1 -> "f′(x₀)"
    2 -> "f′′(x₀)"
    3 -> "f′′′(x₀)"
    4 -> "f⁽⁴⁾(x₀)"
    5 -> "f⁽⁵⁾(x₀)"
    6 -> "f⁽⁶⁾(x₀)"
    7 -> "f⁽⁷⁾(x₀)"
    8 -> "f⁽⁸⁾(x₀)"
    9 -> "f⁽⁹⁾(x₀)"
    else -> "f⁽${order}⁾(x₀)"
}

@Composable
fun TaylorScreen(onBack: () -> Unit) {
    val vm = remember { InterpolationViewModel() }

    var x0 by remember { mutableStateOf("1") }
    val derivatives = remember {
        mutableStateListOf("20", "61", "154", "270", "240")
    }

    var result by remember { mutableStateOf("") }
    var expandedResult by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    MethodScreenScaffold(
        title = "Ряд Тейлора",
        subtitle = "Многочлен в окрестности точки x₀",
        onBack = onBack,
    ) {
        error?.let { msg ->
            ErrorBanner(message = msg, onDismiss = { error = null })
        }

        Section(
            title = "Параметры разложения",
            subtitle = "Точка x₀ и значения функции с производными до порядка ${derivatives.size - 1}.",
        ) {
            CountStepperRow(
                label = "Слагаемых ряда (включая f(x₀))",
                count = derivatives.size,
                range = TaylorDerivMin..TaylorDerivMax,
                onDecrement = {
                    if (derivatives.size > TaylorDerivMin) derivatives.removeAt(derivatives.lastIndex)
                },
                onIncrement = {
                    if (derivatives.size < TaylorDerivMax) derivatives.add("0")
                },
            )
            Spacer(Modifier.height(12.dp))
            NumberField(x0, { x0 = it }, "Точка x₀")
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Значения в точке x₀",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp),
            )
            derivatives.forEachIndexed { order, value ->
                NumberField(
                    value = value,
                    onChange = { derivatives[order] = it },
                    label = taylorDerivativeLabel(order),
                )
            }
        }

        HintText(
            "Порядок многочлена на единицу меньше числа слагаемых. Поддерживаются запятая и точка в числах.",
        )

        PrimaryButton("Построить многочлен") {
            error = null
            val x0d = x0.parseDoubleLocalized()
            if (x0d == null) {
                error = "В поле «Точка x₀» введите одно действительное число."
                return@PrimaryButton
            }
            val derivs = derivatives.toList().parseAllDoubles()
            if (derivs == null) {
                error = "Во всех полях должны быть корректные числа."
                return@PrimaryButton
            }
            val (normal, expended) = vm.buildTaylorBoth(x0d, derivs)
            result = normal
            expandedResult = expended
        }

        if (result.isNotEmpty()) {
            Section(
                title = "Компактная форма",
                subtitle = "Степени (x − x₀) без раскрытия скобок.",
            ) {
                PolynomialDisplay(result)
            }
            Section(
                title = "Раскрытая форма",
                subtitle = "Один многочлен по степеням x.",
            ) {
                PolynomialDisplay(expandedResult)
            }
        }
    }
}
