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
            Start: Vec2f, width: Float, height: Float, radius: Float, widthBorder: Float,
            colors: Array<ABColor>, once: Boolean = false, topBottom: Boolean = false
         */
        RenderUtil2d.drawRoundedRectOutline(Vec2f(100f, 100f), 100f, 100f, 5f, 5f,
                                            arrayOf(ABColor(255, 0, 0), ABColor(0, 255, 0), ABColor(0, 0, 255), ABColor(255, 255, 255)),
                                            true)
    }

}