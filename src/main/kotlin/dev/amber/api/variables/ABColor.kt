package dev.amber.api.variables

/*
    @author: TechALe
    @since: 08/09/21
    This is basically gs
    https://github.com/TechAle/gsplusplus/blob/7814860ff21279a4e8a2b80fe899812fd602c6b6/src/main/java/com/gamesense/api/util/render/GSColor.java#L11
 */
import java.awt.Color
import net.minecraft.client.renderer.GlStateManager




class ABColor : Color {

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

    fun glColor() {
        GlStateManager.color(red / 255.0f, green / 255.0f, blue / 255.0f, alpha / 255.0f)
    }
}