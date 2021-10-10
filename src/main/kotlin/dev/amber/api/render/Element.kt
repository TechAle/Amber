package dev.amber.api.render

abstract class Element {

    open var x: Float = 0f
    open var y: Float = 0f

    abstract fun render(): Boolean

}