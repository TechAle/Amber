package dev.amber.api.util

import dev.amber.api.variables.ABColor

object ColorUtils {

    fun average(start: ABColor, end: ABColor) : ABColor {
        return ABColor(start.red - (start.red - end.red) / 2,
                       start.green - (start.green - end.green) / 2,
                       start.blue - (start.blue - end.blue) / 2,
                       start.alpha - (start.alpha - end.alpha) / 2)
    }

}