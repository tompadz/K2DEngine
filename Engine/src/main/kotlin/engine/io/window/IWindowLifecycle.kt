package engine.io.window

internal interface IWindowLifecycle {
    fun create()
    fun loop()
    fun swapBuffers()
    fun destroy()
}