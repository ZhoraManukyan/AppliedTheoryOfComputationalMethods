package com.example.appliedtheoryofcomputationalmethods.math

import kotlin.math.pow

object Taylor {
    fun evaluate(x: Double, x0: Double, derivatives: List<Double>): Double {
        var result = 0.0

        for (k in derivatives.indices) {
            val term = derivatives[k] * (x - x0).pow(k.toDouble()) / factorial(k)
            result += term
        }

        return result
    }

    private fun factorial(n: Int): Double =
        if (n <= 1) 1.0 else n * factorial(n - 1)
}
