package dev.amber.api.variables

/*
    @author: TechALe
    @since: 08/09/21
    Took inspiration from gs
 */
import net.minecraft.client.renderer.GlStateManager
import java.awt.Color


class ABColor : Color {

    var rainbow: Boolean = false
    var desyncRainbow: Int = 0
    var rainbowSpeed: Float = 1f
    /*
        We have a lot of types of constructors lol
     */
    // just rgb
    constructor(rgb: Int) : super(rgb)
    // rgb with alpha
    constructor(rgba: Int, hasalpha: Boolean) : super(rgba, hasalpha)
    // rbg separated
    constructor(r: Int, g: Int, b: Int) : super(r, g, b)
    // rgba separated
    constructor(r: Int, g: Int, b: Int, a: Int) : super(r, g, b, a)
    // From another color
    constructor(color: Color) : super(color.red, color.green, color.blue, color.alpha)
    // From ABColor
    constructor(color: ABColor, a : Int) : super(color.red, color.green, color.blue, a)
    // Rainbow
    constructor(rainbow: Boolean, desyncRainbow: Int = 0, rainbowSpeed: Float = 1f, alpha: Int = 255) : super(0, 0, 0, alpha) {
        this.rainbow = true
        this.desyncRainbow = desyncRainbow
        this.rainbowSpeed = rainbowSpeed
    }

    // Utilities
    fun fromHSB(hue: Float, saturation: Float, brightness: Float): ABColor {
        return ABColor(getHSBColor(hue, saturation, brightness))
    }

    fun getHue(): Float {
        return RGBtoHSB(red, green, blue, null)[0]
    }

    fun getSaturation(): Float {
        return RGBtoHSB(red, green, blue, null)[1]
    }

    fun getBrightness(): Float {
        return RGBtoHSB(red, green, blue, null)[2]
    }

    fun getRainbow(): ABColor {
        return this.fromHSB(((System.currentTimeMillis() * rainbowSpeed + this.desyncRainbow * 100) % (360 * 32)) / (360f * 32), 1f, 1f);
    }

    fun glColor() {
        if (rainbow) {
            val color = getRainbow()
            GlStateManager.color(color.red / 255.0f, color.green / 255.0f, color.blue / 255.0f, alpha / 255.0f)
        } else
            GlStateManager.color(red / 255.0f, green / 255.0f, blue / 255.0f, alpha / 255.0f)
    }
}