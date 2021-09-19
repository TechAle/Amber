package dev.amber.api.render

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
import net.minecraft.client.renderer.Tessellator
import net.minecraft.realms.RealmsScreen.blit
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import javax.imageio.ImageIO


object RenderUtil2d {

    //region Rect

    /// Normal rect
    // Normal
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
    // Gradient
    fun drawRect(Start: Vec2f, width: Float, height: Float, once : Boolean = false, colors: Array<ABColor>, topBottom: Boolean = false) {

        when(colors.size) {
            // No colors, return
            0 -> return
            // Draw normal rect
            1 -> drawRect(Start, width, height, colors[0], once)
            // Every other cases
            else -> {
                val arr =  when (colors.size) {
                    2, 3 -> if (topBottom) arrayOf(colors[0], colors[1], colors[1], colors[0])
                            else arrayOf(colors[0], colors[0], colors[1], colors[1])
                    else -> Array(4) {colors[it]}
                }

                // Prepare gl
                if (once)
                    VertexUtil.prepareGl()

                // Draw with vertixes
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
        if (once)
            VertexUtil.prepareGl()
        drawArcFilled(center, radius, angleRange, segments, color, onlyVertex)
        if (once)
            VertexUtil.releaseGL()
    }
    private fun drawArcFilled(center: Vec2f, radius: Float, angleRange: Pair<Float, Float>, segments: Int = 0, color: ABColor, onlyVertex: Boolean = false) {
        val arcVertices = getArcVertices(center, radius, angleRange, segments)
        drawTriangleFan(center, arcVertices, color, false, onlyVertex)
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

    // Gradient
    fun drawCircleFilled(center: Vec2f, radius: Float, segments: Int = 0, color: Array<ABColor>, angleRange: Pair<Float, Float> = Pair(0f, 360f), once: Boolean = false) {

        when (color.size) {
            0 -> return
            1 -> drawCircleFilled(center, radius, segments, color[0], angleRange, once)
            else -> {
                if (once)
                    VertexUtil.prepareGl()
                drawArcFilled(center, radius, angleRange, segments, color)
                if (once)
                    VertexUtil.releaseGL()
            }

        }
    }
    private fun drawArcFilled(center: Vec2f, radius: Float, angleRange: Pair<Float, Float>, segments: Int = 0, color: Array<ABColor>) {
        val arcVertices = getArcVertices(center, radius, angleRange, segments)

        drawTriangleFan(center, arcVertices, getGradientVertices(color, arcVertices), false)
    }

    fun getGradientVertices(color: Array<ABColor>, arcVertices: Array<Vec2f>) : ArrayList<ABColor> {
        val pieces = arcVertices.size / (color.size.toFloat() - 1)
        var finalColors = arrayListOf<ABColor>()
        for (i in 0..color.size - 2) {
            val red = color[i].red
            val blue = color[i].blue
            var green = color[i].green
            var alpha = color[i].alpha
            val rChange = (color[i + 1].red - red)/pieces
            val gChange = (color[i + 1].green - green)/pieces
            val bChange = (color[i + 1].blue - blue)/pieces
            val aChange = (color[i + 1].alpha - alpha)/pieces
            for(j in 0..pieces.toInt() - 1) {
                finalColors.add(ABColor(red + (rChange * j).toInt(), green + (gChange*j).toInt(), blue + (bChange*j).toInt(), alpha + (aChange*j).toInt()))
            }

        }

        if (finalColors.size > arcVertices.size) {
            while (finalColors.size != arcVertices.size)
                finalColors.removeLast()
        } else if (finalColors.size < arcVertices.size)
            while (finalColors.size != arcVertices.size)
                finalColors.add(color[color.size - 1])

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
        if (once)
            VertexUtil.prepareGl()
        drawArcOutline(center, radius, angleRange, segments, lineWidth, color)
        if (once)
            VertexUtil.releaseGL()
    }
    private fun drawArcOutline( center: Vec2f, radius: Float, angleRange: Pair<Float, Float>, segments: Int = 0, lineWidth: Float = 1f, color: Array<ABColor>) {
        val arcVertices = getArcVertices(center, radius, angleRange, segments)
        drawLineStrip(arcVertices, lineWidth, getGradientVertices(color, arcVertices))
    }


    /// Border circle
    // Normal
    fun drawCircleBorder(center: Vec2f, radius: Float, segments: Int = 0, lineWidth: Float = 1f, insideC: ABColor, outsideC: ABColor, angleRange: Pair<Float, Float> = Pair(0f, 360f), once: Boolean = false) {
        if (once)
            VertexUtil.prepareGl()

        drawCircleFilled(center, radius - lineWidth, segments, insideC, angleRange, false)
        drawCircleOutline(center, radius, segments, lineWidth + 2, outsideC, angleRange, false)

        if (once)
            VertexUtil.releaseGL()
    }
    // Gradient
    fun drawCircleBorder(center: Vec2f, radius: Float, segments: Int = 0, lineWidth: Float = 1f, insideC: Array<ABColor>, outsideC: Array<ABColor>, angleRange: Pair<Float, Float> = Pair(0f, 360f), once: Boolean = false) {
        if (once)
            VertexUtil.prepareGl()

        drawCircleFilled(center, radius - lineWidth, segments, insideC, angleRange, false)
        drawCircleOutline(center, radius - lineWidth/2 - .5f, segments, lineWidth + 2, outsideC, angleRange, false)

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
                    2, 3 -> if (topBottom) arrayOf(colors[0], colors[1], colors[1], colors[0])
                    else arrayOf(colors[0], colors[0], colors[1], colors[1])
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
                drawCircleOutline(Start.add(width - radius, radius), radius, 0, widthBorder, colors[1], Pair(0f, 90f))
                // Top left
                drawCircleOutline(Start.add(radius, radius), radius, 0, widthBorder, colors[0], Pair(270f, 360f))
                // Bottom left
                drawCircleOutline(Start.add(radius, height - radius), radius, 0, widthBorder, colors[3], Pair(180f, 270f))
                // Bottom right
                drawCircleOutline(Start.add(width - radius, height - radius), radius, 0, widthBorder, colors[2], Pair(90f, 180f))



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
        drawRoundedRect(Start, width, height, radius, cInside)
        drawRoundedRectOutline(Start, width, height, radius, widthBorder, cOutside)
        if (once)
            VertexUtil.releaseGL()
    }
    // Gradient
    fun drawRoundedRectBorder(Start: Vec2f, width: Float, height: Float, radius: Float, widthBorder: Float, cInside: Array<ABColor>, insideTopBottom: Boolean = false, cOutside: Array<ABColor>, outsideTopBottom: Boolean = false, once: Boolean = false) {
        if (once)
            VertexUtil.prepareGl()
        drawRoundedRect(Start, width, height, radius, cInside, insideTopBottom)
        drawRoundedRectOutline(Start, width, height, radius, widthBorder, cOutside, outsideTopBottom)
        if (once)
            VertexUtil.releaseGL()
    }


    //endregion

    //region line

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

    fun drawLineStrip(vertices: Array<Vec2f>, lineWidth: Float = 1f, c: ArrayList<ABColor>) {
        VertexUtil.prepareGl()
        glLineWidth(lineWidth)

        glBegin(GL_LINE_STRIP)
        for ((idx, value) in vertices.withIndex()) {
            VertexUtil.add(value, c[idx])
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

    fun drawLine(start: Vec2f, end: Vec2f, lineWidth: Float = 1f, first: ABColor, second: ABColor, once: Boolean = false) {
        if (once) {
            VertexUtil.prepareGl()
        }

        glLineWidth(lineWidth)
        glBegin(GL_LINES)
        VertexUtil.add(start, first)
        VertexUtil.add(end, second)
        glEnd()

        if (once) {
            VertexUtil.releaseGL()
        } else  glLineWidth(1f)
    }

    fun drawLine(start: Vec2f, end: Vec2f, lineWidth: Float = 1f, c: Array<ABColor>, once: Boolean = false) {

        when(c.size) {
            0 -> return
            1 -> drawLine(start, end, lineWidth, c[0], once)
            2 -> drawLine(start, end, lineWidth, c[0], c[1], once)
            else -> {
                if (once) {
                    VertexUtil.prepareGl()
                }

                val size = c.size - 1
                val lines = ArrayList<Vec2f>()
                val xDiff = (end.x - start.x)/size
                val yDiff = (end.y - start.y)/size

                for(i in 0..size) {
                    lines.add(start.add(xDiff*i, yDiff*i))
                }


                glLineWidth(lineWidth)
                glBegin(GL_LINE_STRIP)

                for((idx, value) in lines.withIndex()) {
                    VertexUtil.add(value, c[idx])
                }

                glEnd()

                if (once) {
                    VertexUtil.releaseGL()
                } else  glLineWidth(1f)
            }
        }

    }

    //endregion

    //region Triangle

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

    //endregion

    //region Text
    /*
        Justify values:
        0 -> Left
        1 -> Center
        2 -> Right
     */
    fun drawText(text: String, x: Float, y: Float, color: ABColor, justify: Int = 0, fontSize : Float = -1f) {

        if (text.length == 0)
            return

        var xVal = when(justify) {
            1 -> x - mc.fontRenderer.getStringWidth(text) / 2
            2 -> x - mc.fontRenderer.getStringWidth(text)
            else -> x
        };

        var sizeNow = false

        if (fontSize != -1f) {
            GlStateManager.pushMatrix()
            GlStateManager.scale(fontSize, fontSize, fontSize)
            sizeNow = true
        }

        mc.fontRenderer.drawString( text, xVal.toInt(), y.toInt(), color.rgb);

        if (sizeNow)
            GlStateManager.popMatrix()
    }

    val renderString = GradientFontRenderer(mc.gameSettings, ResourceLocation("minecraft", "textures/font/ascii.png"), mc.renderEngine, false)

    fun drawText(text: String, x:Float, y:Float, c: Array<ABColor>, justify: Int = 0, fontSize: Float = -1f, horizontal: Boolean = true, dropShadow: Boolean = false) {
        when(c.size) {
            0 -> return
            1 -> drawText(text, x, y, c[0], justify, fontSize)
            2 -> {
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


                renderString.drawString( text, xVal, y, c[0].rgb, c[1].rgb, dropShadow, horizontal);

                if (sizeNow)
                    GlStateManager.popMatrix()


            }
            else -> {

                var start = text.length
                if (start == 0)
                    return

                c.dropLastWhile {
                    c.size > start
                }

                val stringSplitted = ArrayList<String>()
                var nColors = c.size.toFloat() - 1

                var textMod = text

                while (start > 0) {
                    var nWords : Float = start / nColors
                    if (!nWords.rem(1).equals(0f))
                        nWords++

                    stringSplitted.add(textMod.substring(0..nWords.toInt() - 1))

                    textMod = textMod.substring(nWords.toInt())

                    start -= nWords.toInt()
                    nColors--

                }


                var xVal = when(justify) {
                    1 -> x - mc.fontRenderer.getStringWidth(text) / 2
                    2 -> x - mc.fontRenderer.getStringWidth(text)
                    else -> x
                };
                var addX : Float = 0f

                var sizeNow = false

                if (sizeNow) {
                    GlStateManager.pushMatrix()
                    GlStateManager.scale(fontSize, fontSize, fontSize)
                    sizeNow = true
                }

                for((idx, value) in stringSplitted.withIndex()) {

                    renderString.drawString( value, xVal + addX, y, c[idx].rgb, c[idx + 1].rgb, dropShadow, horizontal);

                    addX += renderString.getStringWidth(value)

                }

                if (sizeNow)
                    GlStateManager.popMatrix()
            }
        }
    }

    private fun drawHelloTextWithGradient(x: Int, y: Int, topColor: Int, bottomColor: Int, gradientFontRenderer : GradientFontRenderer, text: String) {
        drawCenteredGradientString(gradientFontRenderer, text, x, y - 4, topColor, bottomColor)
    }

    fun drawCenteredGradientString(fontRendererIn: GradientFontRenderer, text: String, x: Int, y: Int, color: Int, colorBottom: Int) {
        fontRendererIn.drawString(text, (x - fontRendererIn.getStringWidth(text) / 2).toFloat(), y.toFloat(), color, colorBottom, false, true)
    }

    //endregion

    //region pictures

    fun showPicture(x: Int, y: Int, resourceLocation: ResourceLocation, width: Int = -1, height: Int = -1) {
        mc.textureManager.bindTexture(resourceLocation)
        // ImageIO.read(javaClass.classLoader.getResource("assets/minecraft/amber/img/testresources.png"))
        // mc.textureManager.getTexture(resourceLocation)
        // ImageIO.read(mc.resourceManager.getResource(resourceLocation).inputStream)
        //mc.ingameGUI.drawTexturedModalRect(x, y, 0, 0, br.width, br.height)
        var widthFinal = width
        var heightFinal = height
        if (width == -1 || height == -1) {
            val br = ImageIO.read(mc.resourceManager.getResource(resourceLocation).inputStream)
            if (width == -1)
                widthFinal = br.width
            if (height == -1)
                heightFinal = br.height
        }
        blit(0, 0, 0f, 0f, widthFinal, heightFinal, widthFinal.toFloat(), heightFinal.toFloat())
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


