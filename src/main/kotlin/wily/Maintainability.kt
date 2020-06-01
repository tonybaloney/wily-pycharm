package wily

import kotlin.math.ln

fun MI (V: Double, G: Int, LOC: Int): Double {
    return 171 - 5.2 * ln(V) - 0.23 * (G) - 16.2 * ln(LOC.toDouble())
}