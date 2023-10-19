package engine.io.window

import engine.io.scene.Scene

interface IWindowCallback {
    fun onUpdate() {}
    fun onCreate() {}
    fun onDestroy() {}
    fun onSceneChange(scene: Scene) {}
    fun onPositionChange(x: Int, y: Int) {}
    fun onSizeChange(width: Int, height: Int) {}
}