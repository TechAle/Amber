package dev.tempest.api.util

import dev.amber.client.Amber

class timer() {

    val start = System.currentTimeMillis();

    fun endTimer(name : String) {
        Amber.LOGGER.info("[Amber] " + name + " Time: " + (System.currentTimeMillis() - start).toString() + "ms")
    }

}