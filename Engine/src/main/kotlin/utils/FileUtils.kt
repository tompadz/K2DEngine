package utils

import java.io.File
import java.io.IOException
import java.lang.StringBuilder

internal class FileUtils {
    fun loadAsString(path:String) : String {
        val result = StringBuilder()
        try {
           val file = File(path)
            file.readLines().forEach {
                result.append(it).append("\n")
            }
        }catch (e: IOException) {
            Log.e(this.javaClass.name, "file read error - ${e.message.toString()}")
        }
        return result.toString()
    }
}