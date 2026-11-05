package com.example.appliedtheoryofcomputationalmethods

import com.example.appliedtheoryofcomputationalmethods.math.NewtonSolver
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Newton's method for f(x) = 0. Coefficients go from the highest power down (a0 is the leading one). */
class NewtonSolverTest {

    @Test
    fun evaluatesPolynomialFromHighestPower() {
        // x^4 + 3x^3 + 4x^2 + x - 3 (default on the screen) at x = 2: 16 + 24 + 16 + 2 - 3
        assertEquals(55.0, NewtonSolver.f(2.0, listOf(1.0, 3.0, 4.0, 1.0, -3.0)), 1e-12)
    }

    @Test
    fun evaluatesDerivative() {
        // d/dx (x^4 + 3x^3 + 4x^2 + x - 3) = 4x^3 + 9x^2 + 8x + 1, at x = 2: 32 + 36 + 16 + 1
        assertEquals(85.0, NewtonSolver.df(2.0, listOf(1.0, 3.0, 4.0, 1.0, -3.0)), 1e-12)
    }

    @Test
    fun derivativeOfConstantIsZero() {
        assertEquals(0.0, NewtonSolver.df(5.0, listOf(7.0)), 0.0)
    }

    @Test
    fun findsSquareRootOfTwo() {
        val steps = NewtonSolver.solve(listOf(1.0, 0.0, -2.0), x0 = 1.0)

        assertEquals(sqrt(2.0), steps.last().nextX, 1e-4)
    }

    @Test
    fun stepsFollowNewtonFormula() {
        val coefficients = listOf(1.0, 3.0, 4.0, 1.0, -3.0)
        val steps = NewtonSolver.solve(coefficients, x0 = 1.0)

        steps.forEach { s ->
            assertEquals(s.x - s.fx / s.dfx, s.nextX, 1e-12)
        }
        steps.zipWithNext().forEach { (a, b) -> assertEquals(a.nextX, b.x, 0.0) }
    }

    @Test
    fun defaultPolynomialConvergesToARoot() {
        val coefficients = listOf(1.0, 3.0, 4.0, 1.0, -3.0)
        val root = NewtonSolver.solve(coefficients, x0 = 1.0).last().nextX

        assertTrue(abs(NewtonSolver.f(root, coefficients)) < 1e-3)
    }

    @Test
    fun stopsWhenStepIsSmallerThanEpsilon() {
        val steps = NewtonSolver.solve(listOf(1.0, 0.0, -2.0), x0 = 1.0, eps = 1e-4)

        assertTrue(abs(steps.last().nextX - steps.last().x) < 1e-4)
        assertTrue(steps.size < 20)
    }

    @Test
    fun respectsMaxIterations() {
        // x^2 + 1 has no real roots, so the method never converges
        val steps = NewtonSolver.solve(listOf(1.0, 0.0, 1.0), x0 = 0.5, maxIter = 7)

        assertEquals(7, steps.size)
    }
}
