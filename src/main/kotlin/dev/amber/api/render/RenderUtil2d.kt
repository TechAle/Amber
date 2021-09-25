package dev.amber.api.render

/**
 * @author TechAle
 * @since 12/09/21
 */

import com.mojang.realmsclient.gui.ChatFormatting
import dev.amber.api.util.Globals.mc
import dev.amber.api.util.MathUtils
import dev.amber.api.variables.ABColor
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.Vec2f
import org.lwjgl.opengl.GL11.*
import kotlin.math.*
import net.minecraft.client.Minecraft

import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.BufferBuilder
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.realms.RealmsScreen.blit
import org.lwjgl.opengl.GL11
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import javax.imageio.ImageIO


object RenderUtil2d {

    //region Rect

    /// Normal rect
    // Normal
    fun drawRect(Start: Vec2f, width: Float, height: Float, c: ABColor, once : Boolean = false) {
        // Prepare opengl
        if (once)
            VertexUtil.prepareGl()

        // We are drawing quads lol
        glBegin(GL_QUADS)
        // Prepare color and add vertices
        c.glColor()
        VertexUtil.add(Start)
        VertexUtil.add(Start.add(0f, height))
        VertexUtil.add(Start.add(width, height))
        VertexUtil.add(Start.add(width, 0f))

        // End and release gl
        glEnd()
        if (once)
            VertexUtil.releaseGL()
    }
    // Gradient
    fun drawRect(Start: Vec2f, width: Float, height: Float, once : Boolean = false, colors: Array<ABColor>, topBottom: Boolean = false) {

        when(colors.size) {
            // No colors, return
            0 -> return
            // Draw normal rect
            1 -> drawRect(Start, width, height, colors[0], once)
            // Every other cases
            else -> {
                // Setup colors depending on the size
                val arr =  when (colors.size) {
                    2, 3 -> if (topBottom) arrayOf(colors[0], colors[1], colors[1], colors[0])
                            else arrayOf(colors[0], colors[0], colors[1], colors[1])
                    else -> Array(4) {colors[it]}
                }

                // Prepare gl
                if (once)
                    VertexUtil.prepareGl()

                // Draw with vertixes (and relative colors
                glBegin(GL_QUADS)
                VertexUtil.add(Start, arr[0])
                VertexUtil.add(Start.add(0f, height), arr[1])
                VertexUtil.add(Start.add(width, height), arr[2])
                VertexUtil.add(Start.add(width, 0f), arr[3])
                // End
                glEnd()

                // Release gl
                if (once)
                    VertexUtil.releaseGL()
            }
        }
    }

    /// Outline rect
    // Normal
    fun drawRectOutline(Start: Vec2f, width: Float, height: Float, borderWidth: Float, borderColor: ABColor, once: Boolean = false) {
        // Prepare
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

        // Release
        if (once)
            VertexUtil.releaseGL()
    }
    // Gradient
    fun drawRectOutline(Start: Vec2f, width: Float, height: Float, borderWidth: Float, once : Boolean = false, colors: Array<ABColor>, topBottom: Boolean = false) {

        when(colors.size) {
            // No colors, return
            0 -> return
            // Draw normal rect
            1 -> drawRectOutline(Start, width, height, borderWidth, colors[0], once)
            else -> {
                val arr =  when (colors.size) {
                    2, 3 -> if (topBottom) arrayOf(colors[0], colors[1], colors[1], colors[0])
                    else arrayOf(colors[0], colors[0], colors[1], colors[1])
                    else -> Array(4) {colors[it]}
                }

                // Prepare gl
                if (once)
                    VertexUtil.prepareGl()

                /// Vertices
                // Top left
                drawRect(Start, borderWidth, borderWidth, arr[0], false)
                // Top right
                drawRect(Start.add(width, 0f), -borderWidth, borderWidth, arr[1], false)
                // Bottom right
                drawRect(Start.add(width, height), -borderWidth, -borderWidth, arr[2], false)
                // Bottom left
                drawRect(Start.add(0f, height), borderWidth, -borderWidth, arr[3], false)
                /// Gradient
                // Top
                drawRect(Start.add(borderWidth, 0f), width - borderWidth*2, borderWidth, false, arrayOf(arr[0], arr[1]), topBottom = false)
                // Right
                drawRect(Start.add(width, borderWidth), -borderWidth, height - borderWidth, false, arrayOf(arr[1], arr[2]), topBottom = true)
                // Bottom
                drawRect(Start.add(borderWidth, height), width - borderWidth*2, -borderWidth, false, arrayOf(arr[3], arr[2]), topBottom = false)
                // Left
                drawRect(Start.add(0f, borderWidth), borderWidth, height - borderWidth, false, arrayOf(arr[0], arr[3]), topBottom = true)

                // Release gl
                if (once)
                    VertexUtil.releaseGL()
            }
        }

    }

