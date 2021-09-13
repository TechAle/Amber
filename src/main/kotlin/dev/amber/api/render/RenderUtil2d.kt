package dev.amber.api.render

import dev.amber.api.util.MathUtils
import dev.amber.api.variables.ABColor
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.math.Vec2f
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

    fun drawRect(Start: Vec2f, width: Float, height: Float, c: ABColor, once : Boolean = false) {
        if (once)
            prepareGL()
        glBegin(GL_QUADS)
        glColor4f(c.getRed().toFloat() / 255, c.getGreen().toFloat() / 255, c.getBlue().toFloat() / 255, c.getAlpha().toFloat() / 255)
        glVertex2f(Start.x, Start.y)
        glVertex2f(Start.x, Start.y + height)
        glVertex2f(Start.x + width, Start.y + height)
        glVertex2f(Start.x + width, Start.y)
        glEnd()
        if (once)
            releaseGL()
    }

    fun drawRectOutline(Start: Vec2f, width: Float, height: Float, borderWidth: Float, borderColor: ABColor, once: Boolean = true) {
        if (once)
            prepareGL()

        // Top
        drawRect(Start, width , borderWidth, borderColor)
        // Bottom
        drawRect(Start.add(0f, height), width, borderWidth, borderColor)
        // Left
        drawRect(Start.add(0f, borderWidth), borderWidth, height - borderWidth, borderColor)
        // Right
        drawRect(Start.add(width, borderWidth), -borderWidth, height - borderWidth, borderColor)

        if (once)
            releaseGL()
    }

    fun drawRectBorder(Start: Vec2f, width: Float, height: Float, borderWidth: Float, insideC: ABColor, borderColor: ABColor, once : Boolean = true ) {
        if (once)
            prepareGL()

        // Draw inside
        drawRect(Start.add(borderWidth, borderWidth), width - borderWidth , height - borderWidth, insideC)
        /// Draw border
        drawRectOutline(Start, width, height, borderWidth, borderColor, once)

        if (once)
            releaseGL()
    }

    /*
        Circle
     */

    fun drawCircleFilled(center: Vec2f, radius: Float, segments: Int = 0, color: ABColor, angleRange: Pair<Float, Float> = Pair(0f, 360f), once: Boolean = true) {
        if (once)
            prepareGL()
        drawArcFilled(center, radius, angleRange, segments, color)
        if (once)
            releaseGL()
    }
    private fun drawArcFilled(center: Vec2f, radius: Float, angleRange: Pair<Float, Float>, segments: Int = 0, color: ABColor) {
        val arcVertices = getArcVertices(center, radius, angleRange, segments)
        drawTriangleFan(center, arcVertices, color, false)
    }
    private fun getArcVertices(center: Vec2f, radius: Float, angleRange: Pair<Float, Float>, segments: Int): Array<Vec2f> {
        val range = max(angleRange.first, angleRange.second) - min(angleRange.first, angleRange.second)
        val seg = calcSegments(segments, radius, range)
        val segAngle = (range / seg.toFloat())

        return Array(seg + 1) {
            val angle = Math.toRadians((it * segAngle + angleRange.first).toDouble())
            val unRounded = Vec2f(sin(angle).toFloat(), (-cos(angle)).toFloat()).times(radius).add(center)
            Vec2f(MathUtils.round(unRounded.x, 8).toFloat(), MathUtils.round(unRounded.y, 8).toFloat())
        }
    }
    private fun calcSegments(segmentsIn: Int, radius: Float, range: Float): Int {
        if (segmentsIn != -0) return segmentsIn
        val segments = radius * 0.5 * PI * (range / 360.0)
        return max(segments.roundToInt(), 16)
    }

    fun drawCircleOutline(center: Vec2f, radius: Float, segments: Int = 0, lineWidth: Float = 1f, color: ABColor, angleRange: Pair<Float, Float> = Pair(0f, 360f), once: Boolean = true) {
        if (once)
            prepareGL()
        drawArcOutline(center, radius, angleRange, segments, lineWidth, color)
        if (once)
            releaseGL()
    }
    private fun drawArcOutline( center: Vec2f, radius: Float, angleRange: Pair<Float, Float>, segments: Int = 0, lineWidth: Float = 1f, color: ABColor) {
        val arcVertices = getArcVertices(center, radius, angleRange, segments)
        drawLineStrip(arcVertices, lineWidth, color)
    }

    fun drawCircleBorder(center: Vec2f, radius: Float, segments: Int = 0, lineWidth: Float = 1f, insideC: ABColor, outsideC: ABColor, angleRange: Pair<Float, Float> = Pair(0f, 360f), once: Boolean = true) {
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

    fun drawRoundedRect(Start: Vec2f, width: Float, height: Float, radius: Float, c: ABColor) {
        prepareGL()

        glBegin(GL_QUADS)
        glColor4f(c.getRed().toFloat() / 255, c.getGreen().toFloat() / 255, c.getBlue().toFloat() / 255, c.getAlpha().toFloat() / 255)

        // Draw up
        drawRect(Start.add(radius, 0f), width - radius * 2, radius, c)
        // Draw down
        drawRect(Start.add(radius, height), width - radius * 2,  -radius, c)
        // Draw left
        drawRect(Start.add(0f, radius), radius, height - radius * 2, c)
        // Draw Right
        drawRect(Start.add(width, radius), -radius, height - radius * 2, c)
        // Draw body
        drawRect(Start.add(radius, radius), width - radius * 2, height - radius * 2, c)

        glEnd()

        releaseGL()
    }



    /*
        Lines
     */

    fun drawLineStrip(vertices: Array<Vec2f>, lineWidth: Float = 1f, c: ABColor) {
        prepareGL()
        glLineWidth(lineWidth)

        glBegin(GL_LINE_STRIP)
        glColor4f(c.getRed().toFloat() / 255, c.getGreen().toFloat() / 255, c.getBlue().toFloat() / 255, c.getAlpha().toFloat() / 255)
        for (vertex in vertices) {
            glVertex2f(vertex.x, vertex.y)
        }
        glEnd()


        releaseGL()
        glLineWidth(1f)
    }

    fun drawLine(start: Vec2f, end: Vec2f, lineWidth: Float = 1f, c: ABColor, once: Boolean = false) {
        if (once) {
            prepareGL()
        }

        glLineWidth(lineWidth)
        glBegin(GL_LINES)
        glColor4f(c.getRed().toFloat() / 255, c.getGreen().toFloat() / 255, c.getBlue().toFloat() / 255, c.getAlpha().toFloat() / 255)
        glVertex2f(start.x, start.y)
        glVertex2f(end.x, end.y)
        glEnd()
        glLineWidth(1f)

        if (once) {
            releaseGL()
        }
    }

    /*
        Triangles
     */

    private fun drawTriangleFan(center: Vec2f, vertices: Array<Vec2f>, c: ABColor, once: Boolean = true) {
        if (once)
            prepareGL()
        glBegin(GL_TRIANGLE_FAN)
        glColor4f(c.getRed().toFloat() / 255, c.getGreen().toFloat() / 255, c.getBlue().toFloat() / 255, c.getAlpha().toFloat() / 255)
        glVertex2f(center.x, center.y)
        for (vertex in vertices) {
            glVertex2f(vertex.x, vertex.y)
        }
        glEnd()
        if (once)
            releaseGL()
    }

    /*
        Extensions
     */

    private fun Vec2f.add(center: Vec2f): Vec2f {
        return Vec2f(this.x + center.x, this.y + center.y)
    }

    private fun Vec2f.add(xVal: Float, yVal: Float): Vec2f {
        return Vec2f(this.x + xVal, this.y + yVal)
    }

    private fun Vec2f.times(radius: Float): Vec2f {
        return Vec2f(this.x * radius, this.y * radius)
    }



}


