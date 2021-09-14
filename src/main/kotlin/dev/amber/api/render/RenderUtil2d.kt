package dev.amber.api.render

import dev.amber.api.util.MathUtils
import dev.amber.api.variables.ABColor
import net.minecraft.util.math.Vec2f
import org.lwjgl.opengl.GL11.*
import kotlin.math.*


object RenderUtil2d {

    /*
        Rect
     */
    fun drawRect(Start: Vec2f, width: Float, height: Float, c: ABColor, once : Boolean = false) {
        if (once)
            VertexUtil.prepareGl()
        glBegin(GL_QUADS)
        c.glColor()
        VertexUtil.add(Start)
        VertexUtil.add(Start.add(0f, height))
        VertexUtil.add(Start.add(width, height))
        VertexUtil.add(Start.add(width, 0f))
        glEnd()
        if (once)
            VertexUtil.releaseGL()
    }

    fun drawRectOutline(Start: Vec2f, width: Float, height: Float, borderWidth: Float, borderColor: ABColor, once: Boolean = true) {
        if (once)
            VertexUtil.prepareGl()

        // Top
        drawRect(Start, width , borderWidth, borderColor)
        // Bottom
        drawRect(Start.add(0f, height), width, borderWidth, borderColor)
        // Left
        drawRect(Start.add(0f, borderWidth), borderWidth, height - borderWidth, borderColor)
        // Right
        drawRect(Start.add(width, borderWidth), -borderWidth, height - borderWidth, borderColor)

        if (once)
            VertexUtil.releaseGL()
    }

    fun drawRectBorder(Start: Vec2f, width: Float, height: Float, borderWidth: Float, insideC: ABColor, borderColor: ABColor, once : Boolean = true ) {
        if (once)
            VertexUtil.prepareGl()

        // Draw inside
        drawRect(Start.add(borderWidth, borderWidth), width - borderWidth , height - borderWidth, insideC)
        /// Draw border
        drawRectOutline(Start, width, height, borderWidth, borderColor, once)

        if (once)
            VertexUtil.releaseGL()
    }

    /*
        Circle
     */

    fun drawCircleFilled(center: Vec2f, radius: Float, segments: Int = 0, color: ABColor, angleRange: Pair<Float, Float> = Pair(0f, 360f), once: Boolean = true) {
        if (once)
            VertexUtil.prepareGl()
        drawArcFilled(center, radius, angleRange, segments, color)
        if (once)
            VertexUtil.releaseGL()
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
            VertexUtil.prepareGl()
        drawArcOutline(center, radius, angleRange, segments, lineWidth, color)
        if (once)
            VertexUtil.releaseGL()
    }
    private fun drawArcOutline( center: Vec2f, radius: Float, angleRange: Pair<Float, Float>, segments: Int = 0, lineWidth: Float = 1f, color: ABColor) {
        val arcVertices = getArcVertices(center, radius, angleRange, segments)
        drawLineStrip(arcVertices, lineWidth, color)
    }

    fun drawCircleBorder(center: Vec2f, radius: Float, segments: Int = 0, lineWidth: Float = 1f, insideC: ABColor, outsideC: ABColor, angleRange: Pair<Float, Float> = Pair(0f, 360f), once: Boolean = true) {
        if (once)
            VertexUtil.prepareGl()

        drawCircleFilled(center, radius - lineWidth, segments, insideC, angleRange, false)
        drawCircleOutline(center, radius, segments, lineWidth + 2, outsideC, angleRange, false)

        if (once)
            VertexUtil.releaseGL()
    }

    /*
        Rounded rect
     */

