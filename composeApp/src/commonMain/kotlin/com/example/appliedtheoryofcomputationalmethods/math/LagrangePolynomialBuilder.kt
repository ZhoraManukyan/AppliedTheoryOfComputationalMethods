package com.example.appliedtheoryofcomputationalmethods.math

import com.example.appliedtheoryofcomputationalmethods.utils.format3
import kotlin.math.round

object LagrangePolynomialBuilder {

    /** Одно слагаемое L_i в записи как в тетради: дробь и численный знаменатель. */
    data class NotebookTerm(
        val index: Int,
        val xi: Double,
        val yi: Double,
        /** Произведение (x − x_j), j ≠ i */
        val numeratorStr: String,
        /** Произведение (x_i − x_j), j ≠ i — как в тетради */
        val denominatorSymbolicStr: String,
        /** Π_j (x_i − x_j) */
        val denominatorValue: Double,
    ) {
        val coefficient: Double get() = yi / denominatorValue
    }

    /** Степень многочлена = число узлов − 1. */
    fun lagrangePolynomialLabel(pointCount: Int): String {
        val m = pointCount - 1
        return "L_$m(x)"
    }

    /**
     * Слагаемые интерполяционного многочлена в форме тетради:
     * f_i · Π(x−x_j) / Π(x_i−x_j).
     */
    fun buildNotebookTerms(points: List<Pair<Double, Double>>): List<NotebookTerm> {
        return points.indices.map { i ->
            val (xi, yi) = points[i]
            val numParts = mutableListOf<String>()
            val denSymParts = mutableListOf<String>()
            var denVal = 1.0
            for (j in points.indices) {
                if (j == i) continue
                val xj = points[j].first
                numParts.add("(x - ${fmtNode(xj)})")
                denSymParts.add("(${fmtNode(xi)} - ${fmtNode(xj)})")
                denVal *= (xi - xj)
            }
            NotebookTerm(
                index = i,
                xi = xi,
                yi = yi,
                numeratorStr = numParts.joinToString(separator = ""),
                denominatorSymbolicStr = denSymParts.joinToString(separator = ""),
                denominatorValue = denVal,
            )
        }
    }

    /** Полная строка суммы (как первая строка в тетради после «L_m(x) = …»). */
    fun buildNotebookSumEquation(points: List<Pair<Double, Double>>): String {
        if (points.isEmpty()) return ""
        val label = lagrangePolynomialLabel(points.size)
        val body = joinNotebookTermsWithSigns(points) { t ->
            "${fmtNode(kotlin.math.abs(t.yi))}·${t.numeratorStr}/${t.denominatorSymbolicStr}"
        }
        return "$label = $body"
    }

    /**
     * Запись после вычисления знаменателей: f_i · (числитель) / (число).
     */
    fun buildNotebookSumWithNumericDenominators(points: List<Pair<Double, Double>>): String {
        if (points.isEmpty()) return ""
        val label = lagrangePolynomialLabel(points.size)
        val body = joinNotebookTermsWithSigns(points) { t ->
            "${fmtNode(kotlin.math.abs(t.yi))}·${t.numeratorStr}/${format3(t.denominatorValue)}"
        }
        return "$label = $body"
    }

    private fun joinNotebookTermsWithSigns(
        points: List<Pair<Double, Double>>,
        piece: (NotebookTerm) -> String,
    ): String {
        val terms = buildNotebookTerms(points)
        val sb = StringBuilder()
        terms.forEachIndexed { i, t ->
            val neg = t.yi < 0
            val p = piece(t)
            when {
                i == 0 -> {
                    if (neg) sb.append("−").append(p) else sb.append(p)
                }

                neg -> sb.append(" − ").append(p)
                else -> sb.append(" + ").append(p)
            }
        }
        return sb.toString()
    }

    /**
     * «Упрощённые» слагаемые: (y_i/d_i)·Π(x−x_j) — как перед раскрытием скобок в тетради.
     */
    fun buildNotebookSimplifiedProducts(points: List<Pair<Double, Double>>): List<String> {
        return buildNotebookTerms(points).map { t ->
            val c = fmtNode(t.coefficient)
            val sign = if (t.coefficient < 0) "− " else ""
            val absC = if (t.coefficient < 0) fmtNode(-t.coefficient) else c
            "${sign}${absC}·${t.numeratorStr}"
        }
    }

    private fun fmtNode(value: Double): String {
        val r = round(value * 1000) / 1000
        return if (kotlin.math.abs(r % 1.0) < 1e-9) {
            r.toInt().toString()
        } else {
            format3(r)
        }
    }

    fun buildBothForms(points: List<Pair<Double, Double>>): Pair<String, String> {
        val normal = buildNormal(points)
        val expanded = expand(points)

        return Pair(normal, "L(x) = ${polyToString(expanded)}")
    }

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

    private fun multiplyPoly(a: DoubleArray, b: DoubleArray): DoubleArray {
        val res = DoubleArray(a.size + b.size - 1)

        for (i in a.indices) {
            for (j in b.indices) {
                res[i + j] += a[i] * b[j]
            }
        }

        return res
    }

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
