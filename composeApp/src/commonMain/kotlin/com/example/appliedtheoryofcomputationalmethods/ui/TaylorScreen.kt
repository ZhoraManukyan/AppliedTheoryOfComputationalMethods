package com.example.appliedtheoryofcomputationalmethods.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.appliedtheoryofcomputationalmethods.viewmodel.InterpolationViewModel


@Composable
fun TaylorScreen(onBack: () -> Unit) {
    val vm = remember { InterpolationViewModel() }

    var x0 by remember { mutableStateOf("1") }

    var f0 by remember { mutableStateOf("20") }
    var f1 by remember { mutableStateOf("61") }
    var f2 by remember { mutableStateOf("154") }
    var f3 by remember { mutableStateOf("270") }
    var f4 by remember { mutableStateOf("240") }

    var result by remember { mutableStateOf("") }
    var expandedResult by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {

        Text("Taylor Polynomial Builder")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = x0,
            onValueChange = { x0 = it },
            label = { Text("x0") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(f0, { f0 = it }, label = { Text("f(x0)") })
        OutlinedTextField(f1, { f1 = it }, label = { Text("f'(x0)") })
        OutlinedTextField(f2, { f2 = it }, label = { Text("f''(x0)") })
        OutlinedTextField(f3, { f3 = it }, label = { Text("f'''(x0)") })
        OutlinedTextField(f4, { f4 = it }, label = { Text("f⁽⁴⁾(x0)") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val derivatives = listOf(
                f0.toDouble(),
                f1.toDouble(),
                f2.toDouble(),
                f3.toDouble(),
                f4.toDouble()
            )

            val (normal, expended) = vm.buildTaylorBoth(x0.toDouble(), derivatives)

            result = normal
            expandedResult = expended

        }) {
            Text("Построить многочлен")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Нераскрытый:")
        Text(result)

        Spacer(modifier = Modifier.height(8.dp))

        Text("Раскрытый:")
        Text(expandedResult)

        Button(onClick = onBack) {
            Text("Назад")
        }
    }
}
