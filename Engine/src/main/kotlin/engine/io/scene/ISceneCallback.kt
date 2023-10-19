package engine.io.scene

interface ISceneCallback {
    fun onUpdate() {}
    fun onCreate() {}
    fun onDestroy() {}
}