    /// Border rect
    // Normal
    fun drawRectBorder(Start: Vec2f, width: Float, height: Float, borderWidth: Float, insideC: ABColor, borderColor: ABColor, once : Boolean = false ) {
        if (once)
            VertexUtil.prepareGl()

        // Draw inside
        drawRect(Start.add(borderWidth, borderWidth), width - borderWidth , height - borderWidth, insideC)
        /// Draw border
        drawRectOutline(Start, width, height, borderWidth, borderColor, once)

        if (once)
            VertexUtil.releaseGL()
    }
    // Gradient
    fun drawRectBorder(Start: Vec2f, width: Float, height: Float, borderWidth: Float, insideC: Array<ABColor>, insideTopBottom: Boolean = false,
                       borderColor: Array<ABColor>, borderTopBottom: Boolean = false, once : Boolean = false ) {
        if (once)
            VertexUtil.prepareGl()

        // Draw inside
        drawRect(Start.add(borderWidth, borderWidth), width - borderWidth, height - borderWidth, false, insideC, insideTopBottom)
        /// Draw border
        drawRectOutline(Start, width, height, borderWidth, false, borderColor, borderTopBottom)

        if (once)
            VertexUtil.releaseGL()
    }

    //endregion

    //region Circle

    /// Filled circle
    // Normal
    fun drawCircleFilled(center: Vec2f, radius: Float, segments: Int = 0, color: ABColor, angleRange: Pair<Float, Float> = Pair(0f, 360f), once: Boolean = false, onlyVertex: Boolean = false) {
        // Prepare gl
        if (once)
            VertexUtil.prepareGl()

        // Draw the circle main function
        drawArcFilled(center, radius, angleRange, segments, color, onlyVertex)

        // Release gl
        if (once)
            VertexUtil.releaseGL()
    }
    private fun drawArcFilled(center: Vec2f, radius: Float, angleRange: Pair<Float, Float>, segments: Int = 0, color: ABColor, onlyVertex: Boolean = false) {
        // Get segments
        val arcVertices = getArcVertices(center, radius, angleRange, segments)
        // Draw everything
        drawTriangleFan(center, arcVertices, color, false, onlyVertex)
    }
    private fun getArcVertices(center: Vec2f, radius: Float, angleRange: Pair<Float, Float>, segments: Int): Array<Vec2f> {
        // I dunno what's here, some geometry stuff i guess. Thanks lambda
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
        // I dunno what's here, thanks lambda
        if (segmentsIn != -0) return segmentsIn
        val segments = radius * 0.5 * PI * (range / 360.0)
        return max(segments.roundToInt(), 16)
    }

    // Gradient
    fun drawCircleFilled(center: Vec2f, radius: Float, segments: Int = 0, color: Array<ABColor>, angleRange: Pair<Float, Float> = Pair(0f, 360f), once: Boolean = false) {

        when (color.size) {
            // No circle
            0 -> return
            // 1 color circle
            1 -> drawCircleFilled(center, radius, segments, color[0], angleRange, once)
            else -> {
                // Prepearel
                if (once)
                    VertexUtil.prepareGl()
                // Main function drawing circle
                drawArcFilled(center, radius, angleRange, segments, color)
                // Release
                if (once)
                    VertexUtil.releaseGL()
            }

        }
    }
    private fun drawArcFilled(center: Vec2f, radius: Float, angleRange: Pair<Float, Float>, segments: Int = 0, color: Array<ABColor>) {
        // Get vertices
        val arcVertices = getArcVertices(center, radius, angleRange, segments)
        // Draw
        drawTriangleFan(center, arcVertices, getGradientVertices(color, arcVertices), false)
    }

