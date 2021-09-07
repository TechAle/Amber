package dev.tempest.backend.event.core;

import dev.tempest.backend.event.core.imp.Priority;

import java.lang.annotation.*;

/**
 * @author DarkMagician6
 * @since 07-30-2013
 */

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventTarget {

    byte value() default Priority.MEDIUM;
}
