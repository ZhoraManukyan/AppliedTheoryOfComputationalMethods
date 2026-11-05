package com.example.appliedtheoryofcomputationalmethods.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appliedtheoryofcomputationalmethods.math.FaddeevLeverrier
import com.example.appliedtheoryofcomputationalmethods.math.FaddeevLeverrier.formatPolynomial
import com.example.appliedtheoryofcomputationalmethods.utils.format3
import com.example.appliedtheoryofcomputationalmethods.utils.parseDoubleLocalized

private const val FaddeevNMin = 2
private const val FaddeevNMax = 6

@Composable
fun FaddeevScreen(onBack: () -> Unit) {
    val matrixInput = remember {
        mutableStateListOf(
            mutableStateListOf("3", "1", "1"),
            mutableStateListOf("2", "-1", "3"),
            mutableStateListOf("4", "1", "1"),
        )
    }

    var matrix by remember { mutableStateOf<Array<DoubleArray>?>(null) }
    var steps by remember { mutableStateOf(listOf<FaddeevLeverrier.FaddeevStep>()) }
    var result by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    MethodScreenScaffold(
        title = "Фаддеев–Леверье",
        subtitle = "Характеристический многочлен det(λI − A)",
        onBack = onBack,
    ) {
        error?.let { ErrorBanner(it) { error = null } }

        val n = matrixInput.size

        Section(
            title = "Матрица A (${n}×${n})",
            subtitle = "Введите действительные элементы. Пустые ячейки считаются нулями.",
        ) {
            CountStepperRow(
                label = "Размер n×n",
                count = n,
                range = FaddeevNMin..FaddeevNMax,
                onDecrement = {
                    if (matrixInput.size > FaddeevNMin) {
                        matrixInput.removeAt(matrixInput.lastIndex)
                        for (row in matrixInput) {
                            row.removeAt(row.lastIndex)
                        }
                    }
                },
                onIncrement = {
                    if (matrixInput.size < FaddeevNMax) {
                        for (row in matrixInput) {
                            row.add("0")
                        }
                        matrixInput.add(
                            mutableStateListOf<String>().apply {
                                repeat(matrixInput.size + 1) { add("0") }
                            },
                        )
                    }
                },
            )
            Spacer(Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 6.dp),
            ) {
                repeat(n) { j ->
                    Text(
                        text = "Столбец ${j + 1}",
                        modifier = Modifier.weight(1f),
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
            for (i in 0 until n) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = if (i < n - 1) 8.dp else 0.dp),
                ) {
                    for (j in 0 until n) {
                        OutlinedTextField(
                            value = matrixInput[i][j],
                            onValueChange = { matrixInput[i][j] = it },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            placeholder = {
                                Text(
                                    "a${i + 1}${j + 1}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            ),
                            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                        )
                    }
                }
            }
        }

        PrimaryButton("Вычислить характеристический многочлен") {
            error = null
            val dim = matrixInput.size
            val A = Array(dim) { DoubleArray(dim) }
            for (i in 0 until dim) {
                for (j in 0 until dim) {
                    val raw = matrixInput[i][j].trim()
                    val v = if (raw.isEmpty()) 0.0 else raw.parseDoubleLocalized()
                    if (v == null) {
                        error = "Элемент (строка ${i + 1}, столбец ${j + 1}) не является числом."
                        return@PrimaryButton
                    }
                    A[i][j] = v
                }
            }
            val (coeffs, st) = FaddeevLeverrier.solveWithSteps(A)
            matrix = A
            steps = st
            result = formatPolynomial(coeffs)
        }

        matrix?.let { A ->
            Section(title = "Исходная матрица") {
                MatrixCard(A)
            }
        }

        steps.forEach { s ->
            Section(
                title = "Шаг k = ${s.k}",
                subtitle = "Промежуточные матрицы и коэффициент p${s.k}.",
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "A${s.k} =",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    MatrixCard(s.A_k)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            "p${s.k} = tr(A${s.k}) / ${s.k}",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            format3(s.c),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }

                    Text(
                        "B${s.k} =",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    MatrixCard(s.B_k)
                }
            }
        }

        if (result.isNotEmpty()) {
            Section(
                title = "Характеристический многочлен",
                subtitle = "P(λ) = det(λI − A) в виде многочлена от λ.",
            ) {
                Text(
                    "P(λ) =",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 6.dp),
                )
                PolynomialDisplay(result)
            }
        }
    }
}

@Composable
fun MatrixCard(matrix: Array<DoubleArray>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(10.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        matrix.forEach { row ->
            Row {
                row.forEach { value ->
                    Text(
                        text = format3(value),
                        modifier = Modifier.width(72.dp),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}