    fun getGradientVertices(color: Array<ABColor>, arcVertices: Array<Vec2f>) : ArrayList<ABColor> {
        /*
            There, we have to calculate for each vertices the relative color.
         */
        // First we have to understand the number od divisions (we remove 1 because the first color is at beginning)
        val sizes = color.size.toInt() - 1
        val pieces = arcVertices.size / (sizes.toFloat())
        // Output
        var finalColors = arrayListOf<ABColor>()
        for (i in 0..color.size - 2) {
            // Get rgba
            val red = color[i].red
            val blue = color[i].blue
            var green = color[i].green
            var alpha = color[i].alpha
            // Here we have to take the percent of he changes
            val rChange = (color[i + 1].red - red)/pieces
            val gChange = (color[i + 1].green - green)/pieces
            val bChange = (color[i + 1].blue - blue)/pieces
            val aChange = (color[i + 1].alpha - alpha)/pieces
            // And for every piece
            for(j in 0..sizes) {
                // We add the relative color going percent (from 0% to 100%)
                finalColors.add(ABColor(red + (rChange * j).toInt(), green + (gChange*j).toInt(), blue + (bChange*j).toInt(), alpha + (aChange*j).toInt()))
            }

        }

        /*
            For some reasons, sometimes, the number of colors is not excact the same.
            We need it to be excact the same so, instead of debugging and making the entire
            code above 1000 complex, i just hard patch it by adding/removing colors
         */
        // If we are above the limit
        if (finalColors.size > arcVertices.size) {
            // Remove till it's the same
            while (finalColors.size != arcVertices.size)
                finalColors.removeLast()
        // If we are belove
        } else if (finalColors.size < arcVertices.size)
            // Add the last color till it's the same
            while (finalColors.size != arcVertices.size)
                finalColors.add(color[color.size - 1])

        // Return output
        return finalColors
    }

