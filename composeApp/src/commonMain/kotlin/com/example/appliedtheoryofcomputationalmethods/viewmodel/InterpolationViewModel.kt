package com.example.appliedtheoryofcomputationalmethods.viewmodel

import com.example.appliedtheoryofcomputationalmethods.math.LagrangePolynomialBuilder
import com.example.appliedtheoryofcomputationalmethods.math.NewtonPolynomialBuilder
import com.example.appliedtheoryofcomputationalmethods.math.TaylorPolynomialBuilder

class InterpolationViewModel {
    fun buildTaylorBoth(x0: Double, derivatives: List<Double>): Pair<String, String> =
        TaylorPolynomialBuilder.buildBothForms(x0, derivatives)

    fun buildLagrangeFromInput(
        xs: List<Double>,
        ys: List<Double>
    ): Pair<String, String> {
        val points = xs.zip(ys)
        return LagrangePolynomialBuilder.buildBothForms(points)
    }

    fun buildNewtonFromInput(
        xs: List<Double>,
        ys: List<Double>
    ): Triple<List<List<Double>>, List<String>, String> {
        val points = xs.zip(ys)

        val table = NewtonPolynomialBuilder.buildDividedDifferences(points)
        val form = NewtonPolynomialBuilder.buildNewtonForm(points)
        val expanded = NewtonPolynomialBuilder.polyToString(
            NewtonPolynomialBuilder.expand(points)
        )

        return Triple(table, form, expanded)
    }

    fun evaluateNewtonAtPoints(
        xs: List<Double>,
        ys: List<Double>,
        evalPoints: List<Double>
    ): List<Double> {
        val points = xs.zip(ys)

        return evalPoints.map { x ->
            NewtonPolynomialBuilder.evaluate(x, points)
        }
    }
}
