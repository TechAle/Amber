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
        RenderUtil2d.drawRoundedRectBorder(Vec2f(100f, 100f), 100f, 100f, 5f, 10f, ABColor(255, 0, 0, 150), ABColor(0, 255, 0, 150))
    }

}