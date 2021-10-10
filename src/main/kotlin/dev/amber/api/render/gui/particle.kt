package dev.amber.api.render.gui

import dev.amber.api.render.Element
import dev.amber.api.render.RenderUtil2d
import dev.amber.api.util.MathUtils
import dev.amber.api.variables.ABColor
import net.minecraft.util.math.Vec2f

class particle(override var x: Float, override var y: Float,
               private val speedY: Float,
               private var typeParticle: Type,
               private val height: Float,
               private val width: Float = 1f,
               private val primaryColor: ABColor,
               private val endLife: Int = 100,
               private val startAlpha : Int? = null)
               : Element() {

    private var life = 0;
    override fun render() : Boolean {

        if (life++ >= endLife)
            return false

        val startColor: ABColor

        if (startAlpha == null) {
            startColor = primaryColor
        } else {
            val percentage = MathUtils.percentage(0, endLife, life)
            startColor = ABColor(primaryColor, (startAlpha - startAlpha * percentage).toInt())
        }

        when(this.typeParticle) {
            Type.PENIS -> {
                RenderUtil2d.drawRect(Vec2f(x, y), width, height * 2 + width, startColor)
                RenderUtil2d.drawRect(Vec2f(x, y - height), -height, width, startColor)
                RenderUtil2d.drawRect(Vec2f(x + width, y - height), height, width, startColor)
            }

            Type.CROSS -> {
                RenderUtil2d.drawRect(Vec2f(x, y), width, height * 2 + width, startColor)
                RenderUtil2d.drawRect(Vec2f(x, y + height), -height, width, startColor)
                RenderUtil2d.drawRect(Vec2f(x + width, y + height), height, width, startColor)
            }

            Type.CIRCLE -> {
                RenderUtil2d.drawCircleFilled(Vec2f(x, y), height, 15, startColor)
            }

            Type.SQUARE -> {
                RenderUtil2d.drawRect(Vec2f(x, y), height, width, startColor)
            }

        }
        this.y -= speedY

        return false
    }

    enum class Type {
        PENIS, CIRCLE, SQUARE, CROSS
    }
}