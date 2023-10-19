package engine.io.scene

import engine.io.window.Window
import engine.objects.Camera
import engine.objects.GameObject
import engine.render.Renderer
import org.joml.Vector2f
import utils.EngineID

abstract class Scene : ISceneLifecycle, ISceneCallback {

    lateinit var camera : Camera
        private set

    protected lateinit var requireWindow: Window
        private set


    val id: Long = EngineID.generate()

    private var isRunning: Boolean = false
    private val gameObjects: MutableList<GameObject> = mutableListOf()
    protected val renderer = Renderer()

    /**
     * --------------------------------------------
     * Public
     *  --------------------------------------------
     */

    fun addGameObject(gameObject:GameObject) {
        gameObjects.add(gameObject)
        if (isRunning) {
            gameObject.start()
            renderer.add(gameObject)
        }
    }

    /**
     * --------------------------------------------
     * Lifecycle
     * --------------------------------------------
     */

    override fun create(window: Window) {
        requireWindow = window
        isRunning = true
        createCamera()
        startGameObjects()
        onCreate()
    }

    override fun update() {
        updateGameObjects()
        renderer.render()
        onUpdate()
    }

    override fun destroy() {
        isRunning = false
        destroyGameObjects()
        onDestroy()
    }

    /**
     * --------------------------------------------
     * Private
     * --------------------------------------------
     */

    private fun createCamera() {
        camera = Camera()
    }

    private fun startGameObjects() {
        camera.start()
        for (gameObject in gameObjects) {
            gameObject.start()
            renderer.add(gameObject)
        }
    }

    private fun updateGameObjects() {
        camera.update()
        for (gameObject in gameObjects) {
            gameObject.update()
        }
    }

    private fun destroyGameObjects() {
        for (gameObject in gameObjects) {
            gameObject.destroy()
        }
        gameObjects.clear()
    }
}