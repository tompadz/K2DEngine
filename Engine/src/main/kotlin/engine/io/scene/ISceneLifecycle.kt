package engine.io.scene
import engine.io.window.Window

internal interface ISceneLifecycle {
    fun create(window: Window)
    fun update()
    fun destroy()
}