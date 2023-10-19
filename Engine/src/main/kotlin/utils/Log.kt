package utils

internal class Log {
    companion object {
        fun i(tag:String, message:String) { println("$tag     INFO:      $message") }
        fun <T> i(c:Class<T>, message:String) { println("${c.name}     INFO:      $message") }

        fun e(tag:String, message:String) { System.err.println("$tag     ERROR:      $message") }
        fun <T> e(c:Class<T>, message:String) { System.err.println("${c.name}     ERROR:      $message") }

        fun w(tag:String, message:String) { println("$tag     WARNING:       $message") }
        fun <T> w(c:Class<T>, message:String) { System.err.println("${c.name}     WARNING:      $message") }
    }
}