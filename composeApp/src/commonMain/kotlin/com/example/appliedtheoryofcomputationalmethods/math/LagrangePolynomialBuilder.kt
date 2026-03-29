package com.example.appliedtheoryofcomputationalmethods.math

import kotlin.math.round

object LagrangePolynomialBuilder {

    fun buildBothForms(points: List<Pair<Double, Double>>): Pair<String, String> {
        val normal = buildNormal(points)
        val expanded = expand(points)

        return Pair(normal, "L(x) = ${polyToString(expanded)}")
    }

    fun buildFractions(points: List<Pair<Double, Double>>): List<Triple<String, String, Double>> {
        val result = mutableListOf<Triple<String, String, Double>>()

        for (i in points.indices) {

            val (xi, yi) = points[i]

            val numerator = mutableListOf<String>()
            val denominator = mutableListOf<String>()

            for (j in points.indices) {
                if (i != j) {
                    numerator.add("(x - ${points[j].first})")
                    denominator.add("(${xi} - ${points[j].first})")
                }
            }

            result.add(
                Triple(
                    numerator.joinToString(""),
                    denominator.joinToString(""),
                    yi
                )
            )
        }

        return result
    }

    fun buildSimplified(points: List<Pair<Double, Double>>): List<String> {

        val result = mutableListOf<String>()

        for (i in points.indices) {

            val (xi, yi) = points[i]

            val numerator = mutableListOf<String>()
            var denominator = 1.0

            for (j in points.indices) {
                if (i != j) {
                    val xj = points[j].first
                    numerator.add("(x - $xj)")
                    denominator *= (xi - xj)
                }
            }

            val coef = yi / denominator

            val term = "${format(coef)} * " + numerator.joinToString("")

            result.add(term)
        }

        return result
    }

    // -----------------------
    // НЕРАСКРЫТЫЙ ВИД
    // -----------------------
    private fun buildNormal(points: List<Pair<Double, Double>>): String {
        val terms = mutableListOf<String>()

        for (i in points.indices) {

            val (xi, yi) = points[i]

            val numerator = mutableListOf<String>()
            val denominator = mutableListOf<String>()

            for (j in points.indices) {
                if (i != j) {
                    numerator.add("(x - ${points[j].first})")
                    denominator.add("(${xi} - ${points[j].first})")
                }
            }

            val term = "${format(yi)} * " +
                    numerator.joinToString("") +
                    " / " +
                    denominator.joinToString("")

            terms.add(term)
        }

        return "L(x) = " + terms.joinToString(" + ")
    }

    // -----------------------
    // РАСКРЫТИЕ
    // -----------------------
    private fun expand(points: List<Pair<Double, Double>>): DoubleArray {

        val n = points.size
        val result = DoubleArray(n)

        for (i in points.indices) {

            val xi = points[i].first
            val yi = points[i].second

            var basis = doubleArrayOf(1.0)

            var denom = 1.0

            for (j in points.indices) {
                if (i != j) {
                    val xj = points[j].first

                    basis = multiplyPoly(basis, doubleArrayOf(-xj, 1.0))
                    denom *= (xi - xj)
                }
            }

            for (k in basis.indices) {
                result[k] += basis[k] * (yi / denom)
            }
        }

        return result
    }

    // -----------------------
    private fun multiplyPoly(a: DoubleArray, b: DoubleArray): DoubleArray {
        val res = DoubleArray(a.size + b.size - 1)

        for (i in a.indices) {
            for (j in b.indices) {
                res[i + j] += a[i] * b[j]
            }
        }

        return res
    }

    // -----------------------
    private fun polyToString(coef: DoubleArray): String {
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