package dev.amber.api.render

abstract class Element {

    open var x: Float = 0f
    open var y: Float = 0f

    fun Element(x: Int, y: Int) {
        this.x = x.toFloat()
        this.y = y.toFloat()
    }

    fun Element(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    abstract fun render(): Boolean

}