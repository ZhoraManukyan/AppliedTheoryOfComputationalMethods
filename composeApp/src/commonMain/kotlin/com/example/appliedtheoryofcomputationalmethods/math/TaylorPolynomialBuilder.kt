package com.example.appliedtheoryofcomputationalmethods.math

object TaylorPolynomialBuilder {
    fun buildBothForms(
        x0: Double,
        derivatives: List<Double>
    ): Pair<String, String> {

        val n = derivatives.size

        // коэффициенты Тейлора
        val a = DoubleArray(n)
        for (k in 0 until n) {
            a[k] = derivatives[k] / factorial(k)
        }

        // 1️⃣ НЕРАСКРЫТЫЙ ВИД
        val normalForm = buildNormalForm(x0, a)

        // 2️⃣ РАСКРЫТЫЙ ВИД
        val expanded = expandPolynomial(x0, a)

        val expandedForm = "P(x) = " + polynomialToString(expanded)

        return Pair(normalForm, expandedForm)
    }

    // ------------------------
    // НЕРАСКРЫТЫЙ ВИД
    // ------------------------
    private fun buildNormalForm(x0: Double, a: DoubleArray): String {
        val terms = mutableListOf<String>()

        for (k in a.indices) {
            val coef = format(a[k])

            val term = when (k) {
                0 -> coef
                1 -> "$coef(x - $x0)"
                else -> "$coef(x - $x0)^$k"
            }

            terms.add(term)
        }

        return "P(x) = " + terms.joinToString(" + ")
    }

    // ------------------------
    // РАСКРЫТИЕ СКОБОК
    // ------------------------
    private fun expandPolynomial(x0: Double, a: DoubleArray): DoubleArray {
        val result = DoubleArray(a.size)

        for (k in a.indices) {

            val binomial = expandBinomial(k, -x0)

            for (i in binomial.indices) {
                result[i] += a[k] * binomial[i]
            }
        }

        return result
    }

    // (x + c)^k
    private fun expandBinomial(power: Int, c: Double): DoubleArray {
        val coef = DoubleArray(power + 1)

        for (i in 0..power) {
            coef[i] = binomialCoeff(power, i) * c.pow(power - i)
        }

        return coef
    }

    private fun binomialCoeff(n: Int, k: Int): Double {
        return factorial(n) / (factorial(k) * factorial(n - k))
    }

    // ------------------------
    // В СТРОКУ
    // ------------------------
    private fun polynomialToString(coef: DoubleArray): String {
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

    private fun Double.pow(p: Int): Double {
        var result = 1.0
        repeat(p) { result *= this }
        return result
    }

    // -------------------------------

    fun buildPolynomial(
        x0: Double,
        derivatives: List<Double>
    ): String {
        val terms = mutableListOf<String>()

        for (k in derivatives.indices) {

            val coef = derivatives[k] / factorial(k)

            val term = when (k) {
                0 -> format(coef)
                1 -> "${format(coef)}(x - $x0)"
                else -> "${format(coef)}(x - $x0)^$k"
            }

            terms.add(term)
        }

        return "P(x) = " + terms.joinToString(" + ")
    }

    private fun factorial(n: Int): Double =
        if (n <= 1) 1.0 else n * factorial(n - 1)

    private fun format(value: Double): String {
        val rounded = kotlin.math.round(value * 100) / 100
        return if (rounded % 1.0 == 0.0) {
            rounded.toInt().toString()
        } else {
            rounded.toString()
        }
    }
}
