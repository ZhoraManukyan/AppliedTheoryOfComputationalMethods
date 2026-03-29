package com.example.appliedtheoryofcomputationalmethods.utils

fun format3(value: Double): String {
    val rounded = kotlin.math.round(value * 1000) / 1000
    return rounded.toString()
}
