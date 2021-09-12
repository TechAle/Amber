package dev.amber.api.util

import kotlin.math.pow
import kotlin.math.round

object MathUtils {

    fun round(value: Float, places: Int): Double {
        val scale = 10.0.pow(places.toDouble())
        return round(value * scale) / scale
    }

    fun round(value: Double, places: Int): Double {
        val scale = 10.0.pow(places.toDouble())
        return round(value * scale) / scale
    }

}