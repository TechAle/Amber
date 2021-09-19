@file:Suppress("DEPRECATION", "unused")

package dev.amber.frontend.module.modules.client


import dev.amber.api.render.RenderUtil2d
import dev.amber.api.variables.ABColor
import dev.amber.frontend.module.Module
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.Vec2f

/**
 * @author TechAle
 * @since 12/09/21
 */
object BottomColor : Module(category = Category.Client, "BottomColor", true) {

    override fun onRender() {
        /*
        start: Vec2f, end: Vec2f, lineWidth: Float = 1f, c: Array<ABColor>, once: Boolean = false
         */
        RenderUtil2d.showPicture(0, 0, ResourceLocation("amber/img/testresources.png"), 100, 100)
    }

}