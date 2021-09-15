@file:Suppress("DEPRECATION", "unused")

package dev.amber.frontend.module.modules.client


import dev.amber.api.render.RenderUtil2d
import dev.amber.api.variables.ABColor
import dev.amber.frontend.module.Module
import net.minecraft.util.math.Vec2f

/**
 * @author TechAle
 * @since 12/09/21
 */
object BottomColor : Module(category = Category.Client, "BottomColor", true) {

    override fun onRender() {
        /*
        center: Vec2f, radius: Float, segments: Int = 0, lineWidth: Float = 1f, insideC: Array<ABColor>,
        outsideC: Array<ABColor>, angleRange: Pair<Float, Float> = Pair(0f, 360f), once: Boolean = false
         */
        RenderUtil2d.drawCircleBorder(Vec2f(100f, 100f), 20f, 700, 5f,
                                        arrayOf(ABColor(255, 255, 255), ABColor(255, 255, 255), ABColor(0, 255, 255), ABColor(255, 255, 255), ABColor(255, 255, 255)),
                                        arrayOf(ABColor(0, 0, 0), ABColor(255, 0, 0), ABColor(0, 0, 0)),
                                        Pair(0f, 366f), true)

    }

}