    /// Outline circle
    // Normal
    fun drawCircleOutline(center: Vec2f, radius: Float, segments: Int = 0, lineWidth: Float = 1f, color: ABColor, angleRange: Pair<Float, Float> = Pair(0f, 360f), once: Boolean = false) {
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

    // Gradient
    fun drawCircleOutline(center: Vec2f, radius: Float, segments: Int = 0, lineWidth: Float = 1f, color: Array<ABColor>, angleRange: Pair<Float, Float> = Pair(0f, 360f), once: Boolean = false) {
        // Prepare gl
        if (once)
            VertexUtil.prepareGl()

        // Main function drawing circle
        drawArcOutline(center, radius, angleRange, segments, lineWidth, color, once)
        // Release gl
        if (once)
            VertexUtil.releaseGL()
    }
    private fun drawArcOutline( center: Vec2f, radius: Float, angleRange: Pair<Float, Float>, segments: Int = 0, lineWidth: Float = 1f, color: Array<ABColor>, once: Boolean = false) {
        // Get vertices
        val arcVertices = getArcVertices(center, radius, angleRange, segments)
        // Draw
        drawLineStrip(arcVertices, lineWidth, getGradientVertices(color, arcVertices), once)
    }


    /// Border circle
    // Normal
    fun drawCircleBorder(center: Vec2f, radius: Float, segments: Int = 0, lineWidth: Float = 1f, insideC: ABColor, outsideC: ABColor, angleRange: Pair<Float, Float> = Pair(0f, 360f), once: Boolean = false) {
        // Prepare gl
        if (once)
            VertexUtil.prepareGl()

        // Draw inside
        drawCircleFilled(center, radius - lineWidth, segments, insideC, angleRange, false)
        // Draw outside
        drawCircleOutline(center, radius, segments, lineWidth + 2, outsideC, angleRange, false)

        // Release
        if (once)
            VertexUtil.releaseGL()
    }
    // Gradient
    fun drawCircleBorder(center: Vec2f, radius: Float, segments: Int = 0, lineWidth: Float = 1f, insideC: Array<ABColor>, outsideC: Array<ABColor>, angleRange: Pair<Float, Float> = Pair(0f, 360f), once: Boolean = false) {
        // Prepare gl
        if (once)
            VertexUtil.prepareGl()

        // Inside
        drawCircleFilled(center, radius - lineWidth, segments, insideC, angleRange, false)
        // Outside
        drawCircleOutline(center, radius - lineWidth/2 - .5f, segments, lineWidth + 2, outsideC, angleRange, false)

        // Relase gl
        if (once)
            VertexUtil.releaseGL()
    }

    //endregion

    //region Rounded Rect

    /// Fill
    // Normal
    fun drawRoundedRect(Start: Vec2f, width: Float, height: Float, radius: Float, c: ABColor, once: Boolean = false) {
        if (once)
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

        if (once)
            VertexUtil.releaseGL()
    }
    // Gradient
    fun drawRoundedRect(Start: Vec2f, width: Float, height: Float, radius: Float, colors: Array<ABColor>, once: Boolean = false, topBottom: Boolean = false) {

        when(colors.size) {
            0 -> return
            1 -> drawRoundedRect(Start, width, height, radius, colors[0], once)
            else -> {
                // Prepare gl
                if (once)
                    VertexUtil.prepareGl()

                val arr =  when (colors.size) {
                    2, 3 -> if (topBottom) arrayOf(colors[0], colors[1], colors[1], colors[0])
                        else arrayOf(colors[0], colors[0], colors[1], colors[1])
                    else -> Array(4) {colors[it]}
                }

                /// Border
                /// Gradient
                // Top
                drawRect(Start.add(radius, 0f), width - radius*2, radius, false, arrayOf(arr[0], arr[1]), topBottom = false)
                // Right
                drawRect(Start.add(width, radius), -radius, height - radius*2, false, arrayOf(arr[1], arr[2]), topBottom = true)
                // Bottom
                drawRect(Start.add(radius, height), width - radius*2, -radius, false, arrayOf(arr[3], arr[2]), topBottom = false)
                // Left
                drawRect(Start.add(0f, radius), radius, height - radius*2, false, arrayOf(arr[0], arr[3]), topBottom = true)
                // Body
                drawRect(Start.add(radius, radius), width - radius * 2, height - radius*2, false, arrayOf(arr[0], arr[3], arr[2], arr[1]))
                /// Circles
                // Top right
                drawCircleFilled(Start.add(width - radius, radius), radius, 90, arr[1], Pair(0f, 90f), false)
                // Top left
                drawCircleFilled(Start.add(radius, radius), radius, 90, arr[0], Pair(270f, 360f), false)
                // Bottom left
                drawCircleFilled(Start.add(radius, height - radius), radius, 90, arr[3], Pair(180f, 270f), false)
                // Bottom right
                drawCircleFilled(Start.add(width - radius, height - radius), radius, 90, arr[2], Pair(90f, 180f), false)

                /*
                    Old rounded rect. This doesnt really work, it create some shits render bugs
                /// Body
                glBegin(GL_POLYGON)
                /// Circles
                // Top left
                drawCircleFilled(Start.add(radius, radius), radius, 90, arr[0], Pair(270f, 360f), false, true)
                // Top right
                drawCircleFilled(Start.add(width - radius, radius), radius, 90, arr[1], Pair(0f, 90f), false, true)
                // Bottom right
                drawCircleFilled(Start.add(width - radius, height - radius), radius, 90, arr[2], Pair(90f, 180f), false, true)
                // Bottom left
                drawCircleFilled(Start.add(radius, height - radius), radius, 90, arr[3], Pair(180f, 270f), false, true)
                glEnd()*/
                // Relase gl
                if (once)
                    VertexUtil.releaseGL()
                }
        }
    }

    /// Outline
    // Normal
    fun drawRoundedRectOutline(Start: Vec2f, width: Float, height: Float, radius: Float, widthBorder: Float, c: ABColor, once: Boolean = false) {
        if (once)
            VertexUtil.prepareGl()

        /// Rectangle
        // Top
        drawLine(Start.add(radius, 0f), Start.add(width - radius , 0f), widthBorder, c)
        // Bottom
        drawLine(Start.add(radius, height), Start.add(width - radius, height), widthBorder, c)
        // Left
        drawLine(Start.add(0f, radius), Start.add(0f, height - radius ), widthBorder, c)
        // Right
        drawLine(Start.add(width, radius), Start.add(width, height - radius ), widthBorder, c)
        /// Circles
        // Top right
        drawCircleOutline(Start.add(width - radius, radius), radius, 0, widthBorder, c, Pair(0f, 90f))
        // Top left
        drawCircleOutline(Start.add(radius, radius), radius, 0, widthBorder, c, Pair(270f, 360f))
        // Bottom left
        drawCircleOutline(Start.add(radius, height - radius), radius, 0, widthBorder, c, Pair(180f, 270f))
        // Bottom right
        drawCircleOutline(Start.add(width - radius, height - radius), radius, 0, widthBorder, c, Pair(90f, 180f))

        if (once)
            VertexUtil.releaseGL()
    }
    // Gradient
    fun drawRoundedRectOutline(Start: Vec2f, width: Float, height: Float, radius: Float, widthBorder: Float, colors: Array<ABColor>, once: Boolean = false, topBottom: Boolean = false) {

        when(colors.size) {
            0 -> return
            1 -> drawRoundedRectOutline(Start, width, height, radius, widthBorder, colors[0])
            else -> {

                if (once)
                    VertexUtil.prepareGl()

                val arr =  when (colors.size) {
                    2, 3 -> {if (topBottom) arrayOf(colors[0], colors[1], colors[1], colors[0])
                            else arrayOf(colors[0], colors[0], colors[1], colors[1])}
                    else -> Array(4) {colors[it]}
                }

                /// Rectangle
                // Top

                drawLine(Start.add(radius, 0f), Start.add(width - radius , 0f), widthBorder, arr[0], arr[1])
                // Bottom
                drawLine(Start.add(radius, height), Start.add(width - radius, height), widthBorder, arr[3], arr[2])
                // Left
                drawLine(Start.add(0f, radius), Start.add(0f, height - radius ), widthBorder, arr[0], arr[3])
                // Right
                drawLine(Start.add(width, radius), Start.add(width, height - radius ), widthBorder, arr[1], arr[2])
                /// Circles
                // Top right
                drawCircleOutline(Start.add(width - radius, radius), radius, 0, widthBorder, arr[1], Pair(0f, 90f))
                // Top left
                drawCircleOutline(Start.add(radius, radius), radius, 0, widthBorder, arr[0], Pair(270f, 360f))
                // Bottom left
                drawCircleOutline(Start.add(radius, height - radius), radius, 0, widthBorder, arr[3], Pair(180f, 270f))
                // Bottom right
                drawCircleOutline(Start.add(width - radius, height - radius), radius, 0, widthBorder, arr[2], Pair(90f, 180f))



                if (once)
                    VertexUtil.releaseGL()

            }
        }

    }

    /// Border
    // Normal
    fun drawRoundedRectBorder(Start: Vec2f, width: Float, height: Float, radius: Float, widthBorder: Float, cInside: ABColor, cOutside: ABColor, once: Boolean = false) {
        if (once)
            VertexUtil.prepareGl()
        // Inside
        drawRoundedRect(Start, width, height, radius, cInside)
        // Outside
        drawRoundedRectOutline(Start, width, height, radius, widthBorder, cOutside)
        if (once)
            VertexUtil.releaseGL()
    }
    // Gradient
    fun drawRoundedRectBorder(Start: Vec2f, width: Float, height: Float, radius: Float, widthBorder: Float, cInside: Array<ABColor>, insideTopBottom: Boolean = false, cOutside: Array<ABColor>, outsideTopBottom: Boolean = false, once: Boolean = false) {
        if (once)
            VertexUtil.prepareGl()
        // Inside
        drawRoundedRect(Start.add(widthBorder/2 - .8f, widthBorder/2 - .8f), width - widthBorder + 1.2f, height - widthBorder + 1.2f, radius, cInside, once, insideTopBottom)
        // Outside
        drawRoundedRectOutline(Start, width, height, radius, widthBorder, cOutside, outsideTopBottom, once)
        if (once)
            VertexUtil.releaseGL()
    }


    //endregion

    //region line

    fun drawLineStrip(vertices: Array<Vec2f>, lineWidth: Float = 1f, c: ABColor, once: Boolean = false) {
        // Prepare gl
        if (once)
            VertexUtil.prepareGl()

        // Set width
        glLineWidth(lineWidth)

        // Set color and add vertices
        glBegin(GL_LINE_STRIP)
        c.glColor()
        for (vertex in vertices) {
            VertexUtil.add(vertex)
        }
        glEnd()

        // Relase gl
        if (once)
            VertexUtil.releaseGL()
        else
            // Well, we still have to reset lineWidth lol
            glLineWidth(1f)
    }

    fun drawLineStrip(vertices: Array<Vec2f>, lineWidth: Float = 1f, c: ArrayList<ABColor>, once: Boolean = false) {
        // PrepareGl
        if (once)
            VertexUtil.prepareGl()
        // Set lineWidth
        glLineWidth(lineWidth)

        // Add every vertices with color
        glBegin(GL_LINE_STRIP)
        for ((idx, value) in vertices.withIndex()) {
            VertexUtil.add(value, c[idx])
        }
        glEnd()

        // Relase
        if (once)
            VertexUtil.releaseGL()
        else
            // Reset
            glLineWidth(1f)
    }

    fun drawLine(start: Vec2f, end: Vec2f, lineWidth: Float = 1f, c: ABColor, once: Boolean = false) {
        // Preare gl
        if (once) {
            VertexUtil.prepareGl()
        }

        // Set width
        glLineWidth(lineWidth)
        // Set color and start+end
        glBegin(GL_LINES)
        c.glColor()
        glVertex2f(start.x, start.y)
        glVertex2f(end.x, end.y)
        glEnd()

        // End + reset
        if (once) {
            VertexUtil.releaseGL()
        } else  glLineWidth(1f)
    }

    fun drawLine(start: Vec2f, end: Vec2f, lineWidth: Float = 1f, first: ABColor, second: ABColor, once: Boolean = false) {
        // Prepare
        if (once) {
            VertexUtil.prepareGl()
        }

        // Draw a line with 2 differents color
        glLineWidth(lineWidth)
        glBegin(GL_LINES)
        VertexUtil.add(start, first)
        VertexUtil.add(end, second)
        glEnd()

        // Release + reset
        if (once) {
            VertexUtil.releaseGL()
        } else  glLineWidth(1f)
    }

    fun drawLine(start: Vec2f, end: Vec2f, lineWidth: Float = 1f, c: Array<ABColor>, once: Boolean = false) {

        when(c.size) {
            // NO colors lol
            0 -> return
            // 1 color, simple lol
            1 -> drawLine(start, end, lineWidth, c[0], once)
            // 2 colors, simple lo
            2 -> drawLine(start, end, lineWidth, c[0], c[1], once)
            else -> {
                // Prepare
                if (once) {
                    VertexUtil.prepareGl()
                }

                // Get the differences of every lines
                val size = c.size - 1
                val lines = ArrayList<Vec2f>()
                val xDiff = (end.x - start.x)/size
                val yDiff = (end.y - start.y)/size

                // Add with percent
                for(i in 0..size) {
                    lines.add(start.add(xDiff*i, yDiff*i))
                }

                // Simple line draw
                glLineWidth(lineWidth)
                glBegin(GL_LINE_STRIP)

                for((idx, value) in lines.withIndex()) {
                    VertexUtil.add(value, c[idx])
                }

                glEnd()

                // Release
                if (once) {
                    VertexUtil.releaseGL()
                } else  glLineWidth(1f)
            }
        }

    }

    //endregion

    //region Triangle

    /*
        Is this ever going to be used?
     */

    private fun drawTriangleFan(center: Vec2f, vertices: Array<Vec2f>, c: ABColor, once: Boolean = false, onlyVertex: Boolean = false) {
        if (once)
            VertexUtil.prepareGl()
        if (!onlyVertex)
            glBegin(GL_TRIANGLE_FAN)
        c.glColor()
        if (!onlyVertex)
            glVertex2f(center.x, center.y)
        for (vertex in vertices) {
            glVertex2f(vertex.x, vertex.y)
        }
        if (!onlyVertex)
            glEnd()
        if (once)
            VertexUtil.releaseGL()
    }

    private fun drawTriangleFan(center: Vec2f, vertices: Array<Vec2f>, c: ArrayList<ABColor>, once: Boolean = false) {
        if (once)
            VertexUtil.prepareGl()
        glBegin(GL_TRIANGLE_FAN)
        glVertex2f(center.x, center.y)
        for((index, value) in vertices.withIndex()) {
            VertexUtil.add(value, c[index])
        }
        glEnd()
        if (once)
            VertexUtil.releaseGL()
    }

    public fun drawTriangle(pos1: Vec2f, pos2: Vec2f, pos3: Vec2f, c: ABColor, once: Boolean = false) {
        if (once)
            VertexUtil.prepareGl()

        glBegin(GL_TRIANGLES)
        c.glColor()
        VertexUtil.add(pos1)
        VertexUtil.add(pos2)
        VertexUtil.add(pos3)
        glEnd()

        if (once)
            VertexUtil.prepareGl()
    }

    public fun drawTriangle(pos1: Vec2f, pos2: Vec2f, pos3: Vec2f, c: Array<ABColor>, once: Boolean = false) {
        if (c.size != 3)
            return

        if (once)
            VertexUtil.prepareGl()

        glBegin(GL_TRIANGLES)
        VertexUtil.add(pos1, c[0])
        VertexUtil.add(pos2, c[1])
        VertexUtil.add(pos3, c[2])
        glEnd()

        if (once)
            VertexUtil.prepareGl()

    }

    //endregion

    //region Text
    /*
        Justify values:
        0 -> Left
        1 -> Center
        2 -> Right
     */
    fun drawText(text: String, x: Float, y: Float, color: ABColor, justify: Int = 0, fontSize : Float = 1f) {

        // We are not going to waste resources for no lenght ofc
        if (text.length == 0)
            return

        // Get justify
        var xVal = when(justify) {
            1 -> x - mc.fontRenderer.getStringWidth(text) / 2
            2 -> x - mc.fontRenderer.getStringWidth(text)
            else -> x
        };

        // Temp variable for size
        var sizeNow = false

        // Size
        if (fontSize != 1f) {
            GlStateManager.pushMatrix()
            GlStateManager.scale(fontSize, fontSize, fontSize)
            sizeNow = true
        }

        // Draw simple
        mc.fontRenderer.drawString( text, (xVal/fontSize).toInt(), (y/fontSize).toInt(), color.rgb);

        // Reset
        if (sizeNow)
            GlStateManager.popMatrix()
    }

    // Our custom gradient
    val renderString = GradientFontRenderer(mc.gameSettings, ResourceLocation("minecraft", "textures/font/ascii.png"), mc.renderEngine, false)

    // Draw with N colors
    fun drawText(text: String, x:Float, y:Float, c: Array<ABColor>, justify: Int = 0, fontSize: Float = -1f, horizontal: Boolean = true, dropShadow: Boolean = false) {
        when(c.size) {
            // As usual
            0 -> return
            1 -> drawText(text, x, y, c[0], justify, fontSize)
            // 2 colors
            2 -> {
                // Not going to waste
                if (text.length == 0)
                    return

                var xVal = when(justify) {
                    1 -> x - mc.fontRenderer.getStringWidth(text) / 2
                    2 -> x - mc.fontRenderer.getStringWidth(text)
                    else -> x
                };

                var sizeNow = false

                if (sizeNow) {
                    GlStateManager.pushMatrix()
                    GlStateManager.scale(fontSize, fontSize, fontSize)
                    sizeNow = true
                }

                // Draw string with 2 colors
                renderString.drawString( text, xVal, y, c[0].rgb, c[1].rgb, dropShadow, horizontal);

                if (sizeNow)
                    GlStateManager.popMatrix()


            }
            else -> {

                var start = text.length
                if (start == 0)
                    return

                // Drop in case colors are more then letters (I'm not going to overcomplicate this shit and making pixel things)
                c.dropLastWhile {
                    c.size > start
                }

                // We have to get every string with start end colors
                val stringSplitted = ArrayList<String>()
                // We remove 1 because the first color is at beginning
                var nColors = c.size.toFloat() - 1

                // Temp variable
                var textMod = text

                // While we have lenght
                while (start > 0) {
                    // Get nWords we are going to color
                    var nWords : Float = start / nColors
                    // If there is decimal, add 1
                    if (!nWords.rem(1).equals(0f))
                        nWords++

                    // Add substring
                    stringSplitted.add(textMod.substring(0..nWords.toInt() - 1))

                    // Remove what we added
                    textMod = textMod.substring(nWords.toInt())

                    // Decrease start by the number of words and nColors
                    start -= nWords.toInt()
                    nColors--

                }

                // Justify
                var xVal = when(justify) {
                    1 -> x - mc.fontRenderer.getStringWidth(text) / 2
                    2 -> x - mc.fontRenderer.getStringWidth(text)
                    else -> x
                };
                // We have to remember the width of every string
                var addX = 0f

                var sizeNow = false

                // Scale
                if (sizeNow) {
                    GlStateManager.pushMatrix()
                    GlStateManager.scale(fontSize, fontSize, fontSize)
                    sizeNow = true
                }

                // For every string
                for((idx, value) in stringSplitted.withIndex()) {

                    // Draw it form start to finish
                    renderString.drawString( value, xVal + addX, y, c[idx].rgb, c[idx + 1].rgb, dropShadow, horizontal);

                    // Add width
                    addX += renderString.getStringWidth(value)

                }

                // Reset
                if (sizeNow)
                    GlStateManager.popMatrix()
            }
        }
    }

    //endregion

    //region pictures

    // ImageIO.read(javaClass.classLoader.getResource("assets/minecraft/amber/img/testresources.png"))
    // mc.textureManager.getTexture(resourceLocation)
    // ImageIO.read(mc.resourceManager.getResource(resourceLocation).inputStream)
    //mc.ingameGUI.drawTexturedModalRect(x, y, 0, 0, br.width, br.height)

    fun showPicture(x: Int, y: Int, resourceLocation: ResourceLocation, width: Int = -1, height: Int = -1) {
        mc.textureManager.bindTexture(resourceLocation)
        // Get final width
        var widthFinal = width
        var heightFinal = height
        // If we have to get the width of something
        if (width == -1 || height == -1) {
            // Read the resource
            val br = ImageIO.read(mc.resourceManager.getResource(resourceLocation).inputStream)
            // And them get width / height in case is not default
            if (width == -1)
                widthFinal = br.width
            if (height == -1)
                heightFinal = br.height
        }

        GL11.glPushMatrix()
        // Reset color
        GL11.glColor4f(1f, 1f, 1f, 1f)
        // Draw it
        blit(x, y, 0f, 0f, widthFinal, heightFinal, widthFinal.toFloat(), heightFinal.toFloat())
        GL11.glPopMatrix()
    }

    fun showPicture(x: Int, y: Int, resourceLocation: ResourceLocation, width: Int = -1, height: Int = -1, color: ABColor) {
        // Like before
        mc.textureManager.bindTexture(resourceLocation)
        var widthFinal = width
        var heightFinal = height
        if (width == -1 || height == -1) {
            val br = ImageIO.read(mc.resourceManager.getResource(resourceLocation).inputStream)
            if (width == -1)
                widthFinal = br.width
            if (height == -1)
                heightFinal = br.height
        }

        GL11.glPushMatrix();
        // Custom color
        color.glColor()
        blit(x, y, 0f, 0f, widthFinal, heightFinal, widthFinal.toFloat(), heightFinal.toFloat())
        GL11.glPopMatrix();
    }


    //endregion

    //region extensions

    private fun Vec2f.add(center: Vec2f): Vec2f {
        return Vec2f(this.x + center.x, this.y + center.y)
    }

    private fun Vec2f.add(xVal: Float, yVal: Float): Vec2f {
        return Vec2f(this.x + xVal, this.y + yVal)
    }

    private fun Vec2f.times(radius: Float): Vec2f {
        return Vec2f(this.x * radius, this.y * radius)
    }

    //endregion


}


