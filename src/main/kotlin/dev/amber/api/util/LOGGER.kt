package dev.amber.api.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.*
import java.util.logging.Formatter


object LOGGER {

    private val log = Logger.getLogger("Amber")
    private var time = hashMapOf<String, Long>()

    fun startTimer(name: String) {
        time[name] = System.currentTimeMillis()
        info("Started timing process $name")
    }

    fun endTimer(name: String) {
        info("Ended timing process " + name + " with " + ( System.currentTimeMillis() - time.remove(name)!!) + "ms")
    }

    fun info(message: String) {
        log.info(message)
    }

    fun warning(message: String) {
        log.warning(message)
    }

    fun severe(message: String) {
        log.severe(message)
    }

    init {
        log.useParentHandlers = false
        val formatter = MyFormatter()
        val handler = ConsoleHandler()
        handler.formatter = formatter

        log.addHandler(handler)
    }

    // https://kodejava.org/how-do-i-create-a-custom-logger-formatter/
    private class MyFormatter : Formatter() {
        override fun format(record: LogRecord): String {
            val builder = StringBuilder(1000)
            builder.append("[").append(df.format(Date(record.millis))).append("] ")
            builder.append("[Amber] ")
            builder.append("[").append(record.level).append("] ")
            builder.append(formatMessage(record))
            builder.append("\n")
            return builder.toString()
        }

        override fun getHead(h: Handler): String {
            return super.getHead(h)
        }

        override fun getTail(h: Handler): String {
            return super.getTail(h)
        }

        companion object {
            // Create a DateFormat to format the logger timestamp.
            private val df: DateFormat = SimpleDateFormat("HH:mm:ss")
        }
    }

}