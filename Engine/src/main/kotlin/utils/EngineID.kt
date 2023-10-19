package utils

import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.abs
import kotlin.random.Random

internal class EngineID {

    companion object {

        fun generate() : Long {
            val uValue = abs(Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)).nextLong())
            return uValue.getLastNDigits()
        }

        private fun Long.getLastNDigits(n: Int = 12): Long {
            val max = if (n < 1) 1 else if (n > 12) 12 else n
            val raw = this.toString()
            return when {
                raw.length < max -> {
                    raw.padStart(max, '0').toLong()
                }
                raw.length > max -> {
                    raw.substring(raw.length - max, raw.length).toLong()
                }
                else -> {
                    raw.toLong()
                }
            }
        }
    }

}