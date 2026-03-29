package com.example.appliedtheoryofcomputationalmethods.math

import kotlin.math.round

object NewtonPolynomialBuilder {
    fun evaluate(
        x: Double,
        points: List<Pair<Double, Double>>
    ): Double {

        val table = buildDividedDifferences(points)

        var result = table[0][0]
        var product = 1.0

        for (i in 1 until points.size) {
            product *= (x - points[i - 1].first)
            result += table[0][i] * product
        }

        return result
    }

    // -----------------------------
    // ТАБЛИЦА РАЗНОСТЕЙ
    // -----------------------------
    fun buildDividedDifferences(points: List<Pair<Double, Double>>): List<List<Double>> {

        val n = points.size
        val table = MutableList(n) { MutableList(n) { 0.0 } }

        for (i in 0 until n) {
            table[i][0] = points[i].second
        }

        for (j in 1 until n) {
            for (i in 0 until n - j) {
                table[i][j] =
                    (table[i + 1][j - 1] - table[i][j - 1]) /
                            (points[i + j].first - points[i].first)
            }
        }

        return table
    }

    // -----------------------------
    // ФОРМУЛА НЬЮТОНА (как в тетради)
    // -----------------------------
    fun buildNewtonForm(points: List<Pair<Double, Double>>): List<String> {

        val table = buildDividedDifferences(points)
        val terms = mutableListOf<String>()

        for (i in points.indices) {

            val coef = table[0][i]
            val factors = mutableListOf<String>()

            for (j in 0 until i) {
                factors.add("(x - ${points[j].first})")
            }

            val term = if (factors.isEmpty()) {
                format(coef)
            } else {
                "${format(coef)} * " + factors.joinToString("")
            }

            terms.add(term)
        }

        return terms
    }

    // -----------------------------
    // РАСКРЫТИЕ
    // -----------------------------
    fun expand(points: List<Pair<Double, Double>>): DoubleArray {

        val table = buildDividedDifferences(points)
        val n = points.size
        val result = DoubleArray(n)

        for (i in 0 until n) {

            var poly = doubleArrayOf(1.0)

            for (j in 0 until i) {
                poly = multiply(poly, doubleArrayOf(-points[j].first, 1.0))
            }

            val coef = table[0][i]

            for (k in poly.indices) {
                result[k] += poly[k] * coef
            }
        }

        return result
    }

    private fun multiply(a: DoubleArray, b: DoubleArray): DoubleArray {
        val res = DoubleArray(a.size + b.size - 1)

        for (i in a.indices) {
            for (j in b.indices) {
                res[i + j] += a[i] * b[j]
            }
        }

        return res
    }

    fun polyToString(coef: DoubleArray): String {
        val terms = mutableListOf<String>()

        for (i in coef.indices.reversed()) {
            val c = format(coef[i])
            if (c == "0") continue

            val term = when (i) {
                0 -> c
                1 -> "${c}x"
                else -> "${c}x^$i"
            }

            terms.add(term)
        }

        return terms.joinToString(" + ")
    }

    private fun format(value: Double): String {
        val r = round(value * 100) / 100
        return if (r % 1.0 == 0.0) r.toInt().toString() else r.toString()
    }
}