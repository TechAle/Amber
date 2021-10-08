package dev.amber.api.render.gui

import dev.amber.api.render.Element
import dev.amber.api.render.RenderUtil2d
import dev.amber.api.util.ColorUtils
import dev.amber.api.variables.ABColor
import net.minecraft.util.math.Vec2f

class particle(x: Float, y: Float,
               private val speedY: Float,
               private var typeParticle: type,
               private val height: Float,
               private val width: Float = 1f,
               private val primaryColor: ABColor,
               private val secondaryColor: ABColor? = null)
               : Element(x, y) {


    override fun render() {
        when(this.typeParticle) {
            type.STAR -> {
                if (secondaryColor == null) {
                    RenderUtil2d.drawRect(Vec2f(x, y), width, height * 2 + width, primaryColor)
                    RenderUtil2d.drawRect(Vec2f(x, y - height), -height, width, primaryColor)
                    RenderUtil2d.drawRect(Vec2f(x + width, y - height), height, width, primaryColor)
                } else {
                    RenderUtil2d.drawRect(Vec2f(x, y), width, height, primaryColor)
                    RenderUtil2d.drawRect(Vec2f(x, y + height + width), width, height, primaryColor)
                    RenderUtil2d.drawRect(Vec2f(x, y - height), -height, width, secondaryColor)
                    RenderUtil2d.drawRect(Vec2f(x + width, y - height), height, width, secondaryColor)
                    RenderUtil2d.drawRect(Vec2f(x, y + height), width, width, ColorUtils.average(primaryColor, secondaryColor))
                }
            }

            type.CIRCLE -> {
                if (secondaryColor == null)
                    RenderUtil2d.drawCircleFilled(Vec2f(x, y), height, 360, primaryColor)
                else RenderUtil2d.drawCircleBorder(Vec2f(x, y), height, 360, 1f, primaryColor, secondaryColor)
            }

            type.SQUARE -> {
                if (secondaryColor == null)
                    RenderUtil2d.drawRect(Vec2f(x, y), height, width, primaryColor)
                else RenderUtil2d.drawRectBorder(Vec2f(x, y), height, width, 1f, primaryColor, secondaryColor)
            }

        }
        this.y += speedY
    }

    enum class type {
        STAR, CIRCLE, SQUARE
    }
}