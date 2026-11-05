package com.example.appliedtheoryofcomputationalmethods

import com.example.appliedtheoryofcomputationalmethods.math.FaddeevLeverrier
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Faddeev–Leverrier: A_k = A·B_(k-1), p_k = tr(A_k) / k, B_k = A_k − p_k·I.
 * Then det(λI − A) = λ^n − p_1·λ^(n-1) − … − p_n.
 */
class FaddeevLeverrierTest {

    private fun coefficients(a: Array<DoubleArray>) = FaddeevLeverrier.solveWithSteps(a).first

    @Test
    fun diagonalMatrix() {
        // eigenvalues 2 and 3: λ² − 5λ + 6, so p1 = 5, p2 = −6
        val a = arrayOf(doubleArrayOf(2.0, 0.0), doubleArrayOf(0.0, 3.0))

        assertEquals(listOf(5.0, -6.0), coefficients(a))
    }

    @Test
    fun firstCoefficientIsTrace() {
        val a = arrayOf(
            doubleArrayOf(4.0, 1.0, 2.0),
            doubleArrayOf(0.0, 3.0, 1.0),
            doubleArrayOf(1.0, 2.0, 5.0),
        )

        assertEquals(12.0, coefficients(a).first(), 1e-12)
    }

    @Test
    fun lastCoefficientIsRelatedToDeterminant() {
        // det(λI − A) at λ = 0 is (−1)^n·det(A), so p_n = (−1)^(n+1)·det(A)
        val a = arrayOf(
            doubleArrayOf(4.0, 1.0, 2.0),
            doubleArrayOf(0.0, 3.0, 1.0),
            doubleArrayOf(1.0, 2.0, 5.0),
        )
        val det = 4.0 * (3 * 5 - 1 * 2) - 1.0 * (0 * 5 - 1 * 1) + 2.0 * (0 * 2 - 3 * 1) // = 47

        assertEquals(det, coefficients(a).last(), 1e-9)
    }

    @Test
    fun polynomialVanishesAtEigenvalues() {
        // upper-triangular matrix: eigenvalues are 1, 2, 4
        val a = arrayOf(
            doubleArrayOf(1.0, 5.0, -3.0),
            doubleArrayOf(0.0, 2.0, 7.0),
            doubleArrayOf(0.0, 0.0, 4.0),
        )
        val p = coefficients(a)

        listOf(1.0, 2.0, 4.0).forEach { lambda ->
            val value = lambda * lambda * lambda - p[0] * lambda * lambda - p[1] * lambda - p[2]
            assertEquals(0.0, value, 1e-9)
        }
    }

    @Test
    fun lastBIsZeroMatrix() {
        // Cayley–Hamilton: B_n must be the zero matrix
        val a = arrayOf(
            doubleArrayOf(4.0, 1.0, 2.0),
            doubleArrayOf(0.0, 3.0, 1.0),
            doubleArrayOf(1.0, 2.0, 5.0),
        )
        val lastB = FaddeevLeverrier.solveWithSteps(a).second.last().B_k

        lastB.forEach { row -> row.forEach { assertEquals(0.0, it, 1e-9) } }
    }

    @Test
    fun recordsOneStepPerOrder() {
        val a = arrayOf(doubleArrayOf(2.0, 1.0), doubleArrayOf(1.0, 2.0))
        val steps = FaddeevLeverrier.solveWithSteps(a).second

        assertEquals(listOf(1, 2), steps.map { it.k })
        steps.forEach { assertEquals(it.trace / it.k, it.c, 1e-12) }
    }

    @Test
    fun formattedPolynomialHasCorrectSigns() {
        // diag(2, 3): det(λI − A) = λ² − 5λ + 6
        val a = arrayOf(doubleArrayOf(2.0, 0.0), doubleArrayOf(0.0, 3.0))

        assertEquals("λ^2 - 5.0λ + 6.0", FaddeevLeverrier.formatPolynomial(coefficients(a)))
    }
}
