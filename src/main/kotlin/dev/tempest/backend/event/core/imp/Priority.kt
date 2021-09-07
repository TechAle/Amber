package dev.tempest.backend.event.core.imp

/**
 * @author DarkMagician6
 * @since 08-03-2013
 */
object Priority {
    const val HIGHEST: Byte = 0
    const val HIGH: Byte = 1
    const val MEDIUM: Byte = 2
    const val LOW: Byte = 3
    const val LOWEST: Byte = 4
    val VALUE_ARRAY = byteArrayOf(
            HIGHEST,
            HIGH,
            MEDIUM,
            LOW,
            LOWEST
    )
}