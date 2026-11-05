package com.example.appliedtheoryofcomputationalmethods.math

import com.example.appliedtheoryofcomputationalmethods.utils.format3

object FaddeevLeverrier {
    data class FaddeevStep(
        val k: Int,
        val A_k: Array<DoubleArray>,   // A_k = A * B_{k-1}
        val trace: Double,
        val c: Double,
        val B_k: Array<DoubleArray>    // после добавления c_k * I
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as FaddeevStep

            if (k != other.k) return false
            if (trace != other.trace) return false
            if (c != other.c) return false
            if (!A_k.contentDeepEquals(other.A_k)) return false
            if (!B_k.contentDeepEquals(other.B_k)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = k
            result = 31 * result + trace.hashCode()
            result = 31 * result + c.hashCode()
            result = 31 * result + A_k.contentDeepHashCode()
            result = 31 * result + B_k.contentDeepHashCode()
            return result
        }
    }

    fun solveWithSteps(A: Array<DoubleArray>): Pair<List<Double>, List<FaddeevStep>> {
        val n = A.size
        var B = identity(n)

        val coeffs = mutableListOf<Double>()
        val steps = mutableListOf<FaddeevStep>()

        for (k in 1..n) {

            val A_k = multiply(A, B)

            val tr = trace(A_k)
            val ck = tr / k   // ⚠️ как у тебя в тетради (без минуса!)

            coeffs.add(ck)

            // считаем B_k
            val B_k = copyMatrix(A_k)
            for (i in 0 until n) {
                B_k[i][i] -= ck
            }

            steps.add(
                FaddeevStep(
                    k = k,
                    A_k = copyMatrix(A_k),
                    trace = tr,
                    c = ck,
                    B_k = copyMatrix(B_k)
                )
            )

            B = B_k
        }

        return Pair(coeffs, steps)
    }

    private fun copyMatrix(A: Array<DoubleArray>): Array<DoubleArray> {
        return Array(A.size) { i -> A[i].copyOf() }
    }

    fun formatPolynomial(coeffs: List<Double>): String {
        val n = coeffs.size

        val terms = mutableListOf<String>()

        terms.add("λ^$n")

        // det(λI − A) = λ^n − c₁λ^(n−1) − … − cₙ, because c_k = tr(A_k) / k is taken without a minus
        for (i in coeffs.indices) {
            val power = n - i - 1
            val c = -coeffs[i]

            val term = when (power) {
                0 -> format3(c)
                1 -> "${format3(c)}λ"
                else -> "${format3(c)}λ^$power"
            }

            terms.add(term)
        }

        return terms.joinToString(" + ").replace("+ -", "- ")
    }

    // ---------------- UTILS ----------------

    private fun identity(n: Int): Array<DoubleArray> {
        return Array(n) { i ->
            DoubleArray(n) { j -> if (i == j) 1.0 else 0.0 }
        }
    }

    private fun multiply(A: Array<DoubleArray>, B: Array<DoubleArray>): Array<DoubleArray> {
        val n = A.size
        val result = Array(n) { DoubleArray(n) }

        for (i in 0 until n) {
            for (j in 0 until n) {
                for (k in 0 until n) {
                    result[i][j] += A[i][k] * B[k][j]
                }
            }
        }
        return result
    }

    private fun trace(A: Array<DoubleArray>): Double {
        var sum = 0.0
        for (i in A.indices) {
            sum += A[i][i]
        }
        return sum
    }
}
