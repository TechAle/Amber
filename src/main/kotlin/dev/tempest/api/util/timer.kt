package dev.tempest.api.util

import dev.amber.client.Amber

/*
    @author TechAle
    @since 07/09/21
 */

class timer {

    val start = System.currentTimeMillis();

    fun endTimer(name : String) {
        Amber.LOGGER.info("[Amber] " + name + " Time: " + (System.currentTimeMillis() - start).toString() + "ms")
    }

}