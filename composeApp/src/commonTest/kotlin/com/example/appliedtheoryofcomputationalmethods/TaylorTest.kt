package com.example.appliedtheoryofcomputationalmethods

import com.example.appliedtheoryofcomputationalmethods.math.Taylor
import com.example.appliedtheoryofcomputationalmethods.math.TaylorPolynomialBuilder
import kotlin.math.E
import kotlin.math.exp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TaylorTest {

    // Taylor screen: x0 = 1, f = 20, f' = 61, f'' = 154, f''' = 270, f'''' = 240
    // which is f(x) = 10x^4 + 5x^3 + 2x^2 + 2x + 1
    private val x0 = 1.0
    private val derivatives = listOf(20.0, 61.0, 154.0, 270.0, 240.0)
    private fun f(x: Double) = 10 * x * x * x * x + 5 * x * x * x + 2 * x * x + 2 * x + 1

    @Test
    fun compactFormMatchesScreen() {
        val (compact, _) = TaylorPolynomialBuilder.buildBothForms(x0, derivatives)

        assertEquals(
            "P(x) = 20 + 61(x - 1.0) + 77(x - 1.0)^2 + 45(x - 1.0)^3 + 10(x - 1.0)^4",
            compact
        )
    }

    @Test
    fun expandedFormMatchesScreen() {
        val (_, expanded) = TaylorPolynomialBuilder.buildBothForms(x0, derivatives)

        assertEquals("P(x) = 10x^4 + 5x^3 + 2x^2 + 2x + 1", expanded)
    }

    @Test
    fun valueAtExpansionPointIsFOfX0() {
        assertEquals(20.0, Taylor.evaluate(x0, x0, derivatives), 1e-12)
    }

    @Test
    fun polynomialOfDegreeFourIsReproducedExactly() {
        listOf(-1.0, 0.0, 0.5, 2.0, 3.0).forEach { x ->
            assertEquals(f(x), Taylor.evaluate(x, x0, derivatives), 1e-9)
        }
    }

    @Test
    fun exponentialApproximationImprovesWithMoreTerms() {
        // All derivatives of e^x at 0 are 1
        val x = 1.0
        val error3 = kotlin.math.abs(E - Taylor.evaluate(x, 0.0, List(3) { 1.0 }))
        val error8 = kotlin.math.abs(E - Taylor.evaluate(x, 0.0, List(8) { 1.0 }))

        assertTrue(error8 < error3)
        assertEquals(exp(0.5), Taylor.evaluate(0.5, 0.0, List(12) { 1.0 }), 1e-9)
    }
}
