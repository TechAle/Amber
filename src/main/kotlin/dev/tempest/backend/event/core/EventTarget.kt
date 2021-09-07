package dev.tempest.backend.event.core

import dev.tempest.backend.event.core.imp.Priority

/**
 * @author DarkMagician6
 * @since 07-30-2013
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class EventTarget(val value: Byte = Priority.MEDIUM)