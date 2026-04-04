package com.example.appliedtheoryofcomputationalmethods.utils

fun format3(value: Double): String {
    val rounded = kotlin.math.round(value * 1000) / 1000
    return rounded.toString()
}

/** Поддержка запятой как десятичного разделителя (удобно при ручном вводе). */
fun String.parseDoubleLocalized(): Double? =
    trim().replace(',', '.').toDoubleOrNull()

fun List<String>.parseAllDoubles(): List<Double>? {
    val out = ArrayList<Double>(size)
    for (s in this) {
        out.add(s.parseDoubleLocalized() ?: return null)
    }
    return out
}
