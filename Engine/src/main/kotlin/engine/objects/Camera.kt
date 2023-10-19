package engine.objects

import org.joml.Matrix4f
import org.joml.Vector3f

class Camera(name:String = "Camera") : GameObject(name = name) {

    private var projectionMatrix = Matrix4f()
    private var viewMatrix =  Matrix4f()

    init { adjustProjection() }

    private fun adjustProjection() {
        projectionMatrix.identity()
        projectionMatrix.ortho(0f, 32f * 40f, 0f, 32f * 21f, 0f, 100f)
    }

    fun getProjectionMatrix() = projectionMatrix

    fun getViewMatrix() : Matrix4f {
        val frontCam = Vector3f(0f, 0f, -1f)
        val upCam = Vector3f(0f, 1f, 0f)
        viewMatrix.identity()
        viewMatrix = viewMatrix.lookAt(
            Vector3f(
                transform.position.x,
                transform.position.y,
                20f
            ),
            frontCam.add(
                transform.position.x,
                transform.position.y,
                0f
            ),
            upCam
        )
        return viewMatrix
    }
}