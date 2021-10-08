package dev.amber.api.render

abstract class Element(x: Float, y: Float) {

    var x: Float = 0f
    var y: Float = 0f

    fun Element(x: Int, y: Int) {
        this.x = x.toFloat()
        this.y = y.toFloat()
    }

    fun Element(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    abstract fun render()

}