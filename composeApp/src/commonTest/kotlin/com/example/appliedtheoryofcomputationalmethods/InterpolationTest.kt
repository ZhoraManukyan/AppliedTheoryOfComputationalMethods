package com.example.appliedtheoryofcomputationalmethods

import com.example.appliedtheoryofcomputationalmethods.math.Lagrange
import com.example.appliedtheoryofcomputationalmethods.math.LagrangePolynomialBuilder
import com.example.appliedtheoryofcomputationalmethods.math.Newton
import com.example.appliedtheoryofcomputationalmethods.math.NewtonPolynomialBuilder
import com.example.appliedtheoryofcomputationalmethods.model.Point
import kotlin.test.Test
import kotlin.test.assertEquals

/** Lagrange and Newton interpolation. Examples are taken from the app screens. */
class InterpolationTest {

    // f(x) = 10x^4 + 5x^3 + 2x^2 + 2x + 1, sampled at 5 nodes (Lagrange screen)
    private fun f(x: Double) = 10 * x * x * x * x + 5 * x * x * x + 2 * x * x + 2 * x + 1
    private val polyNodes = listOf(0.0, 0.5, 1.0, 1.5, 2.0).map { it to f(it) }

    // Newton screen: tabulated function
    private val tableNodes = listOf(
        0.115 to 8.65,
        0.120 to 8.29,
        0.125 to 7.95,
        0.130 to 7.64,
        0.135 to 7.36,
    )

    // ---------- Lagrange ----------

    @Test
    fun lagrangeNodeValuesMatchScreen() {
        assertEquals(listOf(1.0, 3.75, 20.0, 76.0, 213.0), polyNodes.map { it.second })
    }

    @Test
    fun lagrangePassesThroughEveryNode() {
        val points = tableNodes.map { Point(it.first, it.second) }

        points.forEach { p -> assertEquals(p.y, Lagrange.evaluate(p.x, points), 1e-9) }
    }

    @Test
    fun lagrangeReproducesPolynomialOfMatchingDegree() {
        // 5 nodes -> degree 4, so a degree-4 polynomial is reproduced exactly between nodes
        val points = polyNodes.map { Point(it.first, it.second) }

        listOf(0.25, 0.75, 1.25, 1.75, 3.0).forEach { x ->
            assertEquals(f(x), Lagrange.evaluate(x, points), 1e-9)
        }
    }

    @Test
    fun lagrangeExpandedFormMatchesScreen() {
        val (_, expanded) = LagrangePolynomialBuilder.buildBothForms(polyNodes)

        assertEquals("L(x) = 10x^4 + 5x^3 + 2x^2 + 2x + 1", expanded)
    }

    @Test
    fun lagrangeNotebookDenominatorsMatchScreen() {
        // Π(x_i − x_j) for nodes 0, 0.5, 1, 1.5, 2
        val terms = LagrangePolynomialBuilder.buildNotebookTerms(polyNodes)

        val expected = listOf(1.5, -0.375, 0.25, -0.375, 1.5)
        terms.zip(expected).forEach { (term, den) -> assertEquals(den, term.denominatorValue, 1e-12) }
    }

    @Test
    fun lagrangeNotebookCoefficientsMatchScreen() {
        // c_i = f_i / Π(x_i − x_j): 0.667, −10, 80, −202.667, 142
        val coefficients = LagrangePolynomialBuilder.buildNotebookTerms(polyNodes).map { it.coefficient }

        val expected = listOf(1 / 1.5, -10.0, 80.0, -76 / 0.375, 142.0)
        coefficients.zip(expected).forEach { (actual, exp) -> assertEquals(exp, actual, 1e-9) }
    }

    @Test
    fun lagrangeLabelUsesDegree() {
        assertEquals("L_4(x)", LagrangePolynomialBuilder.lagrangePolynomialLabel(5))
    }

    // ---------- Newton ----------

    @Test
    fun dividedDifferencesMatchScreen() {
        val table = NewtonPolynomialBuilder.buildDividedDifferences(tableNodes)

        val firstOrder = (0 until 4).map { table[it][1] }
        val secondOrder = (0 until 3).map { table[it][2] }

        listOf(-72.0, -68.0, -62.0, -56.0).zip(firstOrder).forEach { (e, a) -> assertEquals(e, a, 1e-6) }
        listOf(400.0, 600.0, 600.0).zip(secondOrder).forEach { (e, a) -> assertEquals(e, a, 1e-4) }
        assertEquals(13333.33, table[0][3], 1e-2)
        assertEquals(-666666.67, table[0][4], 1e-1)
    }

    @Test
    fun newtonValuesAtExtraPointsMatchScreen() {
        val expected = mapOf(0.1217 to 8.171, 0.1236 to 8.042, 0.1141 to 8.716, 0.105 to 9.34)

        expected.forEach { (x, y) ->
            assertEquals(y, NewtonPolynomialBuilder.evaluate(x, tableNodes), 1e-3)
        }
    }

    @Test
    fun newtonAndLagrangeGiveTheSamePolynomial() {
        // The interpolating polynomial is unique, so both methods must agree everywhere
        val points = tableNodes.map { Point(it.first, it.second) }

        listOf(0.11, 0.1217, 0.128, 0.14).forEach { x ->
            assertEquals(Lagrange.evaluate(x, points), Newton.evaluate(x, points), 1e-6)
            assertEquals(Lagrange.evaluate(x, points), NewtonPolynomialBuilder.evaluate(x, tableNodes), 1e-6)
        }
    }

    @Test
    fun newtonExpandedCoefficientsReproducePolynomial() {
        // Expanded coefficients are stored from x^0 upwards
        val coefficients = NewtonPolynomialBuilder.expand(polyNodes)

        listOf(1.0, 2.0, 2.0, 5.0, 10.0).zip(coefficients.toList()).forEach { (e, a) ->
            assertEquals(e, a, 1e-9)
        }
    }

    @Test
    fun newtonFormStartsWithFirstValue() {
        val form = NewtonPolynomialBuilder.buildNewtonForm(tableNodes)

        assertEquals(5, form.size)
        assertEquals("8.65", form[0])
        assertEquals("-72 * (x - 0.115)", form[1])
    }

    @Test
    fun negativeCoefficientsAreShownWithMinus() {
        // Newton screen: expanded polynomial must not contain "+ -"
        val expanded = NewtonPolynomialBuilder.polyToString(NewtonPolynomialBuilder.expand(tableNodes))

        assertEquals(false, expanded.contains("+ -"))
        assertEquals("-666666.67x^4 + 340000x^3 - 64383.33x^2 + 5301.5x - 150.05", expanded)
    }
}
