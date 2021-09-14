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
            Start: Vec2f, width: Float, height: Float, borderWidth: Float, insideC: Array<ABColor>, insideTopBottom: Boolean = false,
                       borderColor: Array<ABColor>, borderTopBottom: Boolean = false, once : Boolean = false
         */
        RenderUtil2d.drawRectBorder(Vec2f(100f, 100f), 100f, 100f, 10f,
                arrayOf(ABColor(255, 0, 0), ABColor(0, 0, 255)), false,
                arrayOf(ABColor(255, 255, 0), ABColor(0, 255, 255)), false, once = true)
    }

}