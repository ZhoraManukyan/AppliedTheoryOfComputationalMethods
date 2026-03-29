package com.example.appliedtheoryofcomputationalmethods.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appliedtheoryofcomputationalmethods.utils.format3
import com.example.appliedtheoryofcomputationalmethods.viewmodel.InterpolationViewModel

@Composable
fun NewtonScreen(onBack: () -> Unit) {

    val vm = remember { InterpolationViewModel() }

    // X
    var x0 by remember { mutableStateOf("0.115") }
    var x1 by remember { mutableStateOf("0.120") }
    var x2 by remember { mutableStateOf("0.125") }
    var x3 by remember { mutableStateOf("0.130") }
    var x4 by remember { mutableStateOf("0.135") }

    // Y
    var f0 by remember { mutableStateOf("8.65") }
    var f1 by remember { mutableStateOf("8.29") }
    var f2 by remember { mutableStateOf("7.95") }
    var f3 by remember { mutableStateOf("7.64") }
    var f4 by remember { mutableStateOf("7.36") }

    var ex1 by remember { mutableStateOf("0.1217") }
    var ex2 by remember { mutableStateOf("0.1236") }
    var ex3 by remember { mutableStateOf("0.1141") }
    var ex4 by remember { mutableStateOf("0.105") }

    var values by remember { mutableStateOf(listOf<Double>()) }

    var table by remember { mutableStateOf(listOf<List<Double>>()) }
    var form by remember { mutableStateOf(listOf<String>()) }
    var expanded by remember { mutableStateOf("") }

    LazyColumn(modifier = Modifier.padding(16.dp)) {

        item { Text("Ньютон") }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // X
        item { Text("X значения") }
        item { OutlinedTextField(x0, { x0 = it }, label = { Text("x0") }) }
        item { OutlinedTextField(x1, { x1 = it }, label = { Text("x1") }) }
        item { OutlinedTextField(x2, { x2 = it }, label = { Text("x2") }) }
        item { OutlinedTextField(x3, { x3 = it }, label = { Text("x3") }) }
        item { OutlinedTextField(x4, { x4 = it }, label = { Text("x4") }) }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // Y
        item { Text("f(x) значения") }
        item { OutlinedTextField(f0, { f0 = it }, label = { Text("f(x0)") }) }
        item { OutlinedTextField(f1, { f1 = it }, label = { Text("f(x1)") }) }
        item { OutlinedTextField(f2, { f2 = it }, label = { Text("f(x2)") }) }
        item { OutlinedTextField(f3, { f3 = it }, label = { Text("f(x3)") }) }
        item { OutlinedTextField(f4, { f4 = it }, label = { Text("f(x4)") }) }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item { Text("Точки для вычисления") }
        item { OutlinedTextField(ex1, { ex1 = it }, label = { Text("x1") }) }
        item { OutlinedTextField(ex2, { ex2 = it }, label = { Text("x2") }) }
        item { OutlinedTextField(ex3, { ex3 = it }, label = { Text("x3") }) }
        item { OutlinedTextField(ex4, { ex4 = it }, label = { Text("x4") }) }

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

                val evalXs = listOf(
                    ex1.toDouble(),
                    ex2.toDouble(),
                    ex3.toDouble(),
                    ex4.toDouble()
                )

                val (t, f, e) = vm.buildNewtonFromInput(xs, ys)

                table = t
                form = f
                expanded = e

                values = vm.evaluateNewtonAtPoints(xs, ys, evalXs)
            }) {
                Text("Построить")
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // 📊 ТАБЛИЦА
        item { Text("1. Таблица разностей:") }

        items(table.size) { i ->
            Text(
                table[i]
                    .take(table.size - i)
                    .joinToString("   ") { value ->
                        format3(value).padStart(8)
                    }
            )
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // 📐 ФОРМУЛА
        item { Text("2. Формула Ньютона:") }

        items(form.size) { i ->
            Text(form[i])
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // 📈 РАСКРЫТЫЙ
        item { Text("3. Раскрытый:") }
        item { Text(expanded) }

        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { Text("4. Значения:") }

        items(values.size) { i ->
            Text("N(x${i + 1}) = ${values[i]}")
        }

        item {
            Button(onClick = onBack) {
                Text("Назад")
            }
        }
    }
}
