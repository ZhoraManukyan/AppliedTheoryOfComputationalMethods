package com.example.appliedtheoryofcomputationalmethods.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appliedtheoryofcomputationalmethods.math.LagrangePolynomialBuilder
import com.example.appliedtheoryofcomputationalmethods.viewmodel.InterpolationViewModel

@Composable
fun LagrangeScreen(onBack: () -> Unit) {

    val vm = remember { InterpolationViewModel() }

    // X
    var x0 by remember { mutableStateOf("0") }
    var x1 by remember { mutableStateOf("0.5") }
    var x2 by remember { mutableStateOf("1") }
    var x3 by remember { mutableStateOf("1.5") }
    var x4 by remember { mutableStateOf("2") }

    // Y
    var f0 by remember { mutableStateOf("1") }
    var f1 by remember { mutableStateOf("3.75") }
    var f2 by remember { mutableStateOf("20") }
    var f3 by remember { mutableStateOf("76") }
    var f4 by remember { mutableStateOf("213") }

    var simplified by remember { mutableStateOf(listOf<String>()) }
    var expanded by remember { mutableStateOf("") }

    // 💥 ДРОБИ
    val fractions = remember { mutableStateListOf<Triple<String, String, Double>>() }

    LazyColumn(modifier = Modifier.padding(16.dp)) {

        item { Text("Лагранж") }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item { Text("X значения") }
        item { OutlinedTextField(x0, { x0 = it }, label = { Text("x0") }) }
        item { OutlinedTextField(x1, { x1 = it }, label = { Text("x1") }) }
        item { OutlinedTextField(x2, { x2 = it }, label = { Text("x2") }) }
        item { OutlinedTextField(x3, { x3 = it }, label = { Text("x3") }) }
        item { OutlinedTextField(x4, { x4 = it }, label = { Text("x4") }) }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item { Text("f(x) значения") }
        item { OutlinedTextField(f0, { f0 = it }, label = { Text("f(x0)") }) }
        item { OutlinedTextField(f1, { f1 = it }, label = { Text("f(x1)") }) }
        item { OutlinedTextField(f2, { f2 = it }, label = { Text("f(x2)") }) }
        item { OutlinedTextField(f3, { f3 = it }, label = { Text("f(x3)") }) }
        item { OutlinedTextField(f4, { f4 = it }, label = { Text("f(x4)") }) }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item {
            Button(onClick = {

                val xs = listOf(
                    x0.toDouble(),
                    x1.toDouble(),
                    x2.toDouble(),
                    x3.toDouble(),
                    x4.toDouble()
                )

                val ys = listOf(
                    f0.toDouble(),
                    f1.toDouble(),
                    f2.toDouble(),
                    f3.toDouble(),
                    f4.toDouble()
                )

                val simple = LagrangePolynomialBuilder.buildSimplified(xs.zip(ys))
                simplified = simple

                // 🔥 дроби
                fractions.clear()
                fractions.addAll(
                    LagrangePolynomialBuilder.buildFractions(xs.zip(ys))
                )

                // раскрытый
                val (_, exp) = vm.buildLagrangeFromInput(xs, ys)
                expanded = exp

            }) {
                Text("Построить")
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // 🔥 ВЫВОД КАК В ТЕТРАДИ
        item { Text("Нераскрытый:") }

        items(fractions.size) { i ->

            val (num, den, y) = fractions[i]

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {

                FractionView(
                    numerator = num,
                    denominator = den
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("* $y")
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
        item { Spacer(modifier = Modifier.height(12.dp)) }

        item { Text("Упрощенный:") }
        items(simplified.size) { i ->
            Text(simplified[i])
        }

        item { Spacer(modifier = Modifier.height(12.dp)) }

        item { Text("Раскрытый:") }
        item { Text(expanded) }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item {
            Button(onClick = onBack) {
                Text("Назад")
            }
        }
    }
}