    fun drawRoundedRect(Start: Vec2f, width: Float, height: Float, radius: Float, c: ABColor) {
        VertexUtil.prepareGl()

        /// Rectangles
        // Draw body
        drawRect(Start.add(radius, 0f), width - radius * 2,  height, c)
        // Draw left
        drawRect(Start.add(0f, radius), radius, height - radius * 2, c)
        // Draw Right
        drawRect(Start.add(width, radius), -radius, height - radius * 2, c)
        /// Circles
        // Top right
        drawCircleFilled(Start.add(width - radius, radius), radius, 90, c, Pair(0f, 90f), false)
        // Top left
        drawCircleFilled(Start.add(radius, radius), radius, 90, c, Pair(270f, 360f), false)
        // Bottom left
        drawCircleFilled(Start.add(radius, height - radius), radius, 90, c, Pair(180f, 270f), false)
        // Bottom right
        drawCircleFilled(Start.add(width - radius, height - radius), radius, 90, c, Pair(90f, 180f), false)

        VertexUtil.releaseGL()
    }

    fun drawRoundedRectOutline(Start: Vec2f, width: Float, height: Float, radius: Float, widthBorder: Float, c: ABColor, once: Boolean = true) {
        if (once)
            VertexUtil.prepareGl()

        /// Rectangle
        // Top
        drawLine(Start.add(radius, 0f), Start.add(width - radius , 0f), widthBorder, c, false)
        // Bottom
        drawLine(Start.add(radius, height), Start.add(width - radius, height), widthBorder, c, false)
        // Left
        drawLine(Start.add(0f, radius), Start.add(0f, height - radius ), widthBorder, c, false)
        // Right
        drawLine(Start.add(width, radius), Start.add(width, height - radius ), widthBorder, c, false)
        /// Circles
        // Top right
        drawCircleOutline(Start.add(width - radius, radius), radius, 0, widthBorder, c, Pair(0f, 90f), false)
        // Top left
        drawCircleOutline(Start.add(radius, radius), radius, 0, widthBorder, c, Pair(270f, 360f), false)
        // Bottom left
        drawCircleOutline(Start.add(radius, height - radius), radius, 0, widthBorder, c, Pair(180f, 270f), false)
        // Bottom right
        drawCircleOutline(Start.add(width - radius, height - radius), radius, 0, widthBorder, c, Pair(90f, 180f), false)

        if (once)
            VertexUtil.releaseGL()
    }

    fun drawRoundedRectBorder(Start: Vec2f, width: Float, height: Float, radius: Float, widthBorder: Float, cInside: ABColor, cOutside: ABColor, once: Boolean = true) {
        if (once)
            VertexUtil.prepareGl()
        drawRoundedRect(Start, width, height, radius, cInside)
        drawRoundedRectOutline(Start, width, height, radius, widthBorder, cOutside)
        if (once)
            VertexUtil.releaseGL()
    }

    /*
        Lines
     */

    fun drawLineStrip(vertices: Array<Vec2f>, lineWidth: Float = 1f, c: ABColor) {
        VertexUtil.prepareGl()
        glLineWidth(lineWidth)

        glBegin(GL_LINE_STRIP)
        c.glColor()
        for (vertex in vertices) {
            VertexUtil.add(vertex)
        }
        glEnd()


        VertexUtil.releaseGL()
        glLineWidth(1f)
    }

    fun drawLine(start: Vec2f, end: Vec2f, lineWidth: Float = 1f, c: ABColor, once: Boolean = false) {
        if (once) {
            VertexUtil.prepareGl()
        }

        glLineWidth(lineWidth)
        glBegin(GL_LINES)
        c.glColor()
        glVertex2f(start.x, start.y)
        glVertex2f(end.x, end.y)
        glEnd()

        if (once) {
            VertexUtil.releaseGL()
        } else  glLineWidth(1f)
    }

    /*
        Triangles
     */

    private fun drawTriangleFan(center: Vec2f, vertices: Array<Vec2f>, c: ABColor, once: Boolean = true) {
        if (once)
            VertexUtil.prepareGl()
        glBegin(GL_TRIANGLE_FAN)
        c.glColor()
        glVertex2f(center.x, center.y)
        for (vertex in vertices) {
            glVertex2f(vertex.x, vertex.y)
        }
        glEnd()
        if (once)
            VertexUtil.releaseGL()
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


