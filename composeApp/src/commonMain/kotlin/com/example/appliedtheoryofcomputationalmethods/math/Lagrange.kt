package com.example.appliedtheoryofcomputationalmethods.math

import com.example.appliedtheoryofcomputationalmethods.model.Point

object Lagrange {
    fun evaluate(x: Double, points: List<Point>): Double {
        var result = 0.0

        for (i in points.indices) {
            var term = points[i].y

            for (j in points.indices) {
                if (i != j) {
                    term *= (x - points[j].x) / (points[i].x - points[j].x)
                }
            }

            result += term
        }

        return result
    }
}
