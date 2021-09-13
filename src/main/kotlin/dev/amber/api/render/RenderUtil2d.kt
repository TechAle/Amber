package dev.amber.api.render

import com.sun.javafx.geom.Vec2d
import dev.amber.api.util.MathUtils
import dev.amber.api.variables.ABColor
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL32
import kotlin.math.*


object RenderUtil2d {

    /**
     * Setup Gl states
     */
    fun prepareGL() {
        GlStateManager.pushMatrix()
        glLineWidth(1f)
        glEnable(GL_LINE_SMOOTH)
        glEnable(GL32.GL_DEPTH_CLAMP)
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST)
        GlStateManager.disableAlpha()
        GlStateManager.shadeModel(GL_SMOOTH)
        GlStateManager.disableCull()
        GlStateManager.enableBlend()
        GlStateManager.depthMask(false)
        GlStateManager.disableTexture2D()
        GlStateManager.disableLighting()
    }

    /**
     * Reverts Gl states
     */
    fun releaseGL() {
        GlStateManager.enableTexture2D()
        GlStateManager.enableDepth()
        GlStateManager.disableBlend()
        GlStateManager.enableCull()
        GlStateManager.shadeModel(GL_FLAT)
        GlStateManager.enableAlpha()
        GlStateManager.depthMask(true)
        glDisable(GL32.GL_DEPTH_CLAMP)
        glDisable(GL_LINE_SMOOTH)
        GlStateManager.color(1f, 1f, 1f)
        glLineWidth(1f)
        GlStateManager.popMatrix()
    }

    /*
        Rect
     */

    fun drawRect(x: Float, y: Float, width: Float, height: Float, c: ABColor, once : Boolean = false) {
        if (once)
            prepareGL()
        glBegin(GL_QUADS)
        glColor4f(c.getRed().toFloat() / 255, c.getGreen().toFloat() / 255, c.getBlue().toFloat() / 255, c.getAlpha().toFloat() / 255)
        glVertex2f(x, y)
        glVertex2f(x, y + height)
        glVertex2f(x + width, y + height)
        glVertex2f(x + width, y)
        glEnd()
        if (once)
            releaseGL()
    }

    fun drawRectOutline(x: Float, y: Float, width: Float, height: Float, borderWidth: Float, borderColor: ABColor, once: Boolean = true) {
        if (once)
            prepareGL()

        // Top
        drawRect(x, y, width , borderWidth, borderColor)
        // Bottom
        drawRect(x, y + height, width, borderWidth, borderColor)
        // Left
        drawRect(x, y + borderWidth, borderWidth, height - borderWidth, borderColor)
        // Right
        drawRect(x + width, y + borderWidth, -borderWidth, height - borderWidth, borderColor)

        if (once)
            releaseGL()
    }

    fun drawRectBorder(x: Float, y: Float, width: Float, height: Float, borderWidth: Float, insideC: ABColor, borderColor: ABColor, once : Boolean = true ) {
        if (once)
            prepareGL()

        // Draw inside
        drawRect(x + borderWidth, y + borderWidth, width - borderWidth , height - borderWidth, insideC)
        /// Draw border
        drawRectOutline(x, y, width, height, borderWidth, borderColor, once)

        if (once)
            releaseGL()
    }

    /*
        Circle
     */

    fun drawCircleFilled(center: Vec2d, radius: Double, segments: Int = 0, color: ABColor, angleRange: Pair<Float, Float> = Pair(0f, 360f), once: Boolean = true) {
        if (once)
            prepareGL()
        drawArcFilled(center, radius, angleRange, segments, color)
        if (once)
            releaseGL()
    }
    private fun drawArcFilled(center: Vec2d, radius: Double, angleRange: Pair<Float, Float>, segments: Int = 0, color: ABColor) {
        val arcVertices = getArcVertices(center, radius, angleRange, segments)
        drawTriangleFan(center, arcVertices, color, false)
    }
    private fun getArcVertices(center: Vec2d, radius: Double, angleRange: Pair<Float, Float>, segments: Int): Array<Vec2d> {
        val range = max(angleRange.first, angleRange.second) - min(angleRange.first, angleRange.second)
        val seg = calcSegments(segments, radius, range)
        val segAngle = (range.toDouble() / seg.toDouble())

        return Array(seg + 1) {
            val angle = Math.toRadians(it * segAngle + angleRange.first.toDouble())
            val unRounded = Vec2d(sin(angle), -cos(angle)).times(radius).plus(center)
            Vec2d(MathUtils.round(unRounded.x, 8), MathUtils.round(unRounded.y, 8))
        }
    }
    private fun calcSegments(segmentsIn: Int, radius: Double, range: Float): Int {
        if (segmentsIn != -0) return segmentsIn
        val segments = radius * 0.5 * PI * (range / 360.0)
        return max(segments.roundToInt(), 16)
    }

    fun drawCircleOutline(center: Vec2d, radius: Double, segments: Int = 0, lineWidth: Float = 1f, color: ABColor, angleRange: Pair<Float, Float> = Pair(0f, 360f), once: Boolean = true) {
        if (once)
            prepareGL()
        drawArcOutline(center, radius, angleRange, segments, lineWidth, color)
        if (once)
            releaseGL()
    }
    private fun drawArcOutline( center: Vec2d, radius: Double, angleRange: Pair<Float, Float>, segments: Int = 0, lineWidth: Float = 1f, color: ABColor) {
        val arcVertices = getArcVertices(center, radius, angleRange, segments)
        drawLineStrip(arcVertices, lineWidth, color)
    }

    fun drawCircleBorder(center: Vec2d, radius: Double, segments: Int = 0, lineWidth: Float = 1f, insideC: ABColor, outsideC: ABColor, angleRange: Pair<Float, Float> = Pair(0f, 360f), once: Boolean = true) {
        if (once)
            prepareGL()

        drawCircleFilled(center, radius - lineWidth, segments, insideC, angleRange, false)
        drawCircleOutline(center, radius, segments, lineWidth + 2, outsideC, angleRange, false)

        if (once)
            releaseGL()
    }

    /*
        Rounded rect
     */
    /*
fun drawRoundedRect(x: Double, y: Double, width: Float, height: Float, radius: Float, c: ABColor) {
    prepareGL()

    /*
    // Draw up
    drawRect(x + radius, y, width - radius * 2, radius, color)
    // Draw down
    drawRect(x + radius, y + height, width - radius * 2,  -radius, color)
    // Draw left
    drawRect(x, y + radius, radius, height - radius * 2, color)
    // Draw Right
    drawRect(width + x, y + radius, -radius, height - radius * 2, color)
    // Draw body
    drawRect(x + radius, y + radius, width - radius * 2, height - radius * 2, color)
    */

    glBegin(GL_POLYGON)
    glColor4f(c.getRed().toFloat() / 255, c.getGreen().toFloat() / 255, c.getBlue().toFloat() / 255, c.getAlpha().toFloat() / 255)

    glVertex2d(x, y)
    glVertex2d(x + 10, y + 1)
    glVertex2d(x + 20, y + 3)
    glVertex2d(x + 30, y + 5)
    glVertex2d(x + 40, y + 10)

    glEnd()



    releaseGL()
}
*/


    /*
        Lines
     */

    fun drawLineStrip(vertices: Array<Vec2d>, lineWidth: Float = 1f, c: ABColor) {
        prepareGL()
        glLineWidth(lineWidth)

        glBegin(GL_LINE_STRIP)
        glColor4f(c.getRed().toFloat() / 255, c.getGreen().toFloat() / 255, c.getBlue().toFloat() / 255, c.getAlpha().toFloat() / 255)
        for (vertex in vertices) {
            glVertex2d(vertex.x, vertex.y)
        }
        glEnd()


        releaseGL()
        glLineWidth(1f)
    }

    /*
        Triangles
     */

    private fun drawTriangleFan(center: Vec2d, vertices: Array<Vec2d>, c: ABColor, once: Boolean = true) {
        if (once)
            prepareGL()
        glBegin(GL_TRIANGLE_FAN)
        glColor4f(c.getRed().toFloat() / 255, c.getGreen().toFloat() / 255, c.getBlue().toFloat() / 255, c.getAlpha().toFloat() / 255)
        glVertex2d(center.x, center.y)
        for (vertex in vertices) {
            glVertex2d(vertex.x, vertex.y)
        }
        glEnd()
        if (once)
            releaseGL()
    }

    /*
        Extensions
     */

    private fun Vec2d.plus(center: Vec2d): Vec2d {
        return Vec2d(this.x + center.x, this.y + center.y)
    }

    private fun Vec2d.times(radius: Double): Vec2d {
        return Vec2d(this.x * radius, this.y * radius)
    }



}


