package com.example.appliedtheoryofcomputationalmethods.math

import com.example.appliedtheoryofcomputationalmethods.model.Point

object Newton {
    fun evaluate(x: Double, points: List<Point>): Double {
        val coef = dividedDifferences(points)

        var result = coef[0]
        var product = 1.0

        for (i in 1 until coef.size) {
            product *= (x - points[i - 1].x)
            result += coef[i] * product
        }

        return result
    }

    private fun dividedDifferences(points: List<Point>): List<Double> {
        val n = points.size
        val coef = DoubleArray(n) { points[it].y }

        for (j in 1 until n) {
            for (i in n - 1 downTo j) {
                coef[i] = (coef[i] - coef[i - 1]) /
                        (points[i].x - points[i - j].x)
            }
        }

        return coef.toList()
    }
}
