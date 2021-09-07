package dev.amber.backend.events.core

import dev.amber.backend.events.core.imp.Priority

/**
 * @author DarkMagician6
 * @since 07-30-2013
 * Translated to kotlin by TechAle
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class EventTarget(val value: Byte = Priority.MEDIUM)