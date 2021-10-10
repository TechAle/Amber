package dev.amber.api.render.gui

import dev.amber.api.render.Element
import dev.amber.api.render.RenderUtil2d
import dev.amber.api.util.ColorUtils
import dev.amber.api.util.MathUtils
import dev.amber.api.variables.ABColor
import net.minecraft.util.math.Vec2f

class particle(override var x: Float, override var y: Float,
               private val speedY: Float,
               private var typeParticle: type,
               private val height: Float,
               private val width: Float = 1f,
               private val primaryColor: ABColor,
               private val secondaryColor: ABColor? = null,
               private val endLife: Int = 100,
               private val startAlpha : Int? = null,
               private val endAlpha : Int? = null)
               : Element() {

    private var life = 0;
    override fun render() : Boolean {

        if (life++ >= endLife)
            return false

        val startColor: ABColor
        val endColor: ABColor?

        if (startAlpha == null && endAlpha == null) {
            startColor = primaryColor
            endColor = secondaryColor
        } else {
            val percentage = MathUtils.percentage(0, endLife, life)
            val alpha = (startAlpha!! - startAlpha * percentage).toInt()
            startColor = ABColor(primaryColor, alpha)
            if (secondaryColor != null)
                endColor = ABColor(secondaryColor, alpha)
            else endColor = null
        }

        when(this.typeParticle) {
            type.PENIS -> {
                if (endColor == null) {
                    RenderUtil2d.drawRect(Vec2f(x, y), width, height * 2 + width, startColor)
                    RenderUtil2d.drawRect(Vec2f(x, y - height), -height, width, startColor)
                    RenderUtil2d.drawRect(Vec2f(x + width, y - height), height, width, startColor)
                } else {
                    RenderUtil2d.drawRect(Vec2f(x, y), width, height, startColor)
                    RenderUtil2d.drawRect(Vec2f(x, y + height + width), width, height, startColor)
                    RenderUtil2d.drawRect(Vec2f(x, y - height), -height, width, endColor)
                    RenderUtil2d.drawRect(Vec2f(x + width, y - height), height, width, endColor)
                    RenderUtil2d.drawRect(Vec2f(x, y + height), width, width, ColorUtils.average(startColor, endColor))
                }
            }

            type.STAR -> {
                if (endColor == null) {
                    RenderUtil2d.drawRect(Vec2f(x, y), width, height * 2 + width, startColor)
                    RenderUtil2d.drawRect(Vec2f(x, y + height), -height, width, startColor)
                    RenderUtil2d.drawRect(Vec2f(x + width, y + height), height, width, startColor)
                } else {
                    RenderUtil2d.drawRect(Vec2f(x, y), width, height, startColor)
                    RenderUtil2d.drawRect(Vec2f(x, y + height + width), width, height, startColor)
                    RenderUtil2d.drawRect(Vec2f(x, y + height), -height, width, endColor)
                    RenderUtil2d.drawRect(Vec2f(x + width, y + height), height, width, endColor)
                    RenderUtil2d.drawRect(Vec2f(x, y + height), width, width, ColorUtils.average(startColor, endColor))
                }
            }

            type.CIRCLE -> {
                if (endColor == null)
                    RenderUtil2d.drawCircleFilled(Vec2f(x, y), height, 15, startColor)
                else RenderUtil2d.drawCircleBorder(Vec2f(x, y), height, 15, 1f, startColor, endColor)
            }

            type.SQUARE -> {
                if (endColor == null)
                    RenderUtil2d.drawRect(Vec2f(x, y), height, width, startColor)
                else RenderUtil2d.drawRectBorder(Vec2f(x, y), height, width, 1f, primaryColor, endColor)
            }

        }
        this.y -= speedY

        return false
    }

    enum class type {
        PENIS, CIRCLE, SQUARE, STAR
    }
}