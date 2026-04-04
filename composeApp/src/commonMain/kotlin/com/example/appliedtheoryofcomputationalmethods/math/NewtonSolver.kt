package com.example.appliedtheoryofcomputationalmethods.math

import kotlin.math.abs
import kotlin.math.pow

object NewtonSolver {
    data class Step(
        val iteration: Int,
        val x: Double,
        val fx: Double,
        val dfx: Double,
        val nextX: Double
    )

    /** P(x) = Σ c[i]·x^(deg−i), deg = c.size − 1 (как в UI: a₀ — старший коэффициент). */
    fun f(x: Double, c: List<Double>): Double {
        if (c.isEmpty()) return 0.0
        val deg = c.size - 1
        var s = 0.0
        for (i in c.indices) {
            val p = deg - i
            s += c[i] * x.pow(p)
        }
        return s
    }

    fun df(x: Double, c: List<Double>): Double {
        if (c.size <= 1) return 0.0
        val deg = c.size - 1
        var s = 0.0
        for (i in c.indices) {
            val p = deg - i
            if (p >= 1) s += c[i] * p * x.pow(p - 1)
        }
        return s
    }

    fun solve(
        coeffs: List<Double>,
        x0: Double,
        eps: Double = 1e-4,
        maxIter: Int = 20
    ): List<Step> {
        val steps = mutableListOf<Step>()
        var x = x0

        for (i in 1..maxIter) {

            val fx = f(x, coeffs)
            val dfx = df(x, coeffs)

            val next = x - fx / dfx

            steps.add(
                Step(i, x, fx, dfx, next)
            )

            if (abs(next - x) < eps) break

            x = next
        }

        return steps
    }
}
