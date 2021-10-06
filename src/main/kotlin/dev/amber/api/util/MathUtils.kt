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

    fun percentage(start: Int, end: Int, now: Int) : Float {
        return (now - start) / (end - start).toFloat()
    }

    fun percentage(start: Float, end: Float, now: Float) : Float {
        return (now - start) / (end - start)
    }

}