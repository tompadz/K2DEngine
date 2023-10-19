package utils

internal class TimeUtils {
    companion object {
        private val timeStarted =  System.nanoTime()
        val time: Float get() = (System.nanoTime() - timeStarted) * 1E-9f
        var deltaTime: Float = -1f
    }
}