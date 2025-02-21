package meteor.logger

import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Logger(var scope: String = "main") {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val logger = Logger()
            logger.trace("trace")
            logger.info("info")
            logger.debug("debug")
            logger.warn("warn")
            logger.error("error")
            logger.error(RuntimeException("FATAL"))
        }
    }

    fun trace(text: String) {
        print(LoggerLevel.TRACE, text)
    }

    fun info(text: String) {
        print(LoggerLevel.INFO, text)
    }

    fun debug(text: String) {
        print(LoggerLevel.DEBUG, text)
    }

    fun warn(text: String) {
        print(LoggerLevel.WARN, text)
    }

    fun error(text: String) {
        print(LoggerLevel.ERROR, text)
    }

    fun error(e: Exception) {
        print(LoggerLevel.ERROR, e.toString())
        for (line in e.stackTrace)
            print(LoggerLevel.ERROR, line.toString())
    }

    fun print(level: LoggerLevel, text: String) {
        print(when (level) {
            LoggerLevel.TRACE -> ConsoleColor.DARK_GRAY
            LoggerLevel.INFO -> ConsoleColor.GRAY
            LoggerLevel.DEBUG -> ConsoleColor.GREEN
            LoggerLevel.WARN -> ConsoleColor.YELLOW
            LoggerLevel.ERROR -> ConsoleColor.BRIGHT_RED
        })
        val line = "[${getCurrentTime()}] [$scope]: $text${ConsoleColor.RESET}"
        println(line)
    }


    fun getCurrentTime(): String {
        val currentTime = LocalTime.now()
        return currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
    }
}