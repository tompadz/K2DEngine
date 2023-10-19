package engine.io.window

import engine.render.Color
import engine.io.input.Input
import engine.io.scene.Scene
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT
import org.lwjgl.opengl.GL11.GL_TRUE
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import utils.EngineID
import utils.Log
import utils.OSUtils
import utils.TimeUtils
import utils.TimeUtils.Companion.deltaTime

private const val DEFAULT_WINDOW_WIDTH = 1280
private const val DEFAULT_WINDOW_HEIGHT = 720

abstract class Window(
    var width: Int = DEFAULT_WINDOW_WIDTH,
    var height: Int = DEFAULT_WINDOW_HEIGHT,
    private var title: String = "",
    private val windowResizable: Boolean = true
) : IWindowLifecycle, IWindowCallback {

    val id: Long = EngineID.generate()
    var window: Long = 0L
    val fps: Int get() = (1f / deltaTime).toInt()

    var windowXPosition = 0
        private set

    var windowYPosition = 0
        private set

    private var backgroundColor: Color = Color.BLACK

    companion object {
        var currentScene: Scene? = null
            private set
    }

    /**
     * --------------------------------------------
     * Public fun
     *  --------------------------------------------
     */

    fun changeScene(scene: Scene) {
        if (currentScene?.id != scene.id) {
            currentScene?.destroy()
            currentScene = scene
            currentScene?.create(this)
            onSceneChange(scene)
        }
    }

    fun setBackgroundColor(color: Color) {
        backgroundColor = color
    }

    fun run() {
        create()
        loop()
        destroy()
    }

    /**
     * --------------------------------------------
     * Lifecycle fun
     *  --------------------------------------------
     */

    override fun create() {
        // Set up an error callback. The default implementation
        GLFWErrorCallback.createPrint(System.err).set()
        check(glfwInit()) { "Unable to initialize GLFW" }
        setWindowHints()
        // Create the window
        window = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL)
        if (window == MemoryUtil.NULL) throw RuntimeException("Failed to create the GLFW window")
        // Set window options
        setWindowOptions()
        setWindowSizeAndPosition()
        setInputCallback()
        setWindowCallback()
        // Show monitor
        glfwShowWindow(window)
        Log.i(javaClass.name, "Window '${id}' has been create")
        onCreate()
    }

    override fun loop() {
        var beginTime = TimeUtils.time
        var endTime: Float
        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents()
            with(backgroundColor) { GL11.glClearColor(r, g, b, a) }
            GL11.glClear(GL_COLOR_BUFFER_BIT)
            if (deltaTime >= 0) {
                currentScene?.update()
                onUpdate()
            }
            swapBuffers()
            endTime = TimeUtils.time
            deltaTime = endTime - beginTime
            beginTime = endTime
        }
    }

    override fun swapBuffers() {
        glfwSwapBuffers(window)
    }

    override fun destroy() {
        // Free the window callbacks and destroy the window
        currentScene?.destroy()
        Callbacks.glfwFreeCallbacks(window)
        glfwDestroyWindow(window)
        glfwTerminate() // Terminate GLFW and free the error callback
        glfwSetErrorCallback(null)?.free()
        Log.i(javaClass.name, "Window '${id}' has been destroy")
        onDestroy()
    }

    /**
     * --------------------------------------------
     * Private fun
     *  --------------------------------------------
     */

    private fun setWindowHints() {
        // the window will stay hidden after creation
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, if (windowResizable) GLFW_TRUE else GLFW_FALSE)
        when (OSUtils().getOS()) {
            OSUtils.OS.MAC -> {
                glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
                glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
                // According to Apple docs, non-core profiles are limited to version 2.1.
                glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
                // Should be true for macOS, according to GLFW docs, to get core profile.
                glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)
            }
            OSUtils.OS.LINUX -> {
                glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
                glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
            }
            else -> {}
        }
    }

    private fun setWindowOptions() {
        glfwMakeContextCurrent(window) // Make the OpenGL context current
        glfwSwapInterval(1) // Enable v-sync
        createCapabilities()
    }

    private fun setWindowSizeAndPosition() {
        MemoryStack.stackPush().use { stack ->
            val pWidth = stack.mallocInt(1)
            val pHeight = stack.mallocInt(1)
            val monitor = glfwGetPrimaryMonitor()
            val videoMode = glfwGetVideoMode(monitor) ?: throw RuntimeException("Failed to create video mode")
            windowXPosition = (videoMode.width() - pWidth[0]) / 2
            windowYPosition = (videoMode.height() - pHeight[0]) / 2

            //set size
            glfwGetWindowSize(window, pWidth, pHeight)
            onSizeChange(pWidth[0], pHeight[0])

            //set position
            glfwSetWindowPos(window, windowXPosition, windowYPosition) // Center the window
            onPositionChange(windowXPosition, windowYPosition)
        }
    }

    private fun setInputCallback() {
        with(Input) {
            glfwSetMouseButtonCallback(window, mouseButtonCallback)
            glfwSetCursorPosCallback(window, cursorPositionCallback)
            glfwSetScrollCallback(window, scrollCallback)
            glfwSetKeyCallback(window, keyboardCallback)
        }
    }

    private fun setWindowCallback() {
        glfwSetWindowSizeCallback(window) { _, cWidth, cHeight ->
            width = cWidth
            height = cHeight
            onSizeChange(width, height)
        }
        glfwSetWindowPosCallback(window) { _, xPos, yPos ->
            windowXPosition = xPos
            windowYPosition = yPos
            onPositionChange(windowXPosition, windowYPosition)
        }
    }
}