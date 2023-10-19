package engine.io.input

import org.lwjgl.glfw.*

class Input {
    companion object {

        private var keyboardKeys = BooleanArray(350)
        private var mouseButtons = BooleanArray(3)

        var isDragging = false
            private set

        var scrollX: Double = 0.0
            private set

        var scrollY: Double = 0.0
            private set

        var xPos: Double = 0.0
            private set

        var yPos: Double = 0.0
            private set

        var lastXPos: Double = 0.0
            private set

        var lastYPos: Double = 0.0
            private set

        fun getKeyDown(key: Int) = keyboardKeys[key]

        fun getButtonDown(button: Int) = mouseButtons[button]

        internal val keyboardCallback: GLFWKeyCallback = object : GLFWKeyCallback() {
            override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
                keyboardKeys[key] = action == GLFW.GLFW_PRESS
            }
        }

        internal val mouseButtonCallback: GLFWMouseButtonCallback = object : GLFWMouseButtonCallback() {
            override fun invoke(window: Long, button: Int, action: Int, mods: Int) {
                when (action) {
                    GLFW.GLFW_PRESS -> {
                        if (button < mouseButtons.size) {
                            mouseButtons[button] = true
                        }
                    }

                    GLFW.GLFW_RELEASE -> {
                        if (button < mouseButtons.size) {
                            mouseButtons[button] = false
                            isDragging = false
                        }
                    }
                }
            }
        }

        internal val cursorPositionCallback: GLFWCursorPosCallback = object : GLFWCursorPosCallback() {
            override fun invoke(window: Long, xpos: Double, ypos: Double) {
                lastXPos = xPos
                lastYPos = yPos
                xPos = xpos
                yPos = ypos
                isDragging = mouseButtons[0] || mouseButtons[1] || mouseButtons[2]
            }
        }

        internal val scrollCallback: GLFWScrollCallback = object : GLFWScrollCallback() {
            override fun invoke(window: Long, xoffset: Double, yoffset: Double) {
                scrollX = xoffset
                scrollY = xoffset
            }
        }
    }